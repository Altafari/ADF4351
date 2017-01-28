package adfctrl.ui.controls;

import adfctrl.utils.IObservable;
import adfctrl.utils.IObserver;

public abstract class BorderedModelView<T> extends BorderedTitledPanel implements IObserver<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    protected IObservable<T> source;
    protected IObserver<T> sink; 
    
    public BorderedModelView(String title) {
        super(title);        
    }
    
    public void setSource(IObservable<T> source) {
        this.source = source;
        source.addObserver(this);
    }
    
    public void setSink(IObserver<T> sink) {
        this.sink = sink;
    }
}
