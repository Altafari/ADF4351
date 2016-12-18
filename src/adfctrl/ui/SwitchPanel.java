package adfctrl.ui;

import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Configurator.SynthMode;
import adfctrl.icmodel.ADF4351Proxy.FeedbackMode;
import adfctrl.icmodel.ADF4351Proxy.NoiseMode;
import adfctrl.icmodel.ADF4351Proxy.PowerMode;
import adfctrl.system.SystemManager;

public class SwitchPanel extends JPanel {
    
    public final LabeledSliderSwitch synthModeSwitch;
    public final LabeledSliderSwitch feedbackModeSwitch;
    public final LabeledSliderSwitch noiseModeSwitch;
    
    public final LabeledSliderSwitch outputPowerSwitch;
    public final LabeledSliderSwitch outputSwitch;
    public final LabeledSliderSwitch auxPowerSwitch;
    public final LabeledSliderSwitch auxSwitch;
    public final LabeledSliderSwitch auxModeSwitch;
    
    public final LabeledSliderSwitch rfDividerSwitch;
    
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
                Arrays.asList(FeedbackMode.FUNDAMENTAL, FeedbackMode.FUNDAMENTAL);
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
        List<Boolean> onOffStates = Arrays.asList(true, false);
        List<String> onOffLabels = Arrays.asList("On", "Off");
        
        outputPowerSwitch = new LabeledSliderSwitch<PowerMode>(
                "Output power level",
                config.outputPower,
                powerLevels,
                powerLevelLabels);
        
        outputSwitch = new LabeledSliderSwitch<Boolean>(
                "Output control",
                config.outputControl,
                onOffStates,
                onOffLabels);
        
        auxPowerSwitch = new LabeledSliderSwitch<PowerMode>(
                "AUX power level",
                config.auxPower,
                powerLevels,
                powerLevelLabels);

        auxSwitch = new LabeledSliderSwitch<Boolean>(
                "AUX control",
                config.auxControl,
                onOffStates,
                onOffLabels);
        
        auxModeSwitch = new LabeledSliderSwitch<AuxMode>(
                "AUX mode",
                
                );
        
        this.add(synthModeSwitch);
    }

}
