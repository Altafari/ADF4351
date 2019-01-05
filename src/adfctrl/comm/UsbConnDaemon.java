package adfctrl.comm;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

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
        Initialized
    }
    
    public final Observable<DeviceStatus> deviceStatus;
    
    public final Observable<Device> device;
    
    private final int TIMEOUT = 1000;
    
    private final long SCAN_INTERVAL = 100;
    
    private final Timer scanTimer;
    
    private class BinaryConfigObserver implements IObserver<byte[]> {

        @Override
        public void notifyChanged(byte[] newVal) {
            if (deviceStatus.getValue() == DeviceStatus.Initialized) {
                DeviceHandle dHandle = new DeviceHandle(); 
                int res = LibUsb.open(device.getValue(), dHandle);
                if (res != 0)
                    throw new RuntimeException();
                ByteBuffer buffer = ByteBuffer.allocateDirect(newVal.length);
                buffer.put(newVal);
                buffer.rewind();
                int transfered = LibUsb.controlTransfer(dHandle,
                    (byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE),
                    (byte) 0x09, (short) 2, (short) 1, buffer, TIMEOUT);
                if (transfered < 0)
                    throw new LibUsbException("Control transfer failed", transfered);
                if (transfered != newVal.length)
                    throw new RuntimeException("Not all data was sent to device");
            }
        }        
    };
    
    public UsbConnDaemon() {
        deviceStatus = new Observable<DeviceStatus>();
        device = new Observable<Device>();
        scanTimer = new Timer(true);
        TimerTask tTask = new TimerTask() {
            @Override
            public void run() {
                onTimer();
            }
        };
        scanTimer.scheduleAtFixedRate(tTask, SCAN_INTERVAL, SCAN_INTERVAL);
    }
    
    private void onTimer() {
        switch(deviceStatus.getValue()) {
        case Disconnected:
            Device dev = acquireDevice();
            if (dev != null) {
                device.notifyChanged(dev);
                deviceStatus.notifyChanged(DeviceStatus.Connected);
            }
            break;
        case Connected:
            boolean success = initializeDevice();
            if (success) {
                deviceStatus.notifyChanged(DeviceStatus.Initialized);
            } else {
                disconnectDevice();
                deviceStatus.notifyChanged(DeviceStatus.Disconnected);
            }
            break;
        case Initialized:
            break;
        }
    }
    
    private Device acquireDevice() {
        return null;
    }
    
    private boolean initializeDevice() {
        return true;
    }
    
    private void disconnectDevice() {
        
    }
}
