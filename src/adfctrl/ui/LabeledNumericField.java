package adfctrl.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Predicate;
import javax.swing.JTextField;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public abstract class LabeledNumericField<T> extends BorderedTitledPanel implements IObserver<T>, ActionListener {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JTextField field;
    private Predicate<T> validator;
    private Observable<T> model;
    
    public LabeledNumericField(String name, int size, Predicate<T> validator) {
    	super(name);
        this.validator = validator;
        field = new JTextField(size);
        field.addActionListener(this);
        this.add(field);
    }

    public void setModel(Observable<T> model) {
        this.model = model;
        model.addObserver(this);
    }
    
    @Override
    public void notifyChanged(T newVal) {
        this.field.setText(newVal.toString());        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (model == null) {
            return;
        }
        T val = this.parseValue(field.getText());
        if (validator.test(val)) {
            model.setValue(val);
        } else {
            model.updateValueAndNotify(null);
        }
    }
    
    protected abstract T parseValue(String val);
}