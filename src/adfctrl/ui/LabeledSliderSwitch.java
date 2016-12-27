package adfctrl.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public class LabeledSliderSwitch<T> extends BorderedTitledPanel implements IObserver<T>, ChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final int INNER_PADDING = 2;
    private final Observable<T> model;
    private final List<T> states;
    private final JSlider slider;
    
    public LabeledSliderSwitch(String title, Observable<T> model, List<T> states, List<String> labels) {
        super(title);      
        this.model = model;
        this.states = states;
        Hashtable<Object, Object> nLabels = new Hashtable<Object, Object>();
        int maxHeight = 0;
        for (int i = 0; i < labels.size(); i++) {
            JLabel label = new JLabel(labels.get(i));
            int newHeight = label.getPreferredSize().height; 
            maxHeight = (newHeight > maxHeight)? newHeight : maxHeight;
            nLabels.put(i, label);
        }
        maxHeight += INNER_PADDING;
        slider = new JSlider(JSlider.VERTICAL, 0, states.size() - 1, 0);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setLabelTable(nLabels);
        int sliderWidth = slider.getPreferredSize().width;
        slider.setPreferredSize(new Dimension(sliderWidth, maxHeight * states.size()));
        this.add(slider);
    }
    
    @Override
    protected void setCustomLayout() {
    	this.setLayout(new FlowLayout(FlowLayout.LEFT));
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
