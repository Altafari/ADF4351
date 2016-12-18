package adfctrl.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public class LabeledSliderSwitch<T> extends JPanel implements IObserver<T>, ChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final int BORDER_PADDING = 5;
    private final int INNER_PADDING = 2;
    private final Observable<T> model;
    private final List<T> states;
    private final JSlider slider;
    
    public LabeledSliderSwitch(String title, Observable<T> model, List<T> states, List<String> labels) {
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING,
                        BORDER_PADDING, BORDER_PADDING),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(Color.gray), title)));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));        
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
        JPanel slug = new JPanel();
        slug.setBorder(BorderFactory.createEmptyBorder(INNER_PADDING, INNER_PADDING,
                INNER_PADDING, INNER_PADDING));
        slug.add(slider);
        this.add(slug);
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
