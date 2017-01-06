package adfctrl.icmodel;

import java.util.ArrayList;
import java.util.List;

public class ADF4351Proxy {
    // ADF4351 internal state variables
    // Register 0
    private int integerVal;
    private int fractionalVal;
    // Register 1
    private int modulusVal;
    private int phaseVal;
    private PrescallerMode prescallerMode = PrescallerMode.MODE_4DIV5;
    private boolean phaseAdjust;
    // Register 2
    private boolean counterReset;
    private boolean cpThreeState;
    private boolean powerDown;
    private PdPolarity pdPolarity = PdPolarity.NEGATIVE;
    private LdpTime ldpTime = LdpTime.MODE_10NS;
    private LdfMode ldfMode = LdfMode.FRAC_N;
    private int cpCurrent;
    private boolean doubleBuffer;
    private int rCounter;
    private boolean refDivBy2;
    private boolean refDoubler;
    private MuxOutMode muxOut = MuxOutMode.ANALOG_LOCK;
    private NoiseMode noiseMode = NoiseMode.LOW_NOISE;
    // Register 3
    private int clockDivider;
    private ClockDividerMode clockDividerMode = ClockDividerMode.CLOCK_DIVIDER_OFF;
    private boolean cycleSlipReduction;
    private boolean chargeCancel;
    private AbpTime abpTime = AbpTime.MODE_3NS;
    private BandSelect bandSelectClockMode = BandSelect.HIGH;
    // Register 4
    private PowerMode outputPower = PowerMode.MODE_PLUS_2DBM;
    private boolean rfOutEnable;
    private PowerMode auxPower = PowerMode.MODE_PLUS_2DBM;
    private boolean auxEnable;
    private AuxMode auxMode = AuxMode.DIVIDED_OUTPUT;
    private boolean muteTillLd;
    private boolean vcoPowerDown;
    private int bandSelectDivider;
    private RfDivider rfDivider = RfDivider.DIV_1;
    private FeedbackMode feedbackMode = FeedbackMode.DIVIDED;
    // Register 5
    private LockDetectPin lockDetectPin = LockDetectPin.DIGITAL_LD;

    private static class BitArray {
        public final int bitMask;
        public final int offset;
        
        public BitArray(int nBits, int offset) {
            this.offset = offset;
            int temp = 0;
            for (int i = 0; i < nBits; i++) {
                temp |= 1 << (i + offset);
            }
            bitMask = temp;
        }
    }
    
    private static class BitBuffer {
        private int buff;
        
        public BitBuffer() {
            // Empty
        }
        
        public void init(int val) {
            buff = val;
        }
        
        public void putArgument(int arg, BitArray bar) {
            buff = (buff & ~bar.bitMask) | (arg << bar.offset & bar.bitMask);
        }
        
        public void putBit(boolean arg, int pos) {
            buff = (buff & ~(1 << pos)) | ((arg? 1 : 0) << pos);
        }
        
        public int getInt() {
            return buff;
        }
    }
    
    // Register 0
    public final static int MIN_FRACTIONAL = 0;
    public final static int MAX_FRACTIONAL = 4095;
    
    private final static BitArray FRACTIONAL_VAL_BITS = new BitArray(12, 3);
    
    public final static int MIN_INTEGER = 23;
    public final static int MAX_INTEGER = 65535;
    
    private final static BitArray INTEGER_VAL_BITS = new BitArray(16, 15);
    // Register 1
    public final static int MIN_MODULUS = 2;
    public final static int MAX_MODULUS = 4095;
    
    private final static BitArray MODULUS_VAL_BITS = new BitArray(12, 3);
    
    public final static int MIN_PHASE = 0;
    public final static int MAX_PHASE = 4095;
    
    private final static BitArray PHASE_VAL_BITS = new BitArray(12, 15);
    
    public final static int DEFAULT_PHASE = 1;

    public enum PrescallerMode {
        MODE_4DIV5, MODE_8DIV9
    }

    // Register 2
    public enum PdPolarity {
        NEGATIVE, POSITIVE
    }

    public enum LdpTime {
        MODE_10NS, MODE_6NS
    }

    public enum LdfMode {
        FRAC_N, INT_N
    }

    public final static int MIN_CP_CURRENT = 0;
    public final static int MAX_CP_CURRENT = 15;
    
    private final static BitArray CP_CURRENT_BITS = new BitArray(4, 9);
    
