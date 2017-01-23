package adfctrl.ui;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.system.SystemManager;
import adfctrl.ui.sevenseg.LabeledSevenSegment;

public class PllPanel extends BorderedTitledPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final LabeledDoubleField referenceFreq;
	public final LabeledIntegerField integerVal;
	public final LabeledIntegerField fractionalVal;
	public final LabeledIntegerField modulusVal;
	
	public PllPanel() {
		super("PLL settings");
		SystemManager sysMgr = SystemManager.getInstance();
		ADF4351Configurator config = sysMgr.getConfigurator();
		
		referenceFreq = new LabeledDoubleField(
				"Reference freq.",
				8,
				config.referenceFrequency,
				config.getValidator(config.referenceFrequency));
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
		
		LabeledSevenSegment vcoFreq = new LabeledSevenSegment(
		        "VCO frequency",
		        config.deviceFreq.vcoFreq,
		        4,
		        6,
		        "MHz",
		        1.0E-6);
		
		this.add(vcoFreq);
		JPanel numFields = new JPanel();
		numFields.setLayout(new GridLayout(2, 2));
		numFields.add(referenceFreq);
		numFields.add(integerVal);
		numFields.add(fractionalVal);
		numFields.add(modulusVal);
		this.add(numFields);
	}
	@Override
	protected void setCustomLayout(){
	    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
}
