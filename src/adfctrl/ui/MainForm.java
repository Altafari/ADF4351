package adfctrl.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import adfctrl.system.SystemManager;
import adfctrl.utils.Observable;

public class MainForm {
    
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("ADF-CTRL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    private static void addComponentsToPane(Container pane) {
        JPanel ctrlPanel = new JPanel();
        LabeledNumericField integerValueField = 
                new LabeledNumericField("Test", 6, (v) -> v > 0 && v < 100); // TODO: move validator to model  
        ctrlPanel.add(integerValueField);
        Observable<Integer> intVal = SystemManager.getInstance().getConfigurator().intValue; 
        integerValueField.setModel(intVal);
        pane.add(ctrlPanel, BorderLayout.LINE_START); 
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });       
    }
}