    public final static int MIN_R_COUNTER = 0;
    public final static int MAX_R_COUNTER = 4095;
    
    private final static BitArray R_COUNTER_BITS = new BitArray(10, 14);

    public enum MuxOutMode {
        THREE_STATE, DVDD, DGND, R_COUNTER, N_DIVIDER, ANALOG_LOCK, DIGITAL_LOCK
    }

    private final static BitArray MUX_OUT_BITS = new BitArray(3, 26);
    
    public enum NoiseMode {
        LOW_NOISE, RESERVED1, RESERVED2, LOW_SPUR;
    }
    
    private final static BitArray NOISE_MODE_BITS = new BitArray(2, 29);

    // Register 3
    public final static int MIN_CLOCK_DIVIDER = 0;
    public final static int MAX_CLOCK_DIVIDER = 4095;

    private final static BitArray CLOCK_DIVIDER_BITS = new BitArray(12, 3);
    
    public enum ClockDividerMode {
        CLOCK_DIVIDER_OFF, FAST_LOCK_ENABLE, RESYNC_ENABLE
    }

    public enum AbpTime {
        MODE_6NS, MODE_3NS
    }

    public enum BandSelect {
        LOW, HIGH
    }

    // Register 4
    public enum PowerMode {
        MODE_MINUS_4DBM, MODE_MINUS_1DBM, MODE_PLUS_2DBM, MODE_PLUS_5DBM
    }
    
    private final static BitArray OUTPUT_POWER_BITS = new BitArray(2, 3);
    private final static BitArray AUX_POWER_BITS = new BitArray(2, 6);

    public enum AuxMode {
        DIVIDED_OUTPUT, FUNDAMENTAL
    }

    public final static int MIN_BAND_SELECT_DIVIDER = 1;
    public final static int MAX_BAND_SELECT_DIVIDER = 255;

    private final static BitArray BAND_SELECT_BITS = new BitArray(8, 12);
    
    public enum RfDivider {
        DIV_1, DIV_2, DIV_4, DIV_8, DIV_16, DIV_32, DIV_64
    }
    
    private final static BitArray RF_DIVIDER_BITS = new BitArray(3, 20);

    public enum FeedbackMode {
        DIVIDED, FUNDAMENTAL
    }

    // Register 5
    
    private final static int reserved = 3;
    private final static BitArray RESERVED_11 = new BitArray(2, 19); 
    
    public enum LockDetectPin {
        LOW, DIGITAL_LD, LOW2, HIGH
    }

    private final static BitArray LOCK_DETECT_BITS = new BitArray(2, 22);
    
    public ADF4351Proxy() {
        // Empty
    }

    // Register 0
    public void setFractional(int val) {
        if (val < MIN_FRACTIONAL || val > MAX_FRACTIONAL) {
            throw new IllegalArgumentException("Fractional value out of range");
        }
        fractionalVal = val;
    }

    public int getFractional() {
        return fractionalVal;
    }

    public void setInteger(int val) {
        if (val < MIN_INTEGER || val > MAX_INTEGER) {
            throw new IllegalArgumentException("Integer value out of range");
        }
        integerVal = val;
    }

    public int getInteger() {
        return integerVal;
    }

    // Register 1
    public void setModulus(int val) {
        if (val < MIN_MODULUS || val > MAX_MODULUS) {
            throw new IllegalArgumentException("Modulus value out of range");
        }
        modulusVal = val;
    }

    public int getModulus() {
        return modulusVal;
    }

    public void setPhase(int val) {
        if (val < MIN_PHASE || val > MAX_PHASE) {
            throw new IllegalArgumentException("Phase value out of range");
        }
        phaseVal = val;
    }

    public int getPhase() {
        return phaseVal;
    }

    public void setPrescaller(PrescallerMode val) {
        prescallerMode = val;
    }

    public PrescallerMode getPrescaller() {
        return prescallerMode;
    }

    public void setPhaseAdjust(boolean val) {
        phaseAdjust = val;
    }

    public boolean getPhaseAdjust() {
        return phaseAdjust;
    }

    // Register 2
    public void setCounterReset(boolean val) {
        counterReset = val;
    }

    public boolean getCounterReset() {
        return counterReset;
    }

    public void setCpThreeState(boolean val) {
        cpThreeState = val;
    }

