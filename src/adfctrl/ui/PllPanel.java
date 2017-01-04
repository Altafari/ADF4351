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
				"Reference freq.", 8, (v) -> v < Double.MAX_VALUE && v > 0.0);
		integerVal = new LabeledIntegerField(
				"Integer", 6, (v) -> v > 0 && v < 100);
		fractionalVal = new LabeledIntegerField(
				"Fractional", 6, (v) -> v > 0 && v < 100);
		modulusVal = new LabeledIntegerField(
				"Modulus", 6, (v) -> v > 0 && v < 100);
		
		
		referenceFreq.setModel(config.referenceFrequency);
		integerVal.setModel(config.intValue);
		fractionalVal.setModel(config.fracValue);
		modulusVal.setModel(config.modValue);
		this.add(referenceFreq);
		this.add(integerVal);
		this.add(fractionalVal);
		this.add(modulusVal);
	}
}
