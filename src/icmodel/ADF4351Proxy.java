package icmodel;

public class ADF4351Proxy {
    
    private boolean invalidRegs[];

    private int integerVal;
    private int fractionalVal;
    private int modulusVal;
    private int phaseVal;
    private PrescallerMode prescallerMode;
    private boolean phaseAdjust;
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
    
    public final static int MIN_FRACTIONAL = 0;
    public final static int MAX_FRACTIONAL = 4095;
    
    public final static int MIN_INTEGER = 23;
    public final static int MAX_INTEGER = 65535;
    
    public final static int MIN_MODULUS = 2;
    public final static int MAX_MODULUS = 4095;
    
    public final static int MIN_PHASE = 0;
    public final static int MAX_PHASE = 4095;
    public final static int DEFAULT_PHASE = 1;
    
    public enum PrescallerMode { MODE4DIV5, MODE8DIV9 };
    public enum PdPolarity { NEGATIVE, POSITIVE };
    public enum LdpTime { MODE10NS, MODE6NS };
    public enum LdfMode { FRAC_N, INT_N };
    
    public final static int MIN_CP_CURRENT = 0;
    public final static int MAX_CP_CURRENT = 15;
    
    public enum MuxOutMode { THREE_STATE, DVDD, DGND, R_COUNTER, N_DIVIDER, ANALOG_LOCK, DIGITAL_LOCK };
    public enum NoiseMode {LOW_NOISE, LOW_SPUR};    
    
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
}
