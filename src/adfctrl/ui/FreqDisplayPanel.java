package adfctrl.ui;

import javax.swing.BoxLayout;

import adfctrl.icmodel.ADF4351Freq;
import adfctrl.ui.sevenseg.LabeledSevenSegment;

public class FreqDisplayPanel extends BorderedModelView<ADF4351Freq> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private LabeledSevenSegment vcoFreq;
    private LabeledSevenSegment pfdFreq;
    private LabeledSevenSegment outFreq;
    private LabeledSevenSegment auxFreq;
    private LabeledSevenSegment vcoSelFreq;
    
    public FreqDisplayPanel() {
        super("Device frequency");
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        vcoFreq = new LabeledSevenSegment("VCO frequency", 4, 3, "MHz", 1E-6);
        pfdFreq = new LabeledSevenSegment("PFD frequency", 5, 2, "kHz", 1E-3);
        outFreq = new LabeledSevenSegment("Out frequency", 4, 3, "MHz", 1E-6);
        auxFreq = new LabeledSevenSegment("Aux frequency", 4, 3, "MHz", 1E-6);
        vcoSelFreq = new LabeledSevenSegment("VCO selection frequency", 5, 2, "kHz", 1E-3);
        this.add(vcoFreq);
        this.add(pfdFreq);
        this.add(outFreq);
        this.add(auxFreq);
        this.add(vcoSelFreq);
    }

    @Override
    public void notifyChanged(ADF4351Freq newVal) {
        vcoFreq.notifyChanged(newVal.vcoFreq);
        pfdFreq.notifyChanged(newVal.pfdFreq);
        outFreq.notifyChanged(newVal.outFreq);
        auxFreq.notifyChanged(newVal.auxFreq);
        vcoSelFreq.notifyChanged(newVal.vcoSelFreq);       
    }
}
