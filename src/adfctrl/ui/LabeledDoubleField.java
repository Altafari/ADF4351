package adfctrl.ui;

import java.util.function.Predicate;

public class LabeledDoubleField extends LabeledNumericField<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LabeledDoubleField(String name, int size, Predicate<Double> validator) {
		super(name, size, validator);
	}

	@Override
	protected Double parseValue(String val) {
		return Double.parseDouble(val);
	}

}
