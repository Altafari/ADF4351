package adfctrl.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public class LabeledSliderSwitch<T extends Enum<?>> extends JPanel implements IObserver<T>, ChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final int BORDER_PADDING = 5;
    private final Observable<T> model;
    private final List<T> states;
    private final JSlider slider;
    
    public LabeledSliderSwitch(String title, Observable<T> model, List<T> states, List<String> labels) {
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING,
                        BORDER_PADDING, BORDER_PADDING),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(Color.gray), title)));
        
        this.model = model;
        this.states = states;
        Hashtable<Object, Object> nLabels = new Hashtable<Object, Object>();
        for (int i = 0; i < labels.size(); i++) {
            nLabels.put(i, new JLabel(labels.get(i)));
        }
        slider = new JSlider(JSlider.VERTICAL, 0, states.size() - 1, 0);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setLabelTable(nLabels);
        this.add(slider);
        // TODO: set proper dimensions
    }
    
    @Override
    public void notifyChanged(T newVal) {
        int idx = states.indexOf(newVal);
        if (idx < 0) {
            return;
        }
        slider.setValue(idx);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        model.setValue(states.get(slider.getValue()));
    }
}
