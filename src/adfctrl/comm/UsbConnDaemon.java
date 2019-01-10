package adfctrl.comm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import adfctrl.utils.IObservable;
import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public class UsbConnDaemon implements IObserver<byte[]> {
    
    public enum DeviceStatus {
        Disconnected,
        Connected,
        Initialized,
        TransferFailed
    }
    
    public final Observable<DeviceStatus> deviceStatus;
    
    private Device device;
    private final Context context;
    private final DeviceHandle dHandle;
    private final int vid;
    private final int pid;
    private final Timer scanTimer;
    private final int TIMEOUT = 1000;
    private final long SCAN_INTERVAL = 100;
    
    private class ShutdownHookThread extends Thread {
        @Override
        public void run() {
            LibUsb.exit(context);
        }
    }
    
    public UsbConnDaemon(int vendorId, int productId) {
        vid = vendorId;
        pid = productId;
        context = new Context();
        dHandle = new DeviceHandle();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
        deviceStatus = new Observable<DeviceStatus>(DeviceStatus.Disconnected);
        scanTimer = new Timer(true);
        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                onTimer();
            }
        };
        
        scanTimer.scheduleAtFixedRate(tTask, SCAN_INTERVAL, SCAN_INTERVAL);
    }
    
    @Override
    public void notifyChanged(byte[] newVal) {
        onConfigChange(newVal);
    }
    
    private void onConfigChange(byte[] config) {
        if (device == null) return;
        if (deviceStatus.getValue() != DeviceStatus.Initialized) return;
        try {
            writeConfigToDevice(config);
        }
        catch (Exception e) {
            System.out.println("Error occured during communication with device");
            closeDevice();
        }
        
    }
    
    private void writeConfigToDevice(byte[] config) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(config.length);
        buffer.put(config);
        buffer.rewind();
        int transfered = LibUsb.controlTransfer(dHandle,
             (byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE),
             (byte) 0x09, (short) 2, (short) 1, buffer, TIMEOUT);
        if (transfered < 0)
            throw new IOException("Control transfer failed, error code " + transfered);
        if (transfered != config.length)
            throw new IOException("Not all data was sent to device");
    }
    
    private void onTimer() {
        switch(deviceStatus.getValue()) {
        case Disconnected:
            acquireDevice();
            break;
        case Connected:
            openDevice();
            break;
        case Initialized:
            break;
        case TransferFailed:
            closeDevice();
            break;
        }
    }
    
    private Device acquireDevice() {
        DeviceList list = new DeviceList();
        try
        {
            int result = LibUsb.getDeviceList(context, list);
            if (result < 0) throw new LibUsbException("Unable to get device list", result);
            for (Device dev: list) {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(dev, descriptor);
                if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
                if (descriptor.idVendor() == vid && descriptor.idProduct() == pid) {
                    LibUsb.refDevice(dev);
                    if (dev != null) {
                        device = dev;
                        deviceStatus.notifyChanged(DeviceStatus.Connected);
                    }
                }
            }
        }
        finally {
            LibUsb.freeDeviceList(list, true);
        }
        return null;
    }
    
    private void openDevice() {
        int res = LibUsb.open(device, dHandle);
        if (res != LibUsb.SUCCESS) return;
        res = LibUsb.claimInterface(dHandle, 0);
        if (res == LibUsb.SUCCESS) {
            deviceStatus.notifyChanged(DeviceStatus.Initialized);
        } else {
            closeDevice();
        }
    }
    
    private void closeDevice() {
        LibUsb.releaseInterface(dHandle, 0);
        LibUsb.close(dHandle);
        LibUsb.unrefDevice(device);
        deviceStatus.notifyChanged(DeviceStatus.Disconnected);
    }
    
    public static void main(String args[]) {
        UsbConnDaemon ucd = new UsbConnDaemon(0x0483, 0x5750);
        IObservable<DeviceStatus> dStatus = ucd.deviceStatus;
        while (true) {
            while (dStatus.getValue() != DeviceStatus.Initialized) {
                synchronized (ucd) {
                    try {
                        ucd.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            ucd.notifyChanged(new byte[] {0x55, 0x66, 0x77, 0x22, 0x15});
            System.out.println("Message was successfully sent");
            while (dStatus.getValue() != DeviceStatus.Disconnected) {
                synchronized (ucd) {
                    try {
                        ucd.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ucd.notifyChanged(new byte[] {0x55, 0x66, 0x77, 0x22, 0x15});
                }
            }
        }
    }
}
