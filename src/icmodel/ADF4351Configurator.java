package icmodel;

import icmodel.ADF4351Proxy.*;

public class ADF4351Configurator {
    
    public enum ReferenceMode {
        DIV2, NORM, X2
    }
    
    public enum SynthMode {
        INTEGER, FRACTIONAL
    }
    
    private ADF4351Proxy device;
    private ReferenceMode refMode;  // Replace with observable?
    private SynthMode synthMode;

    public void setReferenceMode(ReferenceMode refMode, int rCounter) {
        
    }
    
    public void setSynthMode(SynthMode mode) {
        switch (mode) {
        case INTEGER:
            device.setLdfMode(LdfMode.INT_N);
            device.setLdpTime(LdpTime.MODE_6NS);
            device.setAbpTime(AbpTime.MODE_6NS);
            device.setChargeCancel(true);
            device.setNoiseMode(NoiseMode.LOW_NOISE);
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
    
    public ADF4351Configurator(ADF4351Proxy proxy) {
        device = proxy;
        setDefaultValues();
        setReferenceMode(ReferenceMode.NORM);
        setSynthMode(SynthMode.INTEGER);

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
        device.setMuxOutMode(MuxOutMode.DIGITAL_LOCK);
        device.setOutputPower(PowerMode.MODE_PLUS_2DBM);
        device.setRfOutEnable(true);
        device.setAuxPower(PowerMode.MODE_PLUS_2DBM);
        device.setAuxEnable(true);
        device.setAuxMode(AuxMode.DIVIDED_OUTPUT);
        device.setMuteTillLd(false);
        device.setRfDivider(RfDivider.DIV64);
    }

}
