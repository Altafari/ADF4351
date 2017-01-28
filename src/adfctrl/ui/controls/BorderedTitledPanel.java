package adfctrl.ui.controls;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class BorderedTitledPanel extends JPanel {
    /**
     * 
     */ 
    private static final long serialVersionUID = 1L;
    
    private static final int BORDER_PADDING = 2;    

    public BorderedTitledPanel(String title) {
        setCustomLayout();
        if (title != null) {
            this.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.gray), title),
                    BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING,
                            BORDER_PADDING, BORDER_PADDING)));
        }
    }
    
    protected void setCustomLayout() {
    	this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }
}
