package adfctrl.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import adfctrl.utils.IObserver;

public class DeviceBitView extends BorderedTitledPanel implements IObserver{

    private class BitView extends JComponent {
        
        private static final int inset = 3;
        private boolean state;        
        
        public void setState(boolean b) {
            state = b;
        }
        
        // TODO: do something with preferred size
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            if (state) {
                g.fillRect(inset, inset, getWidth() - inset, getHeight() - inset);
            }
            else {
                g.drawRect(inset, inset, getWidth() - inset, getHeight() - inset);
            }
        }
    }
    
    private class BitViewRow extends BorderedTitledPanel {

        private List<BitView> bitViews;
        
        public BitViewRow(String title, int nBits) {
            super(title);
            bitViews = new ArrayList<BitView>(nBits);
            for (int i = 0; i < nBits; i++) {
                bitViews.add(new BitView());
            }
            for (int i = nBits - 1; i >= 0; i--) {
                this.add(bitViews.get(i));
            }
        }
        
        public void updateState(int state) {
            for(BitView b : bitViews) {
                b.setState((state & 1) == 1);
                state >>= 1;
            }
        }
    }
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int N_BITS = 32;
    private static final int N_ROWS = 6;

    private List<JPanel> rows;
    
    public DeviceBitView() {
        super("Device bit state");
        
    }

    @Override
    public void notifyChanged(Object newVal) {
        // TODO Auto-generated method stub
        
    }

}
