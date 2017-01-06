package adfctrl.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import adfctrl.system.SystemManager;
import adfctrl.utils.IObserver;

public class DeviceBitView extends BorderedTitledPanel implements IObserver<List<Integer>>{

    private class BitView extends JComponent {
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private static final int INSET = 2;
        private static final int WIDTH = 10;
        private static final int HEIGHT = 14;
        private boolean state;        
        
        public void setState(boolean b) {
            if (state != b) {
                state = b;
                this.repaint();
            }
        }
        
        @Override
        public int getWidth() {
            return WIDTH;
        }
        
        @Override
        public int getHeight() {
            return HEIGHT;
        }
       
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(getWidth(), getHeight());            
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            if (state) {
                g.fillRect(INSET, INSET, 1 + getWidth() - INSET * 2, 1 + getHeight() - INSET * 2);
            }
            else {
                g.drawRect(INSET, INSET, getWidth() - INSET * 2, getHeight() - INSET * 2);
            }
        }
    }
    
    private class BitViewRow extends BorderedTitledPanel {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
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

    private List<BitViewRow> rows;
    
    public DeviceBitView() {        
        super("Device bit state");
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        rows = new ArrayList<BitViewRow>(N_ROWS);
        for (int i = 0; i < N_ROWS; i++) {
            BitViewRow b = new BitViewRow("DWORD" + i, N_BITS); 
            rows.add(b);
            this.add(b);
        }
        SystemManager.getInstance().getConfigurator().bitState.addObserver(this);
    }

    @Override
    public void notifyChanged(List<Integer> newVal) {
        for (int i = 0; i < N_ROWS; i++) {
            rows.get(i).updateState(newVal.get(i));
        }
    }
}