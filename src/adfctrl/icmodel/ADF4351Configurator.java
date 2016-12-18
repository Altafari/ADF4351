package adfctrl.icmodel;

import adfctrl.icmodel.ADF4351Proxy.*;
import adfctrl.utils.Observable;

public class ADF4351Configurator {
    
    public enum ReferenceMode {
        DIV2, NORM, X2
    }
    
    public enum SynthMode {
        INTEGER, FRACTIONAL
    }

    public final Observable<Double> referenceFrequency;
    public final Observable<ReferenceMode> referenceMode;
    public final Observable<SynthMode> synthMode;
    public final Observable<Integer> intValue;
    public final Observable<Integer> fracValue;
    public final Observable<Integer> modValue;
    public final Observable<FeedbackMode> feedbackMode;
    public final Observable<NoiseMode> noiseMode;
    public final Observable<PowerMode> outputPower;
    public final Observable<Boolean> outputControl;
    public final Observable<PowerMode> auxPower;
    public final Observable<Boolean> auxControl;
    public final Observable<AuxMode> auxMode;
    public final Observable<RfDivider> rfDividerMode;
    
    
    private ADF4351Proxy device;
    
    public ADF4351Configurator(ADF4351Proxy proxy) {
        device = proxy;
        referenceFrequency = new Observable<Double>(100.0E6);
        
        referenceMode = new Observable<ReferenceMode>(ReferenceMode.NORM);
        referenceMode.addObserver((s) -> {setReferenceMode(s); onConfigChanged();});
        
        synthMode = new Observable<SynthMode>(SynthMode.INTEGER);
        synthMode.addObserver((s) -> {setSynthMode(s); onConfigChanged();});
        
        intValue = new Observable<Integer>();
        intValue.addObserver((s) -> {device.setInteger(s); onConfigChanged();});
        
        fracValue = new Observable<Integer>();
        fracValue.addObserver((s) -> {device.setFractional(s); onConfigChanged();});
        
        modValue = new Observable<Integer>();
        modValue.addObserver((s) -> {device.setModulus(s); onConfigChanged();});
        
        feedbackMode = new Observable<FeedbackMode>();
        feedbackMode.addObserver((s) -> {device.setFeedbackMode(s); onConfigChanged();});
        
        noiseMode = new Observable<NoiseMode>();
        noiseMode.addObserver((s) -> {device.setNoiseMode(s); onConfigChanged();});
        
        outputPower = new Observable<PowerMode>();
        outputPower.addObserver((s) -> {device.setOutputPower(s); onConfigChanged();});
        
        outputControl = new Observable<Boolean>();
        outputControl.addObserver((s) -> {device.setRfOutEnable(s); onConfigChanged();});
        
        auxPower = new Observable<PowerMode>();
        auxPower.addObserver((s) -> {device.setAuxPower(s); onConfigChanged();});
        
        auxControl = new Observable<Boolean>();
        auxControl.addObserver((s) -> {device.setAuxEnable(s); onConfigChanged();});
        
        auxMode = new Observable<AuxMode>();
        auxMode.addObserver((s) -> {device.setAuxMode(s); onConfigChanged();});
        
        rfDividerMode = new Observable<RfDivider>();
        rfDividerMode.addObserver((s) -> {device.setRfDivider(s); onConfigChanged();});
        
        setDefaultValues();
        setReferenceMode(ReferenceMode.NORM);
        setSynthMode(SynthMode.INTEGER);
        onConfigChanged();
    }
    
    private void onConfigChanged() {
        // TODO: HW config update hook
    }
    
    public void setSynthMode(SynthMode mode) {
        switch (mode) {
        case INTEGER:
            device.setLdfMode(LdfMode.INT_N);
            device.setLdpTime(LdpTime.MODE_6NS);
            device.setAbpTime(AbpTime.MODE_6NS);
            device.setChargeCancel(true);
            device.setNoiseMode(NoiseMode.LOW_NOISE);// TODO: remove
            break;
        case FRACTIONAL:
            device.setLdfMode(LdfMode.FRAC_N);
            device.setLdpTime(LdpTime.MODE_10NS);
            device.setAbpTime(AbpTime.MODE_3NS);
            device.setChargeCancel(false);
            device.setNoiseMode(NoiseMode.LOW_SPUR);
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
    
    private void setDefaultValues() {
        device.setVcoPowerDown(false);
        device.setPowerDown(false);
        device.setBandSelectClockMode(BandSelect.LOW);
        device.setCpThreeState(false);
        device.setCounterReset(false);
        device.setFeedbackMode(FeedbackMode.DIVIDED);
        device.setPhase(1);
        device.setPhaseAdjust(false);
        device.setPdPolarity(PdPolarity.POSITIVE);
        device.setClockDividerMode(ClockDividerMode.CLOCK_DIVIDER_OFF);
        device.setCycleSlipReduction(false);
        device.setDoubleBuffer(false);
        device.setMuxOutMode(MuxOutMode.ANALOG_LOCK);
        device.setOutputPower(PowerMode.MODE_PLUS_2DBM);
        device.setRfOutEnable(true);
        device.setAuxPower(PowerMode.MODE_PLUS_2DBM);
        device.setAuxEnable(true);
        device.setAuxMode(AuxMode.DIVIDED_OUTPUT);
        device.setMuteTillLd(false);
        device.setRfDivider(RfDivider.DIV_64);
        device.setLdPinMode(LockDetectPin.DIGITAL_LD);
    }
}
