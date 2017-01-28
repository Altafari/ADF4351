package adfctrl.ui.panels;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Configurator.SynthMode;
import adfctrl.icmodel.ADF4351Proxy.FeedbackMode;
import adfctrl.icmodel.ADF4351Proxy.NoiseMode;
import adfctrl.icmodel.ADF4351Proxy.RfDivider;
import adfctrl.system.SystemManager;
import adfctrl.ui.controls.BorderedTitledPanel;
import adfctrl.ui.controls.LabeledComboBox;
import adfctrl.ui.controls.LabeledIntegerField;
import adfctrl.ui.sevenseg.LabeledSevenSegment;

public class PllPanel extends BorderedTitledPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final static int NUM_CP_STATES = 16;
	
	private final LabeledIntegerField integerVal;
	private final LabeledIntegerField fractionalVal;
	private final LabeledIntegerField modulusVal;	
	private final LabeledComboBox<SynthMode> synthMode;
	private final LabeledComboBox<RfDivider> rfDivider;
	private final LabeledComboBox<FeedbackMode> feedbackMode;
	private final LabeledComboBox<Integer> cpCurrent;
	private final LabeledComboBox<NoiseMode> noiseMode;
	
	public PllPanel() {
		super("PLL settings");
		SystemManager sysMgr = SystemManager.getInstance();
		ADF4351Configurator config = sysMgr.getConfigurator();

		integerVal = new LabeledIntegerField(
				"Integer",
				6,
				config.intValue,
				config.getValidator(config.intValue));
		fractionalVal = new LabeledIntegerField(
				"Fractional",
				6,
				config.fracValue,
				config.getValidator(config.fracValue));
		modulusVal = new LabeledIntegerField(
				"Modulus",
				6,
				config.modValue,
				config.getValidator(config.modValue));
		
		synthMode = new LabeledComboBox<SynthMode>(
		        "Synth. mode",
		        config.synthMode,
		        Arrays.asList(SynthMode.INTEGER, SynthMode.FRACTIONAL),
		        Arrays.asList("Integer", "Fractional"));
		
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
        
        rfDivider = new LabeledComboBox<RfDivider>(
                "RF divider mode",
                config.rfDividerMode,
                rfDivModes,
                rfDivLabels);
        
        feedbackMode = new LabeledComboBox<FeedbackMode>(
                "Feedback mode",
                config.feedbackMode,
                Arrays.asList(FeedbackMode.FUNDAMENTAL, FeedbackMode.DIVIDED),
                Arrays.asList("Fundamental", "Divided"));
        
        cpCurrent = new LabeledComboBox<Integer>(
                "CP current",
                config.cpCurrent,
                IntStream.range(0, NUM_CP_STATES).boxed().collect(Collectors.toList()),
                IntStream.range(0, NUM_CP_STATES).boxed().map((s) -> "Level " + s.toString())
                .collect(Collectors.toList()));
        
        noiseMode = new LabeledComboBox<NoiseMode>(
                "Perfomance",
                config.noiseMode,
                Arrays.asList(NoiseMode.LOW_NOISE, NoiseMode.LOW_SPUR),
                Arrays.asList("Low noise","Low spur"));
		
		LabeledSevenSegment vcoFreq = new LabeledSevenSegment(
		        "VCO frequency",
		        config.deviceFreq.vcoFreq,
		        4,
		        6,
		        "MHz",
		        1.0E-6);
		
		this.add(vcoFreq);
		JPanel numFields = new JPanel();
		numFields.setLayout(new GridLayout(4, 2));
	    numFields.add(modulusVal);
		numFields.add(synthMode);
		numFields.add(integerVal);
		numFields.add(fractionalVal);
		numFields.add(rfDivider);
		numFields.add(feedbackMode);
		numFields.add(cpCurrent);
		numFields.add(noiseMode);
		this.add(numFields);
	}
	@Override
	protected void setCustomLayout(){
	    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
}
