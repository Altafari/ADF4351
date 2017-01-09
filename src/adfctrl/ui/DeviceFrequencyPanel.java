package adfctrl.ui;

import javax.swing.JLabel;

import adfctrl.icmodel.ADF4351Freq;

public class DeviceFrequencyPanel extends BorderedModelView<ADF4351Freq> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JLabel vcoFreq;
    private JLabel pfdFreq;
    private JLabel outFreq;
    private JLabel auxFreq;
    private JLabel vcoSelFreq;

    public DeviceFrequencyPanel() {
        super("Device frequency monitor");
        vcoFreq = new JLabel();
        pfdFreq = new JLabel();
        outFreq = new JLabel();
        auxFreq = new JLabel();
        vcoSelFreq = new JLabel();
        this.add(vcoFreq);
        this.add(pfdFreq);
        this.add(outFreq);
        this.add(auxFreq);
        this.add(vcoSelFreq);
    }

    @Override
    public void notifyChanged(ADF4351Freq newVal) {
        // TODO Auto-generated method stub
        
    }

}
