package adfctrl.icmodel;

import java.util.List;
import java.util.function.Predicate;

import adfctrl.icmodel.ADF4351Proxy.*;
import adfctrl.utils.Observable;

public class ADF4351Configurator {
    
    public enum ReferenceMode {
        DIV2, NORM, X2
    }
    
    public enum SynthMode {
        INTEGER, FRACTIONAL
    }
    
    public enum FunctionId {
        VCO_FREQ, PFD_FREQ, OUT_FREQ, AUX_FREQ, VCO_SEL_FREQ
    }
    
    private class CustomObservable<T> extends Observable<T> {
        
        public CustomObservable(T val) {
            super(val);
        }
        
        @Override
        protected void notifyObservers() {
            super.notifyObservers();
            ADF4351Configurator.this.onConfigChanged();
        }
    }

    public final Observable<Double> referenceFrequency;
    public final Observable<ReferenceMode> referenceMode;
    public final Observable<SynthMode> synthMode;
    public final Observable<Integer> intValue;
    public final Observable<Integer> fracValue;
    public final Observable<Integer> modValue;
    public final Observable<Integer> rCounter;
    public final Observable<PrescalerMode> prescalerMode;
    public final Observable<FeedbackMode> feedbackMode;
    public final Observable<NoiseMode> noiseMode;
    public final Observable<PowerMode> outputPower;
    public final Observable<Boolean> outputControl;
    public final Observable<PowerMode> auxPower;
    public final Observable<Boolean> auxControl;
    public final Observable<AuxMode> auxMode;
    public final Observable<RfDivider> rfDividerMode;
    public final Observable<Integer> clockDivider;
    public final Observable<ClockDividerMode> clockDivMode;
    public final Observable<Integer> bandSelectDivider;
    public final Observable<BandSelect> bandSelectClockMode;
    public final Observable<LockDetectPin> lockDetectPin;
    public final Observable<Integer> cpCurrent;
    
    public final Observable<List<Integer>> bitState;
    public final Observable<ADF4351Freq> deviceFreq;
    
    
    private ADF4351Proxy device;
    
    public ADF4351Configurator(ADF4351Proxy proxy) {
        device = proxy;
        
        referenceFrequency = new CustomObservable<Double>(100.0E6);
        
        referenceMode = new CustomObservable<ReferenceMode>(ReferenceMode.NORM);
        referenceMode.addObserver((s) -> setReferenceMode(s));
        
        synthMode = new CustomObservable<SynthMode>(SynthMode.INTEGER);
        synthMode.addObserver((s) -> setSynthMode(s));
        
        intValue = new CustomObservable<Integer>(device.getInteger());
        intValue.addObserver((s) -> device.setInteger(s));
        
        fracValue = new CustomObservable<Integer>(device.getFractional());
        fracValue.addObserver((s) -> device.setFractional(s));
        
        modValue = new CustomObservable<Integer>(device.getModulus());
        modValue.addObserver((s) -> device.setModulus(s));
        
        rCounter = new CustomObservable<Integer>(device.getRcounter());
        rCounter.addObserver((s) -> device.setRcounter(s));
        
        prescalerMode = new CustomObservable<PrescalerMode>(device.getPrescaller());
        prescalerMode.addObserver((s) -> device.setPrescaller(s));
        
        feedbackMode = new CustomObservable<FeedbackMode>(device.getFeedbackMode());
        feedbackMode.addObserver((s) -> device.setFeedbackMode(s));
        
        noiseMode = new CustomObservable<NoiseMode>(device.getNoiseMode());
        noiseMode.addObserver((s) -> device.setNoiseMode(s));
        
        outputPower = new CustomObservable<PowerMode>(device.getOutputPower());
        outputPower.addObserver((s) -> device.setOutputPower(s));
        
        outputControl = new CustomObservable<Boolean>(device.getRfOutEnable());
        outputControl.addObserver((s) -> device.setRfOutEnable(s));
        
        auxPower = new CustomObservable<PowerMode>(device.getAuxPower());
        auxPower.addObserver((s) -> device.setAuxPower(s));
        
        auxControl = new CustomObservable<Boolean>(device.getAuxEnable());
        auxControl.addObserver((s) -> device.setAuxEnable(s));
        
        auxMode = new CustomObservable<AuxMode>(device.getAuxMode());
        auxMode.addObserver((s) -> device.setAuxMode(s));
        
        rfDividerMode = new CustomObservable<RfDivider>(device.getRfDivider());
        rfDividerMode.addObserver((s) -> device.setRfDivider(s));
        
        clockDivider = new CustomObservable<Integer>(device.getClockDivider());
        clockDivider.addObserver((s) -> device.setClockDivider(s));
        
        clockDivMode = new CustomObservable<ClockDividerMode>(device.getClockDividerMode());
        clockDivMode.addObserver((s) -> device.setClockDividerMode(s));
        
        bandSelectDivider = new CustomObservable<Integer>(device.getBandSelectDivider());
        bandSelectDivider.addObserver((s) -> device.setBandSelectDivider(s));
        
        bandSelectClockMode = new CustomObservable<BandSelect>(device.getBandSelectClockMode());
        bandSelectClockMode.addObserver((s) -> device.setBandSelectClockMode(s));
        
        lockDetectPin = new CustomObservable<LockDetectPin>(device.getLdPinMode());
        lockDetectPin.addObserver((s) -> device.setLdPinMode(s));
        
        cpCurrent = new CustomObservable<Integer>(device.getCpCurrent());
        cpCurrent.addObserver((s) -> device.setCpCurrent(s));
        
        bitState = new Observable<List<Integer>>();
        
        deviceFreq = new Observable<ADF4351Freq>();
    }
    
