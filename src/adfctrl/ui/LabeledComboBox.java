package adfctrl.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public class LabeledComboBox<T> extends BorderedModelView<T> implements IObserver<T>, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> combo;
	private List<T> states;
	
	public LabeledComboBox(String name, Observable<T> model, List<T> states, List<String> labels) {
		super(name);
		combo = new JComboBox<String>();
		combo.addActionListener(this);
		setSource(model);
		setSink(model);
		this.states = states;
		for (String s : labels) {
			combo.addItem(s);	
		}
		this.add(combo);
		notifyChanged(source.getValue());
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		sink.notifyChanged(states.get(combo.getSelectedIndex()));
	}

	@Override
	public void notifyChanged(T newVal) {
		int idx = states.indexOf(newVal);
		if (idx < 0) {
			return;
		}
		combo.setSelectedIndex(idx);
	}
}
