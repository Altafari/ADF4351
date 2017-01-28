package adfctrl.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import adfctrl.ui.panels.PllPanel;
import adfctrl.ui.panels.ReferenceControlPanel;
import adfctrl.ui.panels.SwitchPanel;

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
        JPanel pllPanel = new PllPanel();
        JPanel switchPanel = new SwitchPanel();
        switchPanel.setLayout(new BoxLayout(switchPanel, BoxLayout.PAGE_AXIS));
        ReferenceControlPanel rPanel = new ReferenceControlPanel();
        ctrlPanel.add(rPanel);
        ctrlPanel.add(pllPanel);
        ctrlPanel.add(switchPanel);
        //ctrlPanel.add(new DeviceBitView(SystemManager.getInstance().getConfigurator().bitState));
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
