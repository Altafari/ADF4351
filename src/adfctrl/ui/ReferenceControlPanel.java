package adfctrl.ui;

import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Configurator.ReferenceMode;
import adfctrl.icmodel.ADF4351Freq;
import adfctrl.system.SystemManager;
import adfctrl.ui.sevenseg.LabeledSevenSegment;

public class ReferenceControlPanel extends BorderedModelView<ADF4351Freq> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final LabeledDoubleField refFreq;
    private final LabeledIntegerField rCounter;
    private final LabeledSliderSwitch<ReferenceMode> refMode;
    private final LabeledSevenSegment pfdFreq;
    private final LabeledIntegerField bSelDiv;
    private final JCheckBox bSelAuto;
    private final LabeledSevenSegment vcoSelFreq;
    
    public ReferenceControlPanel() {
        super("Reference control");
        ADF4351Configurator config = SystemManager.getInstance().getConfigurator();
        refFreq = new LabeledDoubleField(
                "Ref freq.",
                8,
                config.referenceFrequency,
                config.getValidator(config.referenceFrequency));
        rCounter = new LabeledIntegerField(
                "R counter",
                8,
                config.rCounter,
                config.getValidator(config.rCounter));
        refMode = new LabeledSliderSwitch<ReferenceMode>(
                "Ref mode",
                config.referenceMode,
                Arrays.asList(ReferenceMode.DIV2, ReferenceMode.NORM, ReferenceMode.X2),
                Arrays.asList("Div2", "Normal", "X2"));
        pfdFreq = new LabeledSevenSegment(
                "PFD frequency",
                5,
                3,
                "kHz",
                1E-3);
        bSelDiv = new LabeledIntegerField(
                null,
                8,
                config.bandSelectDivider,
                config.getValidator(config.bandSelectDivider));
        bSelAuto = new JCheckBox("Auto");;
        vcoSelFreq = new LabeledSevenSegment(
                "VCO selection frequency",
                5,
                3,
                "kHz",
                1E-3);
        JPanel firstRow = new JPanel();
        firstRow.setLayout(new FlowLayout());
        JPanel numFields = new JPanel();
        numFields.setLayout(new BoxLayout(numFields, BoxLayout.PAGE_AXIS));
        JPanel bSelRow = new BorderedTitledPanel("Band select divider") {
            private static final long serialVersionUID = 1L;            
            @Override
            protected void setCustomLayout() {
                this.setLayout(new FlowLayout());                
            }
        };
        bSelRow.add(bSelDiv);
        bSelDiv.setEditable(false);
        bSelRow.add(bSelAuto);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        numFields.add(refFreq);
        numFields.add(rCounter);
        firstRow.add(numFields);
        firstRow.add(refMode);
        this.add(firstRow);
        this.add(pfdFreq);
        this.add(bSelRow);
        this.add(vcoSelFreq);
    }
    
    @Override
    public void notifyChanged(ADF4351Freq newVal) {
        pfdFreq.notifyChanged(newVal.pfdFreq);
        vcoSelFreq.notifyChanged(newVal.vcoSelFreq);
        
    }

}
