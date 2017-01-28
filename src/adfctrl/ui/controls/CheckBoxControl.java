package adfctrl.ui.controls;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public class CheckBoxControl<T> extends JPanel implements IObserver<T>, ChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final JCheckBox checkBox;
    private final T onState;
    private final T offState;
    private final Observable<T> model;

    public CheckBoxControl(String name, Observable<T> model, T onState, T offState) {
        checkBox = new JCheckBox(name);
        this.model = model;
        this.onState = onState;
        this.offState = offState;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));        
        checkBox.setAlignmentX(CENTER_ALIGNMENT);
        checkBox.addChangeListener(this);
        model.addObserver(this);
        model.notifyChanged(model.getValue());
        this.add(Box.createVerticalGlue());
        this.add(checkBox);
        this.add(Box.createVerticalGlue());
    }

    @Override
    public void notifyChanged(T newVal) {
        checkBox.setSelected(newVal == onState);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        model.notifyChanged(checkBox.isSelected()? onState : offState);
    }
}