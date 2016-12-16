package adfctrl.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Predicate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public class LabeledNumericField extends JPanel implements IObserver<Integer>, ActionListener {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JTextField field;
    private Predicate<Integer> validator;
    private Observable<Integer> model;
    
    public LabeledNumericField(String name, int size, Predicate<Integer> validator) {
        this.validator = validator;
        this.add(new JLabel(name));
        field = new JTextField(size);
        field.addActionListener(this);
        this.add(field);
    }

    public void setModel(Observable<Integer> model) {
        this.model = model;
        model.addObserver(this);
    }
    
    @Override
    public void notifyChanged(Integer newVal) {
        this.field.setText(newVal.toString());        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (model == null) {
            return;
        }
        Integer val = Integer.parseInt(field.getText());
        if (validator.test(val)) {
            model.setValue(val);
        } else {
            model.updateValueAndNotify(null);
        }
    }
}