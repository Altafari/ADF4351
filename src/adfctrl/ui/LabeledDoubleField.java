package adfctrl.ui;

import java.util.function.Predicate;

import adfctrl.utils.Observable;

public class LabeledDoubleField extends LabeledNumericField<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LabeledDoubleField(String name, int size, Observable<Double> model, Predicate<Double> validator) {
		super(name, size, model, validator);
	}

	@Override
	protected Double parseValue(String val) {
		return Double.parseDouble(val);
	}

}
