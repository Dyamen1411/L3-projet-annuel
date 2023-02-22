package org.noopi.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.noopi.utils.events.view.AddRuleEvent;
import org.noopi.utils.events.view.RemoveRuleEvent;
import org.noopi.utils.listeners.view.AddRuleEventListener;
import org.noopi.utils.listeners.view.RemoveRuleEventListener;
import org.noopi.utils.machine.Direction;
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JComboBox<Direction> addDirection;
    private JTextField removeRuleSymbolTextField;
    private JTextField removeRuleStateTextField;
    private JTextField removeResuSymbolTextField;
    private JTextField removeResuStateTextField;
    private JComboBox<Direction> removeDirection;
    private List<JTextField> textFieldList;
    private EventListenerList listenerList;
    private AddRuleEvent addRuleEvent;
    private RemoveRuleEvent removeRuleEvent;

    // Constructeurs

    public AddAndRemoveRulesComponent() {
        createComponents();
        createController();
        setPanel();
        listenerList = new EventListenerList();
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
            addAndRemoveRulesPanel.add(addDirection);
            addAndRemoveRulesPanel.add(removeRuleButton);
            addAndRemoveRulesPanel.add(new JLabel("          :"));
            addAndRemoveRulesPanel.add(removeRuleSymbolTextField);
            addAndRemoveRulesPanel.add(removeRuleStateTextField);
            addAndRemoveRulesPanel.add(new JLabel("       =>"));
            addAndRemoveRulesPanel.add(removeResuSymbolTextField);
            addAndRemoveRulesPanel.add(removeResuStateTextField);
            addAndRemoveRulesPanel.add(removeDirection);
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
        addDirection = new JComboBox<Direction>(Direction.values());
        removeRuleSymbolTextField = new JTextField("Symbole");
        textFieldList.add(removeRuleSymbolTextField);
        removeRuleStateTextField = new JTextField("Etat");
        textFieldList.add(removeRuleStateTextField);
        removeResuSymbolTextField = new JTextField("Symbole");
        textFieldList.add(removeResuSymbolTextField);
        removeResuStateTextField = new JTextField("Etat");
        textFieldList.add(removeResuStateTextField);
        removeDirection = new JComboBox<Direction>(Direction.values());
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

    private void createController() {
        addRuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                State oldState = new State(addRuleStateTextField.getText());
                Symbol oldSymbol = new Symbol(addRuleSymbolTextField.getText());
                Direction newDirection = (Direction) addDirection.getSelectedItem();
                State newState = new State(addResuStateTextField.getText());
                Symbol newSymbol = new Symbol(addResuSymbolTextField.getText());
                fireAddRuleEvent(new Transition(oldState, oldSymbol, newDirection, newState, newSymbol));
            }
        });
        removeRuleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                State oldState = new State(removeRuleStateTextField.getText());
                Symbol oldSymbol = new Symbol(removeRuleSymbolTextField.getText());
                Direction newDirection = (Direction) removeDirection.getSelectedItem();
                State newState = new State(removeResuStateTextField.getText());
                Symbol newSymbol = new Symbol(removeResuSymbolTextField.getText());
                fireRemoveRuleEvent(new Transition(oldState, oldSymbol, newDirection, newState, newSymbol));
            }
        });
    }

    public void addAddRuleEventListener(AddRuleEventListener l) {
        assert l != null;
        listenerList.add(AddRuleEventListener.class, l);
        
    }

    public void addRemoveRuleEventListener(RemoveRuleEventListener l) {
        assert l != null;
        listenerList.add(RemoveRuleEventListener.class, l);
    }

    protected void fireAddRuleEvent(Transition t) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == AddRuleEventListener.class) {
                if (addRuleEvent == null) {
                    addRuleEvent = new AddRuleEvent(t);
                }
                ((AddRuleEventListener) listeners[i + 1]).onRuleAdded(addRuleEvent);
            }
        }
        addRuleEvent = null;
    }

    protected void fireRemoveRuleEvent(Transition t) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RemoveRuleEventListener.class) {
                if (removeRuleEvent == null) {
                    removeRuleEvent = new RemoveRuleEvent();
                }
                ((RemoveRuleEventListener) listeners[i + 1]).onRuleRemoved(removeRuleEvent);
            }
        }
    }
}