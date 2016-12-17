package adfctrl.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Configurator.SynthMode;
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
        SystemManager sysMgr = SystemManager.getInstance();
        ADF4351Configurator config = sysMgr.getConfigurator();
        
        LabeledNumericField integerValueField = new LabeledNumericField(
                "Test", 6, (v) -> v > 0 && v < 100); // TODO: move validator to model         
        ctrlPanel.add(integerValueField);
        
        
        List<SynthMode> stateList = Arrays.asList(SynthMode.INTEGER, SynthMode.FRACTIONAL);
        List<String> labels = Arrays.asList("Integer", "Fractional");
        LabeledSliderSwitch<SynthMode> synthModeSwitch = new LabeledSliderSwitch<SynthMode>(
                "Mode", config.synthMode, stateList, labels);
        ctrlPanel.add(synthModeSwitch);
        
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