    public boolean getCpThreeState() {
        return cpThreeState;
    }

    public void setPowerDown(boolean val) {
        powerDown = val;
    }

    public boolean getPowerDown() {
        return powerDown;
    }

    public void setPdPolarity(PdPolarity val) {
        pdPolarity = val;
    }

    public PdPolarity getPdPolarity() {
        return pdPolarity;
    }

    public void setLdpTime(LdpTime val) {
        ldpTime = val;
    }

    public LdpTime getLdpTime() {
        return ldpTime;
    }

    public void setLdfMode(LdfMode val) {
        ldfMode = val;
    }

    public LdfMode getLdfMode() {
        return ldfMode;
    }

    public void setCpCurrent(int val) {
        if (val < MIN_CP_CURRENT || val > MAX_CP_CURRENT) {
            throw new IllegalArgumentException("Current pump setting value out of range");
        }
        cpCurrent = val;
    }

    public int getCpCurrent() {
        return cpCurrent;
    }

    public void setDoubleBuffer(boolean val) {
        doubleBuffer = val;
    }

    public boolean getDoubleBuffer() {
        return doubleBuffer;
    }
    
    public void setRcounter(int val) {
        if (val < MIN_R_COUNTER || val > MAX_R_COUNTER) {
            throw new IllegalArgumentException("R counter value out of range");
        }
        rCounter = val;
    }
    
    public int getRcounter() {
        return rCounter;
    }

    public void setReferenceDivBy2(boolean val) {
        refDivBy2 = val;
    }

    public boolean getReferenceDivBy2() {
        return refDivBy2;
    }

    public void setReferenceDoubler(boolean val) {
        refDoubler = val;
    }

    public boolean getReferenceDoubler() {
        return refDoubler;
    }

    public void setMuxOutMode(MuxOutMode val) {
        muxOut = val;
    }

    public MuxOutMode getMuxOutMode() {
        return muxOut;
    }

    public void setNoiseMode(NoiseMode val) {
        noiseMode = val;
    }

    public NoiseMode getNoiseMode() {
        return noiseMode;
    }

    // Register 3
    public void setClockDivider(int val) {
        if (val < MIN_CLOCK_DIVIDER || val > MAX_CLOCK_DIVIDER) {
            throw new IllegalArgumentException("Clock divider out of range");
        }
        clockDivider = val;
    }

    public int getClockDivider() {
        return clockDivider;
    }

    public void setClockDividerMode(ClockDividerMode val) {
        clockDividerMode = val;
    }

    public ClockDividerMode getClockDividerMode() {
        return clockDividerMode;
    }

    public void setCycleSlipReduction(boolean val) {
        cycleSlipReduction = val;
    }

    public boolean getCycleSlipReduction() {
        return cycleSlipReduction;
    }

    public void setChargeCancel(boolean val) {
        chargeCancel = val;
    }

    public boolean getChargeCancel() {
        return chargeCancel;
    }

    public void setAbpTime(AbpTime val) {
        abpTime = val;
    }

    public AbpTime getAbpTime() {
        return abpTime;
    }

    public void setBandSelectClockMode(BandSelect val) {
        bandSelectClockMode = val;
    }

    public BandSelect getBandSelectClockMode() {
        return bandSelectClockMode;
    }

    // Register 4
    public void setOutputPower(PowerMode val) {
        outputPower = val;
    }

    public PowerMode getOutputPower() {
        return outputPower;
    }

    public void setRfOutEnable(boolean val) {
        rfOutEnable = val;
    }

    public boolean getRfOutEnable() {
        return rfOutEnable;
    }

    public void setAuxPower(PowerMode val) {
        auxPower = val;
    }

    public PowerMode getAuxPower() {
        return auxPower;
    }

    public void setAuxEnable(boolean val) {
        auxEnable = val;
    }

    public boolean getAuxEnable() {
        return auxEnable;
    }

    public void setAuxMode(AuxMode val) {
        auxMode = val;
    }

    public AuxMode getAuxMode() {
        return auxMode;
    }

    public void setMuteTillLd(boolean val) {
        muteTillLd = val;
    }

    public boolean getMuteTillLd() {
        return muteTillLd;
    }

    public void setVcoPowerDown(boolean val) {
        vcoPowerDown = val;
    }

    public boolean getVcoPowerDown() {
        return vcoPowerDown;
    }

