package org.noopi.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import org.noopi.utils.events.tape.TapeInitializationEvent;
import org.noopi.utils.events.view.NewFileEvent;
import org.noopi.utils.events.view.OpenFileEvent;
import org.noopi.utils.events.view.RunEvent;
import org.noopi.utils.events.view.SaveEvent;
import org.noopi.utils.events.view.SpeedChangeEvent;
import org.noopi.utils.events.view.StepEvent;
import org.noopi.utils.events.view.StopEvent;
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
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import java.util.EnumMap;

import java.awt.GridLayout;

public class FrameLayout implements IFrameLayout {

    //ATTRIBUTS

    private EventListenerList listenerList;

    private TapeInitializationEvent tapeInitializationEvent;
    private StepEvent stepEvent;
    private RunEvent runEvent;
    private StopEvent stopEvent;
    private SpeedChangeEvent speedChangeEvent;
    private OpenFileEvent openFileEvent;
    private NewFileEvent newFileEvent;
    private SaveEvent saveEvent;

    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JButton stopButton;
    private JButton startButton;
    private JButton stepButton;
    private JButton initButton;
    private JTextField initialRubanTextField;
    private JSlider speedSlider;
    private JList<JLabel> historyJList;
    private JList<JLabel> rulesJList;
    private JList<JLabel> paneRulesTextArea;
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
        tape.shiftTapeRight();
    }

    @Override
    public void shiftTapeLeft() {
        tape.shiftTapeLeft();
    }

    @Override
    public void resetRules() {
        rulesJList.removeAll();
    }

    @Override
    public void setSymbolOnTape(Symbol s) {
        tape.setSymbol(s);
    }

    @Override
    public void setMachineState(State s) {
        tape.setState(s);
    }

    @Override
    public void addRule(Transition t) {
        assert t != null;
        rulesJList.add(createJLabel(t));
    }

    @Override
    public void removeRule(Transition t) {
        assert t != null;
        rulesJList.remove(createJLabel(t));
    }

    @Override
    public void pushHistory(Transition t) {
        assert t != null;
        historyJList.add(createJLabel(t), 0);
    }

    @Override
    public void popHistory() {
        historyJList.remove(0);
    }

    @Override
    public void addAddRuleEventListener(AddRuleEventListener l) {
        assert l != null;
        addAndRemoveRulesComponent.addAddRuleEventListener(l);
    }

    @Override
    public void addRemoveRuleEventListener(RemoveRuleEventListener l) {
        assert l != null;
        addAndRemoveRulesComponent.addRemoveRuleEventListener(l);
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

        historyJList = new JList<JLabel>();
        rulesJList = new JList<JLabel>();
        paneRulesTextArea = new JList<JLabel>();

        tape = new GraphicTape();
    }

    private void placeComponent() {
        setMenuBar();
        Border border = BorderFactory.createLineBorder(Color.GRAY, 3);
        JPanel q = new JPanel();
        q.setBorder(BorderFactory.createTitledBorder(border, "REGLES"));
        {//--
            JScrollPane rulePane = new JScrollPane(rulesJList);
            rulePane.setPreferredSize(new Dimension(300, 175));
            Border rulesBorderPane = BorderFactory.createLineBorder(Color.GRAY);
            rulePane.setBorder(BorderFactory.createTitledBorder(rulesBorderPane, "Cliquez pour aggrandir"));
            q.add(rulePane);
            q.add(addAndRemoveRulesComponent.getAddAndRemoveRulesPanel());
        }//--
        mainPanel.add(q);
        q = new JPanel();
        q.setBorder(BorderFactory.createTitledBorder(border, "RUBAN"));
        {//--
            q.add(tape);
        }//--
        mainPanel.add(q);
        q = new JPanel(new GridLayout(0, 1));
        q.setBorder(BorderFactory.createTitledBorder(border, "EXECUTION"));
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
        JScrollPane historyScrollPane = new JScrollPane(historyJList);
        historyScrollPane.setPreferredSize(new Dimension(300, 500));
        historyScrollPane.setBorder(BorderFactory.createTitledBorder(border, "HISTORIQUE"));
        mainPanel.add(historyScrollPane, BorderLayout.EAST);
    }

    private void createController() {
        rulesJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent arg0) {
                createNewRulesFrame();
            }
        });
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                fireSpeedChangeEvent((double) speedSlider.getValue() / (double) speedSlider.getMaximum());
            }
        });

        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fireStepEvent();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fireRunEvent();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fireStopEvent();
            }
        });

        menuItems.get(Item.NEW).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fireNewFileEvent();
            }
        });

        menuItems.get(Item.OPEN).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fireOpenFileEvent();
            }
        });

        menuItems.get(Item.SAVE).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fireSaveEvent();
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
        paneRulesTextArea = rulesJList;
        JScrollPane newFrameRulesPane = new JScrollPane(paneRulesTextArea);
        rulesFrame.add(newFrameRulesPane);
        rulesFrame.pack();
        rulesFrame.setLocationRelativeTo(null);
        rulesFrame.setVisible(true);
    }

    private JLabel createJLabel(Transition t) {
        return new JLabel("(" + t.getOldState() + ", " + t.getOldSymbol()
        + " => " + "(" + t.getNewState() + ", " + t.getNewSymbol() + ", " + t.getNewDirection() + ")");
    }

    protected void fireTapeInitializationEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TapeInitializationEventListener.class) {
                if (tapeInitializationEvent == null) {
                    tapeInitializationEvent = new TapeInitializationEvent();
                }
                ((TapeInitializationEventListener) listeners[i + 1]).onTapeInitialized(tapeInitializationEvent);
            }
        }
    }

    protected void fireStepEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == StepEventListener.class) {
                if (stepEvent == null) {
                    stepEvent = new StepEvent();
                }
                ((StepEventListener) listeners[i + 1]).onStep(stepEvent);
            }
        }
    }

    protected void fireRunEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RunEventListener.class) {
                if (runEvent == null) {
                    runEvent = new RunEvent();
                }
                ((RunEventListener) listeners[i + 1]).onRun(runEvent);
            }
        }
    }

    protected void fireStopEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == StopEventListener.class) {
                if (stopEvent == null) {
                    stopEvent = new StopEvent();
                }
                ((StopEventListener) listeners[i + 1]).onStop(stopEvent);
            }
        }
    }

    protected void fireSpeedChangeEvent(double v) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SpeedChangeEventListener.class) {
                if (speedChangeEvent == null) {
                    speedChangeEvent = new SpeedChangeEvent(v);
                }
                ((SpeedChangeEventListener) listeners[i + 1]).onSpeedChanged(speedChangeEvent);
            }
        }
        speedChangeEvent = null;
    }

    protected void fireOpenFileEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == OpenFileEventListener.class) {
                if (openFileEvent == null) {
                    openFileEvent = new OpenFileEvent();
                }
                ((OpenFileEventListener) listeners[i + 1]).onFileOpened(openFileEvent);
            }
        }
    }

    protected void fireNewFileEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == NewFileEventListener.class) {
                if (newFileEvent == null) {
                    newFileEvent = new NewFileEvent();
                }
                ((NewFileEventListener) listeners[i + 1]).onNewFile(newFileEvent);
            }
        }
    }

    protected void fireSaveEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SaveEventListener.class) {
                if (saveEvent == null) {
                    saveEvent = new SaveEvent();
                }
                ((SaveEventListener) listeners[i + 1]).onSave(saveEvent);
            }
        }
    }

}
