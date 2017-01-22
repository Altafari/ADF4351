package adfctrl.icmodel;

import adfctrl.icmodel.ADF4351Configurator.ReferenceMode;
import adfctrl.icmodel.ADF4351Configurator.SynthMode;
import adfctrl.icmodel.ADF4351Proxy.AuxMode;
import adfctrl.icmodel.ADF4351Proxy.FeedbackMode;
import adfctrl.icmodel.ADF4351Proxy.RfDivider;
import adfctrl.utils.BinaryCombiner;
import adfctrl.utils.FunctionMapper;
import adfctrl.utils.IObservable;
import adfctrl.utils.SwitchCombiner;

public class ADF4351Freq {    
    public final IObservable<Double> vcoFreq;
    public final IObservable<Double> pfdFreq;
    public final IObservable<Double> outFreq;
    public final IObservable<Double> auxFreq;
    public final IObservable<Double> vcoBandSelFreq;
    
    ADF4351Freq(ADF4351Configurator config) {
     /*   vcoFreq = new Observable<Double>();
        pfdFreq = new Observable<Double>();
        outFreq = new Observable<Double>();
        auxFreq = new Observable<Double>();
        vcoBandSelFreq = new Observable<Double>();*/
        
        FunctionMapper<SynthMode, Boolean> isIntegerMode = 
                new FunctionMapper<SynthMode, Boolean>(config.synthMode, (s) -> s == SynthMode.INTEGER);

        BinaryCombiner<Integer, Integer, Double> fractionN = new BinaryCombiner<Integer, Integer, Double>(
                config.fracValue, config.modValue, (x, y) -> (double)x / y);
        
        FunctionMapper<Integer, Double> integerN = 
                new FunctionMapper<Integer, Double>(config.intValue, (s) -> (double)s);
        
        BinaryCombiner<Double, Double, Double> fractionSum = 
                new BinaryCombiner<Double, Double, Double>(integerN, fractionN, (x, y) -> x + y); 
        
        SwitchCombiner<Double> pllScaleFactor = 
                new SwitchCombiner<Double>(isIntegerMode, integerN, fractionSum);
        
        BinaryCombiner<ReferenceMode, Double, Double> scaledRef = new BinaryCombiner<ReferenceMode, Double, Double>(
                config.referenceMode, config.referenceFrequency, (x, y) -> getScaledRefFreq(x, y));
        
        BinaryCombiner<Double, Integer, Double> pfdFreq =
                new BinaryCombiner<Double, Integer, Double>(scaledRef, config.rCounter, (x, y) -> x / y);
        
        FunctionMapper<FeedbackMode, Boolean> isFeedbackDivided =
                new FunctionMapper<FeedbackMode, Boolean>(config.feedbackMode, (s) -> s == FeedbackMode.DIVIDED);
        
        FunctionMapper<RfDivider, Double> rfDivider =
                new FunctionMapper<RfDivider, Double>(config.rfDividerMode, (s) -> getRfDividerFactor(s));
        
        BinaryCombiner<Double, Double, Double> vcoFreqFundamentalFeedback =
                new BinaryCombiner<Double, Double, Double>(pllScaleFactor, pfdFreq, (x, y) -> x * y);
        
        BinaryCombiner<Double, Double, Double> vcoFreqDividedFeedback =
                new BinaryCombiner<Double, Double, Double>(vcoFreqFundamentalFeedback, rfDivider, (x, y) -> x * y);
        
        SwitchCombiner<Double> vcoFreq = new SwitchCombiner<Double>(
                isFeedbackDivided, vcoFreqDividedFeedback, vcoFreqFundamentalFeedback);
        
        BinaryCombiner<Double, Double, Double> outFreq =
                new BinaryCombiner<Double, Double, Double>(vcoFreq, rfDivider, (x, y) -> x / y);
        
        FunctionMapper<AuxMode, Boolean> isAuxDivided =
                new FunctionMapper<AuxMode, Boolean>(config.auxMode, (s) -> s == AuxMode.DIVIDED_OUTPUT);
        
        SwitchCombiner<Double> auxFreq = new SwitchCombiner<Double>(isAuxDivided, outFreq, vcoFreq);
        
        BinaryCombiner<Double, Integer, Double> vcoBandSelFreq =
                new BinaryCombiner<Double, Integer, Double>(pfdFreq, config.bandSelectDivider, (x, y) -> x / y);
        
        this.vcoFreq = vcoFreq;
        this.pfdFreq = pfdFreq;
        this.outFreq = outFreq;
        this.auxFreq = auxFreq;
        this.vcoBandSelFreq = vcoBandSelFreq;
    }
    
    private static double getScaledRefFreq(ReferenceMode refMod, Double refFreq) {
        switch (refMod) {
        case DIV2:
            return 0.5 * refFreq;
        case NORM:
            return 1.0 * refFreq;
        case X2:
            return 2.0 * refFreq;
        default:
            throw new IllegalArgumentException("Illegal internal state");
        }
    }
    
    private static double getRfDividerFactor(RfDivider divider) {
        switch (divider) {
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
