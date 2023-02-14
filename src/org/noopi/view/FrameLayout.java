package org.noopi.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.GridLayout;

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
        mainPanel = new JPanel(new BorderLayout());

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

    private void placeComponent() {
        setMenuBar();
        {//--
            JPanel q = new JPanel();
            Border rulesBorder = BorderFactory.createLineBorder(Color.GRAY, 3);
            q.setBorder(BorderFactory.createTitledBorder(rulesBorder, "REGLES"));
            {//--
                JScrollPane rulePane = new JScrollPane(rulesTextArea);
                rulePane.setPreferredSize(new Dimension(300, 175));
                Border rulesBorderPane = BorderFactory.createLineBorder(Color.GRAY);
                rulePane.setBorder(BorderFactory.createTitledBorder(rulesBorderPane, "Cliquez pour aggrandir"));
                q.add(rulePane);
                JPanel r = new JPanel(new GridLayout(2, 5));
                {//--
                    r.add(addRuleButton);
                    r.add(new JLabel("          :"));
                    r.add(addRuleSymbolTextField);
                    r.add(addRuleStateTextField);
                    r.add(new JLabel("       =>"));
                    r.add(addResuSymbolTextField);
                    r.add(addResuStateTextField);
                    r.add(addResuDirectionTextField);
                    r.add(removeRuleButton);
                    r.add(new JLabel("          :"));
                    r.add(removeRuleSymbolTextField);
                    r.add(removeRuleStateTextField);
                    r.add(new JLabel("       =>"));
                    r.add(removeResuSymbolTextField);
                    r.add(removeResuStateTextField);
                    r.add(removeResuDirectionTextField);
                }//--
                q.add(r);
            }//--
            mainPanel.add(q);
            q = new JPanel();
            Border tapeBorder = BorderFactory.createLineBorder(Color.GRAY, 3);
            q.setBorder(BorderFactory.createTitledBorder(tapeBorder, "RUBAN"));
            {//--
                q.add(tape);
            }//--
            mainPanel.add(q);
            q = new JPanel(new GridLayout(0, 1));
            Border execBorder = BorderFactory.createLineBorder(Color.GRAY, 3);
            q.setBorder(BorderFactory.createTitledBorder(execBorder, "EXECUTION"));
            {//--
                JPanel r = new JPanel();
                {//--
                    r.add(new JLabel("Ruban initial :"));
                    r.add(initialRubanTextField);
                }//--
                q.add(r);
                r = new JPanel();
                {//--
                    r.add(initButton);
                    r.add(stepButton);
                }//--
                q.add(r);
                r = new JPanel();
                {//--
                    r.add(startButton);
                    r.add(stopButton);
                    setSpeedSlider();
                    r.add(speedSlider);
                }//--
                q.add(r);
            }//--
            mainPanel.add(q);
        }//--
        JScrollPane historyScrollPane = new JScrollPane(historyTextArea);
        historyScrollPane.setPreferredSize(new Dimension(300, 500));
        Border historyBorder = BorderFactory.createLineBorder(Color.GRAY, 3);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder(historyBorder, "HISTORIQUE"));
        mainPanel.add(historyScrollPane, BorderLayout.EAST);
    }
    
}
