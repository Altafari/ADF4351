package adfctrl.ui;

import adfctrl.utils.IObserver;
import adfctrl.utils.Observable;

public abstract class BorderedModelView<T> extends BorderedTitledPanel implements IObserver<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    protected Observable<T> model;
    
    public BorderedModelView(String title) {
        super(title);        
    }
    
    public void setModel(Observable<T> model) {
        this.model = model;
        model.addObserver(this);
    }

}
