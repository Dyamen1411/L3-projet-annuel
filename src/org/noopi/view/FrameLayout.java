package org.noopi.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.VetoableChangeListener;

import org.noopi.utils.events.tape.TapeInitializationEvent;
import org.noopi.utils.events.tape.TapeMovedEvent;
import org.noopi.utils.events.view.NewFileEvent;
import org.noopi.utils.events.view.OpenFileEvent;
import org.noopi.utils.events.view.RunEvent;
import org.noopi.utils.events.view.SaveEvent;
import org.noopi.utils.events.view.SpeedChangeEvent;
import org.noopi.utils.events.view.StepEvent;
import org.noopi.utils.events.view.StopEvent;
import org.noopi.utils.listeners.tape.TapeInitializationEventListener;
import org.noopi.utils.listeners.tape.TapeMovedEventListener;
import org.noopi.utils.listeners.view.ElementAddedEventListener;
import org.noopi.utils.listeners.view.ElementRemovedEventListener;
import org.noopi.utils.listeners.view.InitialTapeSymbolWrittenEventListener;
import org.noopi.utils.listeners.view.NewFileEventListener;
import org.noopi.utils.listeners.view.OpenFileEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.SaveEventListener;
import org.noopi.utils.listeners.view.SpeedChangeEventListener;
import org.noopi.utils.listeners.view.StepEventListener;
import org.noopi.utils.listeners.view.StopEventListener;
import org.noopi.utils.listeners.view.TapeShiftEventListener;
import org.noopi.utils.listeners.view.TransitionModifiedEventListener;
import org.noopi.model.TransitionTableModel;
import org.noopi.model.tape.ITape;
import org.noopi.utils.IDatabase;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.Transition;
import org.noopi.view.components.GraphicTape;
import org.noopi.view.components.ModifiableList;
import org.noopi.view.components.TransitionEditorComponent;
import org.noopi.view.components.TransitionTable;
import org.noopi.view.components.model.DatabaseComboboxModel;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;
import java.util.EnumMap;

import java.awt.GridLayout;

public class FrameLayout implements IFrameLayout {

  //ATTRIBUTS

  private IDatabase<String, Symbol> symbols;
  private IDatabase<String, State> states;
  private ITape tapeModel;
  private ITape initialTapeModel;

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
  private JSlider speedSlider;
  private JList<JLabel> historyJList;
  private JList<JLabel> transitionsJList;
  private GraphicTape tape;
  private GraphicTape initialTape;
  private Map<Item, JMenuItem> menuItems;
  private TransitionEditorComponent addTransition;
  private TransitionEditorComponent removeTransition;
  private TransitionTableModel transitions;
  private TransitionTable transitionTable;
  private ModifiableList symbolList;
  private ModifiableList stateList;
  private JButton initialTapeLeft;
  private JButton initialTapeRight;
  private JComboBox<String> initialTapeSymbolSelector;
  private JComboBox<String> initialStateSelector;
  private JLabel currentState;
  private State initialState;

  //CONSTRUCTEURS

