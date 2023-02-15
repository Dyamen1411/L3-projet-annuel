package org.noopi.view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.LinkedList;
import java.util.List;
import java.awt.Color;

public class AddAndRemoveRulesComponent extends JComponent {

    // ATTRIBUTS

    private JPanel addAndRemoveRulesPanel;
    private JButton removeRuleButton;
    private JButton addRuleButton;
    private JTextField addRuleSymbolTextField;
    private JTextField addRuleStateTextField;
    private JTextField addResuSymbolTextField;
    private JTextField addResuStateTextField;
    private JTextField addResuDirectionTextField;
    private JTextField removeRuleSymbolTextField;
    private JTextField removeRuleStateTextField;
    private JTextField removeResuSymbolTextField;
    private JTextField removeResuStateTextField;
    private JTextField removeResuDirectionTextField;
    private List<JTextField> textFieldList;

    // Constructeurs

    public AddAndRemoveRulesComponent() {
        createComponents();
        setPanel();
    }

    // REQUETES

    public JPanel getAddAndRemoveRulesPanel() {
        return addAndRemoveRulesPanel;
    }

    // Outils

    private void setPanel() {
        addAndRemoveRulesPanel = new JPanel(new GridLayout(2, 5));
        {
            addAndRemoveRulesPanel.add(addRuleButton);
            addAndRemoveRulesPanel.add(new JLabel("          :"));
            addAndRemoveRulesPanel.add(addRuleSymbolTextField);
            addAndRemoveRulesPanel.add(addRuleStateTextField);
            addAndRemoveRulesPanel.add(new JLabel("       =>"));
            addAndRemoveRulesPanel.add(addResuSymbolTextField);
            addAndRemoveRulesPanel.add(addResuStateTextField);
            addAndRemoveRulesPanel.add(addResuDirectionTextField);
            addAndRemoveRulesPanel.add(removeRuleButton);
            addAndRemoveRulesPanel.add(new JLabel("          :"));
            addAndRemoveRulesPanel.add(removeRuleSymbolTextField);
            addAndRemoveRulesPanel.add(removeRuleStateTextField);
            addAndRemoveRulesPanel.add(new JLabel("       =>"));
            addAndRemoveRulesPanel.add(removeResuSymbolTextField);
            addAndRemoveRulesPanel.add(removeResuStateTextField);
            addAndRemoveRulesPanel.add(removeResuDirectionTextField);
        }
    }

    private void createComponents() {
        removeRuleButton = new JButton("Retirer");
        addRuleButton = new JButton("Ajouter");
        textFieldList = new LinkedList<JTextField>();
        addRuleSymbolTextField = new JTextField("Symbole");
        textFieldList.add(addRuleSymbolTextField);
        addRuleStateTextField = new JTextField("Etat");
        textFieldList.add(addRuleStateTextField);
        addResuSymbolTextField = new JTextField("Symbole");
        textFieldList.add(addResuSymbolTextField);
        addResuStateTextField = new JTextField("Etat");
        textFieldList.add(addResuStateTextField);
        addResuDirectionTextField = new JTextField("Direction");
        textFieldList.add(addResuDirectionTextField);
        removeRuleSymbolTextField = new JTextField("Symbole");
        textFieldList.add(removeRuleSymbolTextField);
        removeRuleStateTextField = new JTextField("Etat");
        textFieldList.add(removeRuleStateTextField);
        removeResuSymbolTextField = new JTextField("Symbole");
        textFieldList.add(removeResuSymbolTextField);
        removeResuStateTextField = new JTextField("Etat");
        textFieldList.add(removeResuStateTextField);
        removeResuDirectionTextField = new JTextField("Direction");
        textFieldList.add(removeResuDirectionTextField);
        setTextField();
    }

    private void setTextField() {
        for (JTextField tf : textFieldList) {
            tf.setForeground(Color.GRAY);
            addAddAndRemoveRulesTextfieldEventListener(tf, tf.getText());
        }
    }

    private void addAddAndRemoveRulesTextfieldEventListener(JTextField textField, String s) {
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
            @Override
            public void focusLost(FocusEvent arg0) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(s);
                }
            }
        });
    }
}