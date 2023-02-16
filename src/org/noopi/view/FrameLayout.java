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
import javax.swing.event.EventListenerList;

import org.noopi.utils.listeners.tape.TapeInitializationEventListener;
import org.noopi.utils.listeners.view.AddRuleEventListener;
import org.noopi.utils.listeners.view.NewFileEventListener;
import org.noopi.utils.listeners.view.OpenFileEventListener;
import org.noopi.utils.listeners.view.RemoveRuleEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.SaveEventListener;
import org.noopi.utils.listeners.view.SpeedChangeEventListener;
import org.noopi.utils.listeners.view.StepEventListener;
import org.noopi.utils.listeners.view.StopEventListener;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.lang.Thread.State;
import java.util.Map;
import java.util.EnumMap;

import java.awt.GridLayout;

public class FrameLayout implements IFrameLayout {

    //ATTRIBUTS

    private EventListenerList listenerList;

    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JButton stopButton;
    private JButton startButton;
    private JButton stepButton;
    private JButton initButton;
    private JTextField initialRubanTextField;
    private JSlider speedSlider;
    private JTextArea historyTextArea;
    private JTextArea rulesTextArea;
    private JTextArea paneRulesTextArea;
    private GraphicTape tape;
    private Map<Item, JMenuItem> menuItems;
    private JFrame rulesFrame;
    private AddAndRemoveRulesComponent addAndRemoveRulesComponent;

    //CONSTRUCTEURS

    public FrameLayout() {
        listenerList = new EventListenerList();

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
    public void resetRules() {
        rulesTextArea.setText(null);
    }

    @Override
    public void popHistory() {
    }

    @Override
    public void setSymbolOnTape(Symbol s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMachineState(State s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addRule(Transition t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeRule(Transition t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushHistory(Transition t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addAddRuleEventListener(AddRuleEventListener l) {
        assert l != null;
        listenerList.add(AddRuleEventListener.class, l);
    }

    @Override
    public void addRemoveRuleEventListener(RemoveRuleEventListener l) {
        assert l != null;
        listenerList.add(RemoveRuleEventListener.class, l);
    }

    @Override
    public void addTapeInitializationEventListener(TapeInitializationEventListener l) {
        assert l != null;
        listenerList.add(TapeInitializationEventListener.class, l);
    }

    @Override
    public void addStepEventListener(StepEventListener l) {
        assert l != null;
        listenerList.add(StepEventListener.class, l);
    }

    @Override
    public void addRunEventListener(RunEventListener l) {
        assert l != null;
        listenerList.add(RunEventListener.class, l);
    }

    @Override
    public void addStopEventListener(StopEventListener l) {
        assert l != null;
        listenerList.add(StopEventListener.class, l);
    }

    @Override
    public void addSpeedChangeEventListener(SpeedChangeEventListener l) {
        assert l != null;
        listenerList.add(SpeedChangeEventListener.class, l);
    }

    @Override
    public void addOpenFileEventListener(OpenFileEventListener l) {
        assert l != null;
        listenerList.add(OpenFileEventListener.class, l);
    }

    @Override
    public void addNewFileEventListener(NewFileEventListener l) {
        assert l != null;
        listenerList.add(NewFileEventListener.class, l);
    }

    @Override
    public void addSaveEventListener(SaveEventListener l) {
        assert l != null;
        listenerList.add(SaveEventListener.class, l);
    }

    // OUTILS

    private void createView() {
        mainPanel = new JPanel(new BorderLayout());

        menuBar = new JMenuBar();

        stopButton = new JButton("Stopper");
        startButton = new JButton("Lancer");
        stepButton = new JButton("Pas à pas");
        initButton = new JButton("Initialiser");
        addAndRemoveRulesComponent = new AddAndRemoveRulesComponent();
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
                q.add(addAndRemoveRulesComponent.getAddAndRemoveRulesPanel());
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