  public FrameLayout(
    IDatabase<String, Symbol> symbols,
    IDatabase<String, State> states,
    TransitionTableModel transitions,
    ITape tapeModel,
    ITape initialTapeModel
  ) {
    assert transitions != null;
    assert symbols != null;
    assert states != null;
    assert tapeModel != null;
    assert initialTapeModel != null;
    this.transitions = transitions;
    this.symbols = symbols;
    this.states = states;
    this.tapeModel = tapeModel;
    this.initialTapeModel = initialTapeModel;
    createView();
    placeComponent();
    createController();
    listenerList = new EventListenerList();
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
      menuItems.put(i, new JMenuItem(i.label()));
    }
    return menuItems;
  }

  public State getInitialState(){
    return initialState;
  }

  // COMMANDES

  @Override
  public void resetTransitions() {
    transitionsJList.removeAll();
  }

  @Override
  public void setMachineState(State s) {
    currentState.setText(s.toString());
  }

  @Override
  public void addTransition(Transition t) {
    assert t != null;
    transitionsJList.add(createJLabel(t));
  }

  @Override
  public void removeTransition(Transition t) {
    assert t != null;
    transitionsJList.remove(createJLabel(t));
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
  public boolean showConfirmDialog(String message) {
    throw new UnsupportedOperationException(
      "showConfirmDialog is not implemented yet."
    );
  }

  @Override
  public void showInformation(String message) {
    assert message != null;
    JOptionPane.showMessageDialog(
      mainPanel,
      message,
      "Information",
      JOptionPane.INFORMATION_MESSAGE
    );
  }

  @Override
  public void showError(String message) {
    throw new UnsupportedOperationException(
      "showError is not implemented yet."
    );
  }

  @Override
  public void addTransitionAddedEventListener(
    TransitionModifiedEventListener l
  ) {
    assert l != null;
    addTransition.addTransitionModifiedEventListener(l);
  }

  @Override
  public void addTransitionRemovedEventListener(
    TransitionModifiedEventListener l
  ) {
    assert l != null;
    removeTransition.addTransitionModifiedEventListener(l);
  }

  @Override
  public void addTapeInitializationEventListener(
    TapeInitializationEventListener l
  ) {
    assert l != null;
    listenerList.add(TapeInitializationEventListener.class, l);
  }

  @Override
  public void addInitialTapeShiftEventListener(TapeShiftEventListener l) {
    assert l != null;
    initialTapeLeft.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        l.onTapeShifted(MachineAction.TAPE_LEFT);
      }
    });
    initialTapeRight.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        l.onTapeShifted(MachineAction.TAPE_RIGHT);
      }
    });
  }

  public void addInitialTapeSymbolWrittenEventListener(
    InitialTapeSymbolWrittenEventListener l
  ) {
    initialTapeSymbolSelector.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        Symbol s = e.getItem() == null
          ? Symbol.DEFAULT
          : symbols.get((String) e.getItem());
        l.onTapeWritten(s);
      }
    });
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

  public void addSymbolRegisteredEventListener(ElementAddedEventListener l) {
    assert l != null;
    symbolList.addElementAddedEventListener(l);
  }

  public void addStateRegisteredEventListener(ElementAddedEventListener l) {
    assert l != null;
    stateList.addElementAddedEventListener(l);
  }

  public void addSymbolUnRegisteredEventListener(ElementRemovedEventListener l) {
    assert l != null;
    symbolList.addElementRemovedEventListener(l);
  }

  public void addStateUnRegisteredEventListener(ElementRemovedEventListener l) {
    assert l != null;
    stateList.addElementRemovedEventListener(l);
  }

  public void addSymbolRegisteredVetoableChangeListener(
    VetoableChangeListener l
  ) {
    assert l != null;
    symbolList.addElementAddedVetoableChangeListener(l);
  }

  public void addStateRegisteredVetoableChangeListener(
    VetoableChangeListener l
  ) {
    assert l != null;
    stateList.addElementAddedVetoableChangeListener(l);
  }

  public void addSymbolUnregisteredVetoableChangeListener(
    VetoableChangeListener l
  ) {
    assert l != null;
    symbolList.addElementRemovedVetoableChangeListener(l);
  }

  public void addStateUnregisteredVetoableChangeListener(
    VetoableChangeListener l
  ) {
    assert l != null;
    stateList.addElementRemovedVetoableChangeListener(l);
  }

  // OUTILS

  private void createView() {
    mainPanel = new JPanel(new BorderLayout());

    menuBar = new JMenuBar();

    stopButton = new JButton("Stopper");
    startButton = new JButton("Lancer");
    stepButton = new JButton("Pas à pas");
    initButton = new JButton("Initialiser");
    addTransition = new TransitionEditorComponent("Ajouter", symbols, states);
    removeTransition = new TransitionEditorComponent("Retirer", symbols, states);
    symbolList = new ModifiableList("Symboles", "Ajouter", "Retirer");
    stateList = new ModifiableList("Etats", "Ajouter", "Retirer");
    transitionTable = new TransitionTable(symbols, states, transitions);

    speedSlider = new JSlider(0, 100, 20);

    historyJList = new JList<JLabel>();
    transitionsJList = new JList<JLabel>();

    tape = new GraphicTape(tapeModel, false, 150);
    initialTape = new GraphicTape(initialTapeModel, true, 47);
    initialTapeLeft = new JButton("Gauche");
    initialTapeRight = new JButton("Droite");
    initialTapeSymbolSelector = new JComboBox<>(
      new DatabaseComboboxModel<>(symbols)
    );
    initialStateSelector = new JComboBox<>(
      new DatabaseComboboxModel<>(states)
    );
    currentState = new JLabel("");
    currentState.setForeground(Color.RED);
  }

  private void placeComponent() {
    setMenuBar();
    final Border border = BorderFactory.createLineBorder(Color.GRAY, 3);
    mainPanel.add(createGUI(border), BorderLayout.CENTER);
    mainPanel.add(createHistoryGUI(border), BorderLayout.EAST);
  }

  private JPanel createGUI(Border border) {
    JPanel gui = new JPanel(new GridLayout(0, 1));
    gui.add(createTransitionsGUI(border));
    gui.add(createMachineGUI(border));
    gui.add(createControlsGUI(border));
    return gui;
  }

  private JPanel createTransitionsGUI(Border border) {
    JPanel transitions = new JPanel();
    transitions.setBorder(BorderFactory.createTitledBorder(border, "REGLES"));

    JPanel symbolStateEditor = new JPanel();
    { //--
      symbolStateEditor.add(symbolList);
      symbolStateEditor.add(stateList);
    } //--
    transitions.add(symbolStateEditor);

    transitions.add(transitionTable);

    return transitions;
  }

  private JPanel createMachineGUI(Border border) {
    JPanel machine = new JPanel(new GridLayout(0, 1));
    machine.setBorder(BorderFactory.createTitledBorder(border, "REPRESENTATION GRAPHIQUE"));
    
    JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
    { //--
      p.add(tape);
    } //--
    machine.add(p);

    p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    { //--
      JLabel l = new JLabel("Etat courant de la machine : ");
      l.setForeground(Color.RED);
      p.add(l);
      p.add(currentState);
    } //--
    machine.add(p);
    return machine;
  }

  private JPanel createControlsGUI(Border border) {
    JPanel controls = new JPanel(new GridLayout(0, 1));
    controls.setBorder(BorderFactory.createTitledBorder(border, "EXECUTION"));

    JPanel q = new JPanel(new FlowLayout(FlowLayout.CENTER));
    q.add(new JLabel("Edition du ruban initial :"));
    controls.add(q);

    JPanel tapeControls = new JPanel(new GridLayout(1, 0));
    { //--
      JPanel s = new JPanel(new FlowLayout(FlowLayout.CENTER));
      { //--
        s.add(initialTapeLeft);
        s.add(initialTapeSymbolSelector);
        s.add(initialTapeRight);
      } //--
      tapeControls.add(s);
    } //--
    controls.add(tapeControls);
    
    JPanel s = new JPanel(new FlowLayout(FlowLayout.CENTER));
    { //--
      s.add(initialTape);
    } //--
    controls.add(s);

    JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
    { //--
      p.add(new JLabel("Entrez l'état initial de la machine -->"));
      p.add(initialStateSelector);
    } //--
    controls.add(p);  

    JPanel stateControls = new JPanel();
    stateControls.add(initButton);
    stateControls.add(stepButton);
    controls.add(stateControls);

    JPanel machineControls = new JPanel();
    machineControls.add(startButton);
    machineControls.add(stopButton);
    setSpeedSlider();
    machineControls.add(speedSlider);
    controls.add(machineControls);

    return controls;
  }

  private JScrollPane createHistoryGUI(Border border) {
    JScrollPane historyScrollPane = new JScrollPane(historyJList);
    historyScrollPane.setPreferredSize(new Dimension(300, 500));
    historyScrollPane.setBorder(
      BorderFactory.createTitledBorder(border, "HISTORIQUE")
    );
    return historyScrollPane;
  }

  private void createController() {

    initialStateSelector.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        setInitialState((State)e.getItem());
      }
      
    });

    speedSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        final double speedRatio
            = (double) speedSlider.getValue()
            / (double) speedSlider.getMaximum();
        fireSpeedChangeEvent(speedRatio);
      }
    });

    stepButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireStepEvent();
      }
    });

    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireRunEvent();
      }
    });

    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireStopEvent();
      }
    });

    menuItems.get(Item.NEW).addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireNewFileEvent();
      }
    });

    menuItems.get(Item.OPEN).addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireOpenFileEvent();
      }
    });

    menuItems.get(Item.SAVE).addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireSaveEvent();
      }
    });

    initButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        fireTapeInitializationEvent();
      }
      
    });

    initialTapeModel.addTapeMovedEventListener(new TapeMovedEventListener() {
      @Override
      public void onTapeMoved(TapeMovedEvent e) {
        Symbol read = initialTapeModel.readSymbol();
        initialTapeSymbolSelector.setSelectedItem(
          read == Symbol.DEFAULT ? null : read.toString()
        );
        // hack :/
        initialTapeModel.writeSymbol(read);
      }
    });
  }

  private void setInitialState(State s){
    initialState = s;
  }

  private void setMenuBar() {
    for (Menu m : Menu.STRUCT.keySet()) {
      JMenu menu = new JMenu(m.label());
      for (Item i : Menu.STRUCT.get(m)) {
        menu.add(
          i == null
          ? new JSeparator()
          : getMenuItemsMap().get(i));
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

  private JLabel createJLabel(Transition t) {
    return new JLabel(t.toString());
  }

  protected void fireTapeInitializationEvent() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TapeInitializationEventListener.class) {
        if (tapeInitializationEvent == null) {
          tapeInitializationEvent = new TapeInitializationEvent();
        }
        ((TapeInitializationEventListener) listeners[i + 1])
          .onTapeInitialized(tapeInitializationEvent);
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
        ((SpeedChangeEventListener) listeners[i + 1])
          .onSpeedChanged(speedChangeEvent);
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
