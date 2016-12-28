package adfctrl.ui;

import java.util.function.Predicate;

public class LabeledIntegerField extends LabeledNumericField<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LabeledIntegerField(String name, int size, Predicate<Integer> validator) {
		super(name, size, validator);
	}

	@Override
	protected Integer parseValue(String val) {
		return Integer.parseInt(val);
	}

}