    public void setBandSelectDivider(int val) {
        if (val < MIN_BAND_SELECT_DIVIDER || val > MAX_BAND_SELECT_DIVIDER) {
            throw new IllegalArgumentException("Band select clock divider out of range");
        }
        bandSelectDivider = val;
    }

    public int getBandSelectDivider() {
        return bandSelectDivider;
    }

    public void setRfDivider(RfDivider val) {
        rfDivider = val;
    }

    public RfDivider getRfDivider() {
        return rfDivider;
    }

    public void setFeedbackMode(FeedbackMode val) {
        feedbackMode = val;
    }

    public FeedbackMode getFeedbackMode() {
        return feedbackMode;
    }

    // Register 5
    public void setLdPinMode(LockDetectPin val) {
        lockDetectPin = val;
    }

    public LockDetectPin getLdPinMode() {
        return lockDetectPin;
    }

    public List<Integer> getBitState() {
        ArrayList<Integer> res = new ArrayList<Integer>(5);
        for (int i = 0; i < 6; i++) {
            res.add(getRegister(i));
        }
        return res;
    }
    
    public int getRegister(int reg) {
        BitBuffer buff = new BitBuffer();
        switch (reg) {
        case 0:
            buff.init(0);
            buff.putArgument(integerVal, INTEGER_VAL_BITS);
            buff.putArgument(fractionalVal, FRACTIONAL_VAL_BITS);
            break;
        case 1: 
            buff.init(1);
            buff.putArgument(this.modulusVal, MODULUS_VAL_BITS);
            buff.putArgument(this.phaseVal, PHASE_VAL_BITS);
            buff.putBit(this.prescallerMode == PrescallerMode.MODE_8DIV9, 27);
            buff.putBit(this.phaseAdjust, 28);
            break;
        case 2:
            buff.init(2);
            buff.putBit(this.counterReset, 3);
            buff.putBit(this.cpThreeState, 4);
            buff.putBit(this.powerDown, 5);
            buff.putBit(this.pdPolarity == PdPolarity.POSITIVE, 6);
            buff.putBit(this.ldpTime == LdpTime.MODE_6NS, 7);
            buff.putBit(this.ldfMode == LdfMode.INT_N, 8);
            buff.putArgument(this.cpCurrent, CP_CURRENT_BITS);
            buff.putBit(this.doubleBuffer, 13);
            buff.putArgument(this.rCounter, R_COUNTER_BITS);
            buff.putBit(this.refDivBy2, 24);
            buff.putBit(this.refDoubler, 24);
            buff.putArgument(this.muxOut.ordinal(), MUX_OUT_BITS);
            buff.putArgument(this.noiseMode.ordinal(), NOISE_MODE_BITS);
            break;
        case 3:
            buff.init(3);
            buff.putArgument(this.clockDivider, CLOCK_DIVIDER_BITS);
            buff.putArgument(this.clockDividerMode.ordinal(), CLOCK_DIVIDER_BITS);
            buff.putBit(this.cycleSlipReduction, 18);
            buff.putBit(this.chargeCancel, 21);
            buff.putBit(this.abpTime == AbpTime.MODE_3NS, 22);
            buff.putBit(this.bandSelectClockMode == BandSelect.HIGH, 23);
            break;
        case 4:
            buff.init(4);
            buff.putArgument(this.outputPower.ordinal(), OUTPUT_POWER_BITS);
            buff.putBit(this.rfOutEnable, 5);
            buff.putArgument(this.auxPower.ordinal(), AUX_POWER_BITS);
            buff.putBit(this.auxEnable, 8);
            buff.putBit(this.auxMode == AuxMode.FUNDAMENTAL, 9);
            buff.putBit(this.muteTillLd, 10);
            buff.putBit(this.vcoPowerDown, 11);
            buff.putArgument(this.bandSelectDivider, BAND_SELECT_BITS);
            buff.putArgument(this.rfDivider.ordinal(), RF_DIVIDER_BITS);
            buff.putBit(this.feedbackMode == FeedbackMode.FUNDAMENTAL, 23);
            break;
        case 5:
            buff.init(5);
            buff.putArgument(this.lockDetectPin.ordinal(), LOCK_DETECT_BITS);
            buff.putArgument(reserved, RESERVED_11);
            break;
        default:
            throw new IllegalArgumentException("Unknown register number: " + reg);
        }
        return buff.getInt();
    }
}
