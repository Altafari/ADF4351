package adfctrl.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ControlsGroup extends JPanel {
    /**
     * 
     */ 
    private static final long serialVersionUID = 1L;
    
    private static final int BORDER_PADDING = 2;    

    public ControlsGroup(String title) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING,
                        BORDER_PADDING, BORDER_PADDING),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(Color.gray), title)));
    }
}
