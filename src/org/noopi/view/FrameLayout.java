package org.noopi.view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import org.noopi.utils.listeners.AddRuleEventListener;
import org.noopi.utils.listeners.NewFileEventListener;
import org.noopi.utils.listeners.OpenFileEventListener;
import org.noopi.utils.listeners.RemoveRuleEventListener;
import org.noopi.utils.listeners.RunEventListener;
import org.noopi.utils.listeners.SaveEventListener;
import org.noopi.utils.listeners.SpeedChangeEventListener;
import org.noopi.utils.listeners.StepEventListener;
import org.noopi.utils.listeners.StopEventListener;
import org.noopi.utils.listeners.TapeInitializationEventListener;

public class FrameLayout implements IFrameLayout {

    //ATTRIBUTS
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JButton stopButton;
    private JButton startButton;
    private JButton removeRuleButton;
    private JButton addRuleButton;
    private JButton stepButton;
    private JButton initButton;
    private JTextField addRuleSymbolTextField;
    private JTextField addRuleStateTextField;
    private JTextField addResuSymbolTextField;
    private JTextField addResuStateTextField;
    private JTextField addResuDirectionTextField;
    private JTextField initialRubanTextField;
    private JTextField removeRuleSymbolTextField;
    private JTextField removeRuleStateTextField;
    private JTextField removeResuSymbolTextField;
    private JTextField removeResuStateTextField;
    private JTextField removeResuDirectionTextField;
    private JSlider speedSlider;
    private JTextArea historyTextArea;
    private JTextArea rulesTextArea;
    private JTextArea paneRulesTextArea;
    private GraphicTape tape;

    //CONSTRUCTEURS

    public FrameLayout() {
        createView();
        placeComponent();
        createController();
    }

    @Override
    public JComponent getView() {
        return mainPanel;
    }

    @Override
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void shiftTapeRight() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shiftTapeLeft() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSymbolOnTape() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMachineState() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addRule() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeRule() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetRules() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushHistory() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void popHistory() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addAddRuleEventListener(AddRuleEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addRemoveRuleEventListener(RemoveRuleEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addTapeInitializationEventListener(TapeInitializationEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addStepEventListener(StepEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addRunEventListener(RunEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addStopEventListener(StopEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addSpeedChangeEventListener(SpeedChangeEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addOpenFileEventListener(OpenFileEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addNewFileEventListener(NewFileEventListener l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addSaveEventListener(SaveEventListener l) {
        // TODO Auto-generated method stub
        
    }

    // OUTILS
    private void createView() {
        mainPanel = new JPanel();

        menuBar = new JMenuBar();

        stopButton = new JButton("Stopper");
        startButton = new JButton("Lancer");
        removeRuleButton = new JButton("Retirer");
        addRuleButton = new JButton("Ajouter");
        stepButton = new JButton("Pas à pas");
        initButton = new JButton("Initialiser");

        addRuleSymbolTextField = new JTextField("Symbole");
        addRuleSymbolTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(addRuleSymbolTextField, "Symbole");
        addRuleStateTextField = new JTextField("Etat");
        addRuleStateTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(addRuleStateTextField, "Etat");
        addResuSymbolTextField = new JTextField("Symbole");
        addResuSymbolTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(addResuSymbolTextField, "Symbole");
        addResuStateTextField = new JTextField("Etat");
        addResuStateTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(addResuStateTextField, "Etat");
        addResuDirectionTextField = new JTextField("Direction");
        addResuDirectionTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(addResuDirectionTextField, "Direction");
        initialRubanTextField = new JTextField();
        initialRubanTextField.setPreferredSize(new Dimension(100, 25));
        removeRuleSymbolTextField = new JTextField("Symbole");
        removeRuleSymbolTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(removeRuleSymbolTextField, "Symbole");
        removeRuleStateTextField = new JTextField("Etat");
        removeRuleStateTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(removeRuleStateTextField, "Etat");
        removeResuSymbolTextField = new JTextField("Symbole");
        removeResuSymbolTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(removeResuSymbolTextField, "Symbole");
        removeResuStateTextField = new JTextField("Etat");
        removeResuStateTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(removeResuStateTextField, "Etat");
        removeResuDirectionTextField = new JTextField("Direction");
        removeResuDirectionTextField.setForeground(Color.GRAY);
        addAddAndRemoveRulesTextfieldEventListener(removeResuDirectionTextField, "Direction");

        speedSlider = new JSlider(0, 100, 20);

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        rulesTextArea = new JTextArea();
        rulesTextArea.setEditable(false);
        paneRulesTextArea = new JTextArea();
        paneRulesTextArea.setEditable(false);

        tape = new GraphicTape();
    }

    private void addAddAndRemoveRulesTextfieldEventListener(JTextField textField, String s) {
        textField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent arg0) {
                if (textField.getText().equals(s)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(s);
                }
            }

        }
        );
    }
    
}
