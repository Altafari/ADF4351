package adfctrl.ui.sevenseg;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class SevenSegment extends JComponent {
   
    private static final byte[] stateTable = {
            0b0111111,
            0b0000110,
            0b1011011,
            0b1001111,
            0b1100110,
            0b1101101,
            0b1111101,
            0b0000111,
            0b1111111,
            0b1101111 };
    
    private final static int SEG_GAP = 1;
    private final static int SEG_WIDTH = 3;
    private final static int SEG_LENGTH = 12;
    private final static int DIGIT_GAP = 5;
    private final static double DIGIT_SKEW = 0.06;
    private final static int H_INSET = 3;
    private final static int V_INSET = 0;
    private final static int FONT_SIZE = 16;
    
    private static final boolean[] IS_VERTICAL = {
            false, true, true, false, true, true, false };
    
    private static final int[] H_TRANSLATION = {
            0, 1, 1, 0, 0, 0, 0 };
    
    private static final int[] V_TRANSLATION = {
            0, 0, 1, 2, 1, 0, 1 };
    
    private final List<Polygon> segments;
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final int numIntPos;
    private final int numFracPos;
    private final String units;
    private final int textWidth;
    private final Font font;
    private double value;
    
    public SevenSegment(int numIntPos, int numFracPos, String units) {
        this.numIntPos = numIntPos;
        this.numFracPos = numFracPos;
        this.units = units;
        segments = new ArrayList<Polygon>(7);
        for (int i = 0; i < 7; i++) {
            segments.add(buildSegment(i));
        }
        font = new Font("Monospaced", Font.BOLD, FONT_SIZE);
        Canvas c = new Canvas();
        textWidth = c.getFontMetrics(font).stringWidth(units);
    }
    
    public void updateValue(double val) {
        value = val;
        this.repaint();
    }
    
    @Override
    public int getWidth() {
        return textWidth + (SEG_LENGTH + SEG_WIDTH + DIGIT_GAP) * (numIntPos + numFracPos) + 2 * H_INSET;
    }
    
    @Override
    public int getHeight() {
        return SEG_LENGTH * 2 + SEG_WIDTH + DIGIT_GAP + 2 * V_INSET;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(H_INSET, V_INSET);
        g2d.shear(-DIGIT_SKEW, 0.0);
        int digitStep = SEG_LENGTH + SEG_WIDTH + DIGIT_GAP;
        int[] digits = new int[numIntPos + numFracPos];
        getDigits(numIntPos, digits);
        boolean hasNotZero = false;
        for (int i = 0; i < numIntPos; i++) {
            int digit = digits[i];
            if (digit != 0 || i == (numIntPos - 1)) {
                hasNotZero = true;
            }
            if (hasNotZero) {
                drawDigit(digits[i], g);
            }
            g2d.translate(digitStep, 0);
        }
        if (numFracPos > 0) {
            g2d.fillOval(-(SEG_WIDTH + DIGIT_GAP) / 2, SEG_LENGTH * 2, SEG_WIDTH, SEG_WIDTH);
        }
        for (int i = 0; i < numFracPos; i++) {            
            drawDigit(digits[i + numIntPos], g);
            g2d.translate(digitStep, 0);
        }
        g2d.shear(DIGIT_SKEW, 0);
        g2d.setFont(font);
        g2d.drawString(units, 0, SEG_LENGTH * 2 + SEG_WIDTH);
    }
    
    private Polygon buildSegment(int idx) {
       int hTranslation = H_TRANSLATION[idx];
       int vTranslation = V_TRANSLATION[idx];
       boolean isVertical = IS_VERTICAL[idx];
       int[] xPoints = new int[6];
       int[] yPoints = new int[6];
       int[] refA, refB;
       if (isVertical) {
           refA = xPoints;
           refB = yPoints;
       } else {
           refA = yPoints;
           refB = xPoints;
       }
       refA[0] = 0;
       refA[1] = SEG_WIDTH / 2;
       refA[2] = SEG_WIDTH;
       refA[3] = SEG_WIDTH;
       refA[4] = SEG_WIDTH / 2;
       refA[5] = 0;
       refB[0] = SEG_GAP + SEG_WIDTH;
       refB[1] = SEG_GAP + SEG_WIDTH / 2;
       refB[2] = SEG_GAP + SEG_WIDTH;
       refB[3] = SEG_LENGTH - SEG_GAP;
       refB[4] = SEG_LENGTH + SEG_WIDTH / 2 - SEG_GAP;
       refB[5] = SEG_LENGTH - SEG_GAP;
       for(int i = 0; i < xPoints.length; i++) {
           xPoints[i] += hTranslation * SEG_LENGTH;
       }
       for(int i = 0; i < yPoints.length; i++) {
           yPoints[i] += vTranslation * SEG_LENGTH;
       }
       return new Polygon(xPoints, yPoints, 6);
    }
    
    private void drawDigit(int digit, Graphics g) {
        byte mask = stateTable[digit];
        for (Polygon p : segments) {
            if ((mask & 1) == 1) {
                g.fillPolygon(p);
            }
            mask >>= 1;
        }
    }
    
    private void getDigits(int integer, int[] digits) {
        double rad = Math.pow(10.0, integer - 1);
        double val = value;
        for (int i = 0; i < digits.length - 1; i++) {
            digits[i] = (int) Math.floor(val / rad);
            val -= digits[i] * rad;
            rad /= 10; 
        }
        digits[digits.length - 1] = (int) Math.round(val / rad);
        int pos = digits.length - 1;
        while (digits[pos] == 10 && pos > 0) {
            digits[pos] = 0;
            pos--;
            digits[pos]++;
        }
        if (digits[0] > 9 || digits[0] < 0) {
            for (int i = 0; i < digits.length; i++) {
                digits[i] = 9;
            }
        }
    }
}
