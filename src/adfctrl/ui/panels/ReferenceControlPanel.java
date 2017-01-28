package adfctrl.ui.panels;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Configurator.ReferenceMode;
import adfctrl.system.SystemManager;
import adfctrl.ui.controls.BorderedTitledPanel;
import adfctrl.ui.controls.LabeledComboBox;
import adfctrl.ui.controls.LabeledDoubleField;
import adfctrl.ui.controls.LabeledIntegerField;
import adfctrl.ui.sevenseg.LabeledSevenSegment;

public class ReferenceControlPanel extends BorderedTitledPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final LabeledDoubleField refFreq;
    private final LabeledIntegerField rCounter;
    private final LabeledComboBox<ReferenceMode> refMode;
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
        refMode = new LabeledComboBox<ReferenceMode>(
                "Ref mode",
                config.referenceMode,
                Arrays.asList(ReferenceMode.DIV2, ReferenceMode.NORM, ReferenceMode.X2),
                Arrays.asList("Div2", "Normal", "X2"));
        pfdFreq = new LabeledSevenSegment(
                "PFD frequency",
                config.deviceFreq.pfdFreq,
                5,
                1,
                "kHz",
                1E-3);
        bSelDiv = new LabeledIntegerField(
                null,
                3,
                config.bandSelectDividerField,
                config.getValidator(config.bandSelectDividerField));
        bSelDiv.setSource(config.bandSelectDivider);
        bSelAuto = new JCheckBox("Auto");
        bSelAuto.addActionListener((s) -> config.bandSelectAutoSwitch.notifyChanged(bSelAuto.isSelected()));
        vcoSelFreq = new LabeledSevenSegment(
                "VCO selection frequency",
                config.deviceFreq.vcoBandSelFreq,
                3,
                2,
                "kHz",
                1E-3);
        JPanel ctrlGroup = new JPanel();
        ctrlGroup.setLayout(new GridLayout(2,2));
        JPanel bSelRow = new BorderedTitledPanel("Band Fs") {
            private static final long serialVersionUID = 1L;            
            @Override
            protected void setCustomLayout() {
                this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));                
            }
        };
        bSelRow.add(bSelDiv);
        bSelRow.add(bSelAuto);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        ctrlGroup.add(refFreq);
        ctrlGroup.add(rCounter);
        ctrlGroup.add(refMode);
        ctrlGroup.add(bSelRow);
        this.add(pfdFreq);
        this.add(ctrlGroup);
        this.add(vcoSelFreq);        
        config.bandSelectAutoSwitch.addObserver((s) -> bSelDiv.setEditable(!s));
        config.bandSelectAutoSwitch.notifyChanged(true);
        bSelAuto.setSelected(true);        
    }
}
