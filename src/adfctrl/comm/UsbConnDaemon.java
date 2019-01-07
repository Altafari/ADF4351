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

public class UsbConnDaemon {
    
    public enum DeviceStatus {
        Disconnected,
        Connected,
        Initialized,
        TransferFailed
    }
    
    public final Observable<DeviceStatus> deviceStatus;
    private final Context context;
    private Device device;
    private final DeviceHandle dHandle;
    private final Timer scanTimer;
    private final int TIMEOUT = 1000;
    private final long SCAN_INTERVAL = 100;
    private final int VENDOR_ID = 0x856;
    private final int PRODUCT_ID = 0x5566;
    
    private class BinaryConfigObserver implements IObserver<byte[]> {
        @Override
        public void notifyChanged(byte[] newVal) {
            onConfigChange(newVal);
        }        
    };
    private class ShutdownHookThread extends Thread {
        @Override
        public void run() {
            LibUsb.exit(context);
        }
    }
    
    public UsbConnDaemon() {
        context = new Context();
        dHandle = new DeviceHandle();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
        deviceStatus = new Observable<DeviceStatus>();
        scanTimer = new Timer(true);
        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                onTimer();
            }
        };
        
        scanTimer.scheduleAtFixedRate(tTask, SCAN_INTERVAL, SCAN_INTERVAL);
    }
    
    private void onConfigChange(byte[] config) {
        if (device == null) return;
        if (deviceStatus.getValue() != DeviceStatus.Initialized) return;
        try {
            writeConfigToDevice(config);
        }
        catch (Exception e) {
            
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
            Device dev = acquireDevice();
            if (dev != null) {
                device = dev;
                deviceStatus.notifyChanged(DeviceStatus.Connected);
            }
            break;
        case Connected:
            boolean success = openDevice();
            if (success) {
                deviceStatus.notifyChanged(DeviceStatus.Initialized);
            } else {
                closeDevice();
                deviceStatus.notifyChanged(DeviceStatus.Disconnected);
            }
            break;
        case Initialized:
            break;
        case TransferFailed:
            closeDevice();
            deviceStatus.notifyChanged(DeviceStatus.Disconnected);
            break;
        }
    }
    
    private Device acquireDevice() {
        DeviceList list = new DeviceList();
        try
        {
            int result = LibUsb.getDeviceList(context, list);
            if (result < 0) throw new LibUsbException("Unable to get device list", result);
            for (Device device: list)
            {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, descriptor);
                if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
                if (descriptor.idVendor() == VENDOR_ID && descriptor.idProduct() == PRODUCT_ID) return device;
            }
        }
        finally
        {
            LibUsb.freeDeviceList(list, true);
        }
        return null;
    }
    
    private boolean openDevice() {
        int res = LibUsb.open(device, dHandle);
        return res == LibUsb.SUCCESS;
    }
    
    private void closeDevice() {
        LibUsb.close(dHandle);
    }
}
