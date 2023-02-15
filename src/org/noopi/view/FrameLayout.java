package org.noopi.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.FocusEvent;
import java.awt.GridLayout;

import org.noopi.model.history.ITransitionHistory;
import org.noopi.model.history.ITransitionHistory.Action;
import org.noopi.model.machine.ITuringMachine;
import org.noopi.model.tape.ITape;
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
import org.noopi.utils.machine.Transition;
import org.w3c.dom.events.MouseEvent;

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
    private Map<Item, JMenuItem> menuItems;
    private JFrame rulesFrame;
    private List<JTextField> textFieldList;

    // Models

    private ITuringMachine machineModel;
    private ITransitionHistory historyModel;

    //CONSTRUCTEURS

    public FrameLayout() {
        createModel();
        createView();
        placeComponent();
        createController();
    }

    //REQUETES

    @Override
    public JComponent getView() {
        return mainPanel;
    }

    @Override
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public Map<Item, JMenuItem> getMenuItemsMap() {
        menuItems = new EnumMap<Item, JMenuItem>(Item.class);
        for(Item i : Item.values()) {
            JMenuItem item = new JMenuItem(i.label());
            menuItems.put(i, item);
        }
        return menuItems;
    }

    // COMMANDES

    @Override
    public void shiftTapeRight() {
        
    }

    @Override
    public void shiftTapeLeft() {
        
    }

    @Override
    public void setSymbolOnTape() {
        
    }

    @Override
    public void setMachineState() {

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
        rulesTextArea.setText(null);
    }

    @Override
    public void pushHistory() {
        
    }

    @Override
    public void popHistory() {
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
    private void createModel() {

    }
    private void createView() {
        mainPanel = new JPanel(new BorderLayout());

        menuBar = new JMenuBar();

        stopButton = new JButton("Stopper");
        startButton = new JButton("Lancer");
        removeRuleButton = new JButton("Retirer");
        addRuleButton = new JButton("Ajouter");
        stepButton = new JButton("Pas à pas");
        initButton = new JButton("Initialiser");
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
        initialRubanTextField = new JTextField();
        initialRubanTextField.setPreferredSize(new Dimension(100, 25));

        speedSlider = new JSlider(0, 100, 20);

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        rulesTextArea = new JTextArea();
        rulesTextArea.setEditable(false);
        paneRulesTextArea = new JTextArea();
        paneRulesTextArea.setEditable(false);

        tape = new GraphicTape();
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

    private void setMenuBar() {
        for (Menu m : Menu.STRUCT.keySet()) {
            JMenu menu = new JMenu(m.label());
            for (Item i : Menu.STRUCT.get(m)) {
                if (i == null) {
                    menu.add(new JSeparator());
                } else {
                    menu.add(getMenuItemsMap().get(i));
                }
            }
            menuBar.add(menu);
        }
    }

    private void setSpeedSlider() {
        speedSlider.setPaintTrack(true); 
        speedSlider.setPaintTicks(true); 
        speedSlider.setPaintLabels(true); 
        speedSlider.setMajorTickSpacing(20); 
        speedSlider.setMinorTickSpacing(5); 
    }

    private void createController() {
        rulesTextArea.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent arg0) {
                createNewRulesFrame();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent arg0) {
                // Nothing to do
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent arg0) {
                 // Nothing to do
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent arg0) {
                 // Nothing to do
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent arg0) {
                 // Nothing to do
            }
        });
    }

    private void createNewRulesFrame() {
        rulesFrame = new JFrame("Liste des règles");
        rulesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rulesFrame.setPreferredSize(new Dimension(200, 400));
        rulesFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        paneRulesTextArea.append(rulesTextArea.getText()); 
        JScrollPane newFrameRulesPane = new JScrollPane(paneRulesTextArea);
        rulesFrame.add(newFrameRulesPane);
        rulesFrame.pack();
        rulesFrame.setLocationRelativeTo(null);
        rulesFrame.setVisible(true);
    }
    
}
