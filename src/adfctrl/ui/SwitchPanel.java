package adfctrl.ui;

import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Configurator.SynthMode;
import adfctrl.icmodel.ADF4351Proxy.AuxMode;
import adfctrl.icmodel.ADF4351Proxy.FeedbackMode;
import adfctrl.icmodel.ADF4351Proxy.NoiseMode;
import adfctrl.icmodel.ADF4351Proxy.PowerMode;
import adfctrl.icmodel.ADF4351Proxy.RfDivider;
import adfctrl.system.SystemManager;

public class SwitchPanel extends JPanel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public final LabeledSliderSwitch<SynthMode> synthModeSwitch;
    public final LabeledSliderSwitch<FeedbackMode> feedbackModeSwitch;
    public final LabeledSliderSwitch<NoiseMode> noiseModeSwitch;
    public final LabeledSliderSwitch<PowerMode> outputPowerSwitch;
    public final LabeledSliderSwitch<Boolean> outputSwitch;
    public final LabeledSliderSwitch<PowerMode> auxPowerSwitch;
    public final LabeledSliderSwitch<Boolean> auxSwitch;
    public final LabeledSliderSwitch<AuxMode> auxModeSwitch;    //TODO: move these two out
    public final LabeledComboBox<RfDivider> rfDividerSwitch;
    
    public SwitchPanel() {
        
        SystemManager sysMgr = SystemManager.getInstance();
        ADF4351Configurator config = sysMgr.getConfigurator();
        
        synthModeSwitch = new LabeledSliderSwitch<SynthMode>(
                "Synth Mode",
                config.synthMode,
                Arrays.asList(SynthMode.INTEGER, SynthMode.FRACTIONAL),
                Arrays.asList("Integer", "Fractional"));
        
        feedbackModeSwitch = new LabeledSliderSwitch<FeedbackMode>(
                "Loop feedback mode",
                config.feedbackMode,
                Arrays.asList(FeedbackMode.FUNDAMENTAL, FeedbackMode.DIVIDED),
                Arrays.asList("Fundamental", "Divided"));
        
        noiseModeSwitch = new LabeledSliderSwitch<NoiseMode>(
                "Performance mode",
                config.noiseMode,
                Arrays.asList(NoiseMode.LOW_NOISE, NoiseMode.LOW_SPUR),
                Arrays.asList("Low noise", "Low spur"));
        
        List<PowerMode> powerLevels = Arrays.asList(
                PowerMode.MODE_MINUS_4DBM,
                PowerMode.MODE_MINUS_1DBM,
                PowerMode.MODE_PLUS_2DBM,
                PowerMode.MODE_PLUS_5DBM);
        
        List<String> powerLevelLabels = Arrays.asList(
                "-4dBm",
                "-1dBm",
                "+2dBm",
                "+5dBm");
        List<Boolean> onOffStates = Arrays.asList(false, true);
        List<String> onOffLabels = Arrays.asList("Off", "On");
        
        outputPowerSwitch = new LabeledSliderSwitch<PowerMode>(
                "OUT power",
                config.outputPower,
                powerLevels,
                powerLevelLabels);
        
        outputSwitch = new LabeledSliderSwitch<Boolean>(
                "OUT enable",
                config.outputControl,
                onOffStates,
                onOffLabels);
        
        auxPowerSwitch = new LabeledSliderSwitch<PowerMode>(
                "AUX power",
                config.auxPower,
                powerLevels,
                powerLevelLabels);

        auxSwitch = new LabeledSliderSwitch<Boolean>(
                "AUX enable",
                config.auxControl,
                onOffStates,
                onOffLabels);
        
        auxModeSwitch = new LabeledSliderSwitch<AuxMode>(
                "AUX mode",
                config.auxMode,
                Arrays.asList(AuxMode.DIVIDED_OUTPUT, AuxMode.FUNDAMENTAL),
                Arrays.asList("Divided", "Fundamental"));
        
        List<RfDivider> rfDivModes = Arrays.asList(
                RfDivider.DIV_1,
                RfDivider.DIV_2,
                RfDivider.DIV_4,
                RfDivider.DIV_8,
                RfDivider.DIV_16,
                RfDivider.DIV_32,
                RfDivider.DIV_64);
        
        List<String> rfDivLabels = Arrays.asList(
                "1/1",
                "1/2",
                "1/4",
                "1/8",
                "1/16",
                "1/32",
                "1/64");
        
        rfDividerSwitch = new LabeledComboBox<RfDivider>(
                "RF divider mode",
                config.rfDividerMode,
                rfDivModes,
                rfDivLabels);
        
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

        this.add(synthModeSwitch);
        this.add(feedbackModeSwitch);
        this.add(noiseModeSwitch);
        this.add(outputGroup);
        this.add(auxModeSwitch);
        this.add(rfDividerSwitch);
    }
}
