package adfctrl.icmodel;

public class ADF4351Freq {
    public final double vcoFreq;
    public final double pfdFreq;
    public final double outFreq;
    public final double auxFreq;
    public final double vcoSelFreq;
    
    ADF4351Freq(double vcoFreq, double pfdFreq,
            double outFreq, double auxFreq, double vcoSelFreq) {
        this.vcoFreq = vcoFreq;
        this.pfdFreq = pfdFreq;
        this.outFreq = outFreq;
        this.auxFreq = auxFreq;
        this.vcoSelFreq = vcoSelFreq;
    }
}
