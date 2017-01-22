package adfctrl.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Predicate;
import javax.swing.JTextField;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public abstract class LabeledNumericField<T> extends BorderedModelView<T> implements IObserver<T>, ActionListener {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JTextField field;
    private Predicate<T> validator;
    
    public LabeledNumericField(String name, int size, Observable<T> model, Predicate<T> validator) {
    	super(name);
        this.validator = validator;
        setSource(model);
        setSink(model);
        field = new JTextField(size);
        field.addActionListener(this);
        this.add(field);
        notifyChanged(source.getValue());
    }
    
    @Override
    public void notifyChanged(T newVal) {
        this.field.setText(newVal.toString());        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        T val = null;
        try {
            val = this.parseValue(field.getText());
        } catch (Exception ex) { }
        if (val != null && validator.test(val)) {
            sink.notifyChanged(val);
        } else {
            notifyChanged(source.getValue());
        }
    }

    public void setEditable(boolean b) {
        field.setEditable(b);
    }
    
    protected abstract T parseValue(String val);
}