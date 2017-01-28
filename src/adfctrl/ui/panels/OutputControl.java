package adfctrl.ui.panels;

import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Proxy.AuxMode;
import adfctrl.icmodel.ADF4351Proxy.PowerMode;
import adfctrl.system.SystemManager;
import adfctrl.ui.controls.BorderedTitledPanel;
import adfctrl.ui.controls.CheckBoxControl;
import adfctrl.ui.controls.LabeledComboBox;
import adfctrl.ui.sevenseg.LabeledSevenSegment;

public class OutputControl extends JPanel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public final LabeledComboBox<PowerMode> outputPowerSwitch;
    public final CheckBoxControl<Boolean> outputSwitch;
    public final LabeledComboBox<PowerMode> auxPowerSwitch;
    public final CheckBoxControl<Boolean> auxSwitch;
    public final LabeledComboBox<AuxMode> auxModeSwitch;
    
    public OutputControl() {
        
        SystemManager sysMgr = SystemManager.getInstance();
        ADF4351Configurator config = sysMgr.getConfigurator();
        
        List<PowerMode> powerLevels = Arrays.asList(
                PowerMode.MODE_PLUS_5DBM,
                PowerMode.MODE_PLUS_2DBM,
                PowerMode.MODE_MINUS_1DBM,
                PowerMode.MODE_MINUS_4DBM);
        
        List<String> powerLevelLabels = Arrays.asList(
                "+5dBm",
                "+2dBm",
                "-1dBm",
                "-4dBm");
        
        outputPowerSwitch = new LabeledComboBox<PowerMode>(
                "OUT power",
                config.outputPower,
                powerLevels,
                powerLevelLabels);
        
        outputSwitch = new CheckBoxControl<Boolean>(
                "OUT enable",
                config.outputControl,
                true,
                false);
        
        auxPowerSwitch = new LabeledComboBox<PowerMode>(
                "AUX power",
                config.auxPower,
                powerLevels,
                powerLevelLabels);

        auxSwitch = new CheckBoxControl<Boolean>(
                "AUX enable",
                config.auxControl,
                true,
                false);
        
        auxModeSwitch = new LabeledComboBox<AuxMode>(
                "AUX mode",
                config.auxMode,
                Arrays.asList(AuxMode.DIVIDED_OUTPUT, AuxMode.FUNDAMENTAL),
                Arrays.asList("Divided", "Fundamental"));
        
        
        JPanel outputGroup = new BorderedTitledPanel("Output controls");
        JPanel auxCol = new JPanel();
        auxCol.setLayout(new BoxLayout(auxCol, BoxLayout.PAGE_AXIS));
        JPanel outCol = new JPanel();
        outCol.setLayout(new BoxLayout(outCol, BoxLayout.PAGE_AXIS));
        outputGroup.setLayout(new FlowLayout(FlowLayout.CENTER));
        outCol.add(outputPowerSwitch);
        outCol.add(outputSwitch);
        auxCol.add(auxPowerSwitch);
        auxCol.add(auxSwitch);
        outputGroup.add(auxCol);
        outputGroup.add(outCol);
        LabeledSevenSegment outFreq = new LabeledSevenSegment(
                "Out frequency",
                config.deviceFreq.outFreq,
                4,
                3,
                "MHz",
                1E-6);
        LabeledSevenSegment auxFreq = new LabeledSevenSegment(
                "Aux frequency",
                config.deviceFreq.auxFreq,
                4,
                3,
                "MHz",
                1E-6);
        this.add(outFreq);
        this.add(auxFreq);
        this.add(outputGroup);
        this.add(auxModeSwitch);
    }
}
