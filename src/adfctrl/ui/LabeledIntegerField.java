package adfctrl.ui;

import java.util.function.Predicate;

import adfctrl.utils.Observable;

public class LabeledIntegerField extends LabeledNumericField<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LabeledIntegerField(String name, int size, Observable<Integer> model, Predicate<Integer> validator) {
		super(name, size, model, validator);
	}

	@Override
	protected Integer parseValue(String val) {
		return Integer.parseInt(val);
	}

}
