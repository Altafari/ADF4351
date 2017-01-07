package adfctrl.ui;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.system.SystemManager;

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

		this.add(referenceFreq);
		this.add(integerVal);
		this.add(fractionalVal);
		this.add(modulusVal);
	}
}