    private void onConfigChanged() {
        // TODO: HW config update hook
        bitState.updateValueAndNotify(device.getBitState());
        deviceFreq.setValue(getFreq());
    }
    
    public void setSynthMode(SynthMode mode) {
        switch (mode) {
        case INTEGER:
            device.setLdfMode(LdfMode.INT_N);
            device.setLdpTime(LdpTime.MODE_6NS);
            device.setAbpTime(AbpTime.MODE_6NS);
            device.setChargeCancel(true);
            break;
        case FRACTIONAL:
            device.setLdfMode(LdfMode.FRAC_N);
            device.setLdpTime(LdpTime.MODE_10NS);
            device.setAbpTime(AbpTime.MODE_3NS);
            device.setChargeCancel(false);
        }
    }
    
    public void setReferenceMode(ReferenceMode mode) {
        switch (mode) {
        case DIV2:
            device.setReferenceDivBy2(true);
            device.setReferenceDoubler(false);
            break;
        case NORM:
            device.setReferenceDivBy2(false);
            device.setReferenceDoubler(false);
            break;
        case X2:
            device.setReferenceDivBy2(false);
            device.setReferenceDoubler(true);
        }
    }
   
    public <T> Predicate<T> getValidator(Observable<T> val) {
        if (val == referenceFrequency) {
            return (s) -> (Double)s >= 0.0 && (Double)s <= 250.0E6;
        }
        if (val == intValue) {
            return (s) -> {
                if (prescalerMode.getValue() == PrescalerMode.MODE_4DIV5) {
                    return (Integer)s >= 23 && (Integer)s <= 65535;
                } else {
                    return (Integer)s >= 75 && (Integer)s <= 65535;
                }
            };
        }
        if (val == fracValue) {
            return (s) -> (Integer)s >= 0 && (Integer)s < modValue.getValue();
        }
        if (val == modValue) {
            return (s) -> (Integer)s >= 2 && (Integer)s <= 4095;
        }        
        if (val == rCounter) {
            return (s) -> (Integer)s >= 1 && (Integer)s <= 1023;
        }
        if (val == bandSelectDivider) {
            return (s) -> (Integer)s >= 1 && (Integer)s <= 255;
        }
        throw new IllegalArgumentException("Not a member of the configurator instance");
    }
    
    private ADF4351Freq getFreq() {
        double pllScaleFactor;        
        double vcoFreq;
        double pfdFreq;
        double outFreq;
        double auxFreq;
        double vcoSelFreq;

        if (this.synthMode.getValue() == SynthMode.INTEGER) {
            pllScaleFactor = intValue.getValue();
        } else {
            pllScaleFactor = intValue.getValue() + (fracValue.getValue() / modValue.getValue());
        }
        pfdFreq = getRefScaleFactor() * referenceFrequency.getValue() / rCounter.getValue();
        if (this.feedbackMode.getValue() == FeedbackMode.DIVIDED) {
            vcoFreq = pllScaleFactor * getRfDividerFactor() * pfdFreq;
        } else {
            vcoFreq = pllScaleFactor * pfdFreq;
        }
        if (this.feedbackMode.getValue() == FeedbackMode.DIVIDED) {
            outFreq = pllScaleFactor * pfdFreq;
        } else {
            outFreq = (pllScaleFactor * pfdFreq) / getRfDividerFactor();
        }
        if (auxMode.getValue() == AuxMode.FUNDAMENTAL) {
            auxFreq = vcoFreq;
        } else {
            auxFreq = outFreq;
        }
        vcoSelFreq = pfdFreq / bandSelectDivider.getValue();
        return new ADF4351Freq(
                vcoFreq,
                pfdFreq,
                outFreq,
                auxFreq,
                vcoSelFreq);
    }
    
    private double getRefScaleFactor() {
        switch (referenceMode.getValue()) {
        case DIV2:
            return 0.5;
        case NORM:
            return 1.0;
        case X2:
            return 2.0;
        }
        throw new IllegalArgumentException("Illegal internal state");
    }
    
    private double getRfDividerFactor() {
        switch (rfDividerMode.getValue()) {
        case DIV_1:
            return 1.0;
        case DIV_2:
            return 2.0;
        case DIV_4:
            return 4.0;
        case DIV_8:
            return 8.0;
        case DIV_16:
            return 16.0;
        case DIV_32:
            return 32.0;
        case DIV_64:
            return 64.0;
        }
        throw new IllegalArgumentException("Illegal internal state");
    }
}
