package adfctrl.ui.sevenseg;

import adfctrl.ui.BorderedModelView;

public class LabeledSevenSegment extends BorderedModelView<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final SevenSegment display;
    private final double scale;
    
    public LabeledSevenSegment(String title, int numIntPos, int numFracPos, String units, double scale) {
        super(title);
        this.scale = scale;
        display = new SevenSegment(numIntPos, numFracPos, units);
        this.add(display);
    }

    @Override
    public void notifyChanged(Double newVal) {
        display.updateValue(newVal * scale);        
    }

}
