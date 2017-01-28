package adfctrl.ui.sevenseg;

import adfctrl.ui.controls.BorderedModelView;
import adfctrl.utils.IObservable;

public class LabeledSevenSegment extends BorderedModelView<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final SevenSegment display;
    private final double scale;
    
    public LabeledSevenSegment(
            String title, IObservable<Double> source, int numIntPos, int numFracPos, String units, double scale) {
        super(title);
        setSource(source);
        this.scale = scale;
        display = new SevenSegment(numIntPos, numFracPos, units);
        this.add(display);        
        notifyChanged(source.getValue());
    }

    @Override
    public void notifyChanged(Double newVal) {
        display.updateValue(newVal * scale);        
    }

}
