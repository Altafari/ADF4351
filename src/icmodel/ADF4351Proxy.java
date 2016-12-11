package icmodel;

public class ADF4351Proxy {
    // ADF4351 internal state variables
    // Register 0
    private int integerVal;
    private int fractionalVal;
    // Register 1
    private int modulusVal;
    private int phaseVal;
    private PrescallerMode prescallerMode;
    private boolean phaseAdjust;
    // Register 2
    private boolean counterReset;
    private boolean cpThreeState;
    private boolean powerDown;
    private PdPolarity pdPolarity;
    private LdpTime ldpTime;
    private LdfMode ldfMode;
    private int cpCurrent;
    private boolean doubleBuffer;
    private boolean refDivBy2;
    private boolean refDoubler;
    private MuxOutMode muxOut;
    private NoiseMode noiseMode;
    // Register 3
    private int clockDivider;
    private ClockDividerMode clockDividerMode;
    private boolean cycleSlipReduction;
    private boolean chargeCancel;
    private AbpTime abpTime;
    private boolean bandSelectClockMode;
    // Register 4
    private OutputPower outputPower;
    private boolean rfOutEnable;
    private OutputPower auxPower;
    private boolean auxEnable;
    private AuxMode auxMode;
    private boolean muteTillLd;
    private boolean vcoPowerDown;
    private int bandSelectDivider;
    private RfDivider rfDivider;
    private FeedbackMode feedbackMode;
    // Register 5
    private LockDetectPin lockDetectPin;
    
    public final static int MIN_FRACTIONAL = 0;
    public final static int MAX_FRACTIONAL = 4095;
    
    public final static int MIN_INTEGER = 23;
    public final static int MAX_INTEGER = 65535;
    
    public final static int MIN_MODULUS = 2;
    public final static int MAX_MODULUS = 4095;
    
    public final static int MIN_PHASE = 0;
    public final static int MAX_PHASE = 4095;
    public final static int DEFAULT_PHASE = 1;
    
    public enum PrescallerMode { MODE_4DIV5, MODE_8DIV9 }
    public enum PdPolarity { NEGATIVE, POSITIVE }
    public enum LdpTime { MODE_10NS, MODE_6NS }
    public enum LdfMode { FRAC_N, INT_N }
    
    public final static int MIN_CP_CURRENT = 0;
    public final static int MAX_CP_CURRENT = 15;
    
    public enum MuxOutMode { THREE_STATE, DVDD, DGND, R_COUNTER, N_DIVIDER, ANALOG_LOCK, DIGITAL_LOCK }
    public enum NoiseMode { LOW_NOISE, LOW_SPUR }
    
    public final static int MIN_CLOCK_DIVIDER = 0;
    public final static int MAX_CLOCK_DIVIDER = 4095;
    
    public enum ClockDividerMode { CLOCK_DIVIDER_OFF, FAST_LOCK_ENABLE, RESYNC_ENABLE }
    public enum AbpTime { MODE_6NS, MODE_3NS }
    
    public enum OutputPower { MODE_MINUS_4DBM, MODE_MINUS_1DBM, MODE_PLUS_2DBM, MODE_PLUS_5DBM }
    public enum AuxMode { DIVIDED_OUTPUT, FUNDAMENTAL }
    public enum RfDivider { DIV_1, DIV_2, DIV_4, DIV_8, DIV_16, DIV32, DIV64 }
    public enum FeedbackMode { DIVIDED, FUNDAMENTAL }
    
    public enum LockDetectPin { LOW, DIGITAL_LD, HIGH }
    
    public ADF4351Proxy() {
        phaseVal = DEFAULT_PHASE;
    }
    
    public void setFractional(int val) {        
        if (val < MIN_FRACTIONAL || val > MAX_FRACTIONAL) {
            throw new IllegalArgumentException("Fractional value out of range");
        }
        fractionalVal = val;
    }

    public void setInteger(int val) {
        if (val < MIN_INTEGER || val > MAX_INTEGER) {            
            throw new IllegalArgumentException("Integer value out of range");
        }
        integerVal = val;
    }

    public void setModulus(int val) {
        if (val < MIN_MODULUS || val > MAX_MODULUS) {
            throw new IllegalArgumentException("Modulus value out of range");
        }
        modulusVal = val;
    }
    
    public void setPhase(int val) {
        if (val < MIN_PHASE || val > MAX_PHASE) {
            throw new IllegalArgumentException("Phase value out of range");
        }
        phaseVal = val;
    }
    
    public void setPrescaller(PrescallerMode val) {
        prescallerMode = val;
    }
    
    public void setPhaseAdjust(boolean val) {
        phaseAdjust = val;
    }
    
    public void setCounterReset(boolean val) {
        counterReset = val;
    }
    
    public void setCpThreeState(boolean val) {
        cpThreeState = val;
    }
    
    public void setPowerDown(boolean val) {
        powerDown = val;
    }
    
    public void setPdPolarity(PdPolarity val) {
        pdPolarity = val;
    }
    
    public void setLdpTime(LdpTime val) {
        ldpTime = val;
    }
    
    public void setLdfMode(LdfMode val) {
        ldfMode = val;
    }
    
    public void setCpCurrent(int val) {
        if (val < MIN_CP_CURRENT || val > MAX_CP_CURRENT) {
            throw new IllegalArgumentException("Current pump setting value out of range");
        }
        cpCurrent = val;
    }
    
    public void setDoubleBuffer(boolean val) {
        doubleBuffer = val;
    }
    
    // R - counter??? TODO: investigate issue
    
    public void setReferenceDivBy2(boolean val) {
        refDivBy2 = val;
    }
    
    public void setReferenceDoubler(boolean val) {
        refDoubler = val;
    }
    
    public void setMuxOutMode(MuxOutMode val) {
        muxOut = val;
    }
    
    public void setNoiseMode(NoiseMode val) {
        noiseMode = val;
    }
    
    public void setClockDivider(int val) {
        if (val < MIN_CLOCK_DIVIDER || val > MAX_CLOCK_DIVIDER) {
            throw new IllegalArgumentException("Clock divider out of range");
        }
        clockDivider = val;
    }
    
    public void setClockDividerMode(ClockDividerMode val) {
        clockDividerMode = val;
    }
    
    public void setAbpTime(AbpTime val) {
        abpTime = val;
    }
    
    public void setBandSelectClockMode(boolean val) {
        bandSelectClockMode = val;
    }
}
