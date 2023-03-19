package org.noopi.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.noopi.utils.listeners.view.ActiveMachineListener;
import org.noopi.utils.listeners.view.ElementAddedEventListener;
import org.noopi.utils.listeners.view.ElementRemovedEventListener;
import org.noopi.utils.listeners.view.InitialTapeSymbolWrittenEventListener;
import org.noopi.utils.listeners.view.MachineInitialStateChangedEventListener;
import org.noopi.utils.listeners.view.NewFileEventListener;
import org.noopi.utils.listeners.view.OpenFileEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.SaveEventListener;
import org.noopi.utils.listeners.view.SpeedChangeEventListener;
import org.noopi.utils.listeners.view.StepEventListener;
import org.noopi.utils.listeners.view.StopEventListener;
import org.noopi.utils.listeners.view.TapeShiftEventListener;
import org.noopi.model.TransitionTableModel;
import org.noopi.model.tape.ITape;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.Transition;
import org.noopi.view.components.GBC;
import org.noopi.view.components.GraphicTape;
import org.noopi.view.components.ModifiableList;
import org.noopi.view.components.TransitionTable;
import org.noopi.view.components.model.DatabaseComboboxModel;
import org.noopi.view.components.model.GraphicArrow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.Map;
import java.util.EnumMap;

import java.awt.GridLayout;

public class FrameLayout implements IFrameLayout {

  //ATTRIBUTS

  private boolean started;

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
  private JSlider speedSlider;
  private JList<JLabel> historyJList;
  private JList<JLabel> transitionsJList;
  private GraphicTape tape;
  private GraphicTape initialTape;
  private Map<Item, JMenuItem> menuItems;
  private TransitionTableModel transitions;
  private TransitionTable transitionTable;
  private ModifiableList symbolList;
  private ModifiableList stateList;
  private JButton initialTapeLeft;
  private JButton initialTapeRight;
  private JComboBox<String> initialTapeSymbolSelector;
  private JComboBox<String> initialStateSelector;
  private JLabel currentState;
  private JCheckBox isActiveCheckBox;

  //CONSTRUCTEURS

  public FrameLayout(
    TransitionTableModel transitions,
    ITape tapeModel,
    ITape initialTapeModel
  ) {
    assert transitions != null;
    assert tapeModel != null;
    assert initialTapeModel != null;
    this.transitions = transitions;
    this.tapeModel = tapeModel;
    this.initialTapeModel = initialTapeModel;
    this.listenerList = new EventListenerList();
    started = false;
    createView();
    placeComponent();
    createController();
    refreshView();
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
  public void addMachineInitialStateChangedEventListener(
    MachineInitialStateChangedEventListener l
  ) {
    assert l != null;
    initialStateSelector.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        l.onInitialStateChanged(
          transitions.getStatesDatabase()
          .get((String) e.getItem())
        );
      }
    });
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
          : transitions.getSymbolDatabase().get((String) e.getItem());
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

  public void addActiveMachineListener(ActiveMachineListener l){
    assert l != null;
    isActiveCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        l.onActiveStateChange(isActiveCheckBox.isSelected());
      }
    });
  }

  // OUTILS

  private void refreshMachineActivationCheckBox() {
    isActiveCheckBox.setEnabled(
      isActiveCheckBox.isSelected()
      || initialStateSelector.getSelectedItem() != null
    );
  }

  private void refreshView() {
    refreshMachineActivationCheckBox();
    boolean active = isActiveCheckBox.isSelected();
    symbolList.setActive(!active);
    stateList.setActive(!active);
    transitionTable.setActive(!active);
    initialTapeLeft.setEnabled(!active);
    initialTapeRight.setEnabled(!active);
    initialStateSelector.setEnabled(!active);
    initialTapeSymbolSelector.setEnabled(!active);
    stepButton.setEnabled(active && !started);
    startButton.setEnabled(active && !started);
    stopButton.setEnabled(active && started);
  }

  private void createView() {
    mainPanel = new JPanel(new GridBagLayout());

    menuBar = new JMenuBar();
    stopButton = new JButton("Stopper");
    startButton = new JButton("Lancer");
    stepButton = new JButton("Pas à pas");
    symbolList = new ModifiableList("Symboles", "Ajouter", "Retirer");
    stateList = new ModifiableList("Etats", "Ajouter", "Retirer");
    transitionTable = new TransitionTable(transitions);
    isActiveCheckBox = new JCheckBox("Activer/Desactiver la machine");

    speedSlider = new JSlider(0, 100, 20);
    setSpeedSlider();
    setMenuBar();

    historyJList = new JList<JLabel>();
    transitionsJList = new JList<JLabel>();

    tape = new GraphicTape(tapeModel, false, 150);
    initialTape = new GraphicTape(initialTapeModel, true, 47);
    initialTapeLeft = new JButton("Gauche");
    initialTapeRight = new JButton("Droite");
    initialTapeSymbolSelector = new JComboBox<>(
      new DatabaseComboboxModel<>(transitions.getSymbolDatabase())
    );
    initialStateSelector = new JComboBox<>(
      new DatabaseComboboxModel<>(transitions.getStatesDatabase())
    );
    currentState = new JLabel("");
    currentState.setForeground(Color.RED);
  }

  private void placeComponent() {
    final Border border = BorderFactory.createLineBorder(Color.GRAY, 3);
    mainPanel.add(createTransitionsGUI(border), new GBC(0, 0, 1, 1).fill(GBC.BOTH).weight(3, 1));
    mainPanel.add(createMachineGUI(border), new GBC(0, 1, 1, 1).fill(GBC.BOTH).weight(0, 2));
    mainPanel.add(createControlsGUI(border), new GBC(0, 2, 1, 1).fill(GBC.BOTH).weight(0, 2));
    mainPanel.add(createHistoryGUI(border), new GBC(1, 0, 1, 3).fill(GBC.BOTH).weight(1, 0));
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
      JLabel l = new JLabel("Etat courant de la machine : ");
      l.setForeground(Color.RED);
      p.add(l);
      p.add(currentState);
    } //--
    machine.add(p);
    
    p = new JPanel(new FlowLayout(FlowLayout.CENTER));
    { //--
      p.add(tape);
    } //--
    machine.add(p);

    p = new JPanel(new FlowLayout(FlowLayout.CENTER));
    { //--
      p.add(new GraphicArrow());
    } //--
    machine.add(p);

    
    return machine;
  }

  private JPanel createControlsGUI(Border border) {

    GBC[] constraintsLeft = new GBC[] {
      // START_BUTTON
      new GBC(0, 1, 1, 1),
      // STOP BUTTON
      new GBC(1, 1, 1, 1),
      // STEP BUTTON
      new GBC(0, 2, 2, 1),
      // SLIDER
      new GBC(0, 3, 2, 1).fill(GBC.HORIZONTAL)
    };

    JComponent[] componentsLeft = new JComponent[] {
      // START BUTTON
      startButton,
      // STOP BUTTON
      stopButton,
      // STEP BUTTON
      stepButton,
      // SLIDER
      speedSlider
    };

    GBC[] constraintsRight = new GBC[]{
      // LABEL INITIAL STATE
      new GBC(0, 0, 2, 1),
      // INITIAL STATE SELECTOR
      new GBC(2, 0, 1, 1),
      // LEFT BUTTON
      new GBC(0, 2, 1, 1),
      // RIGHT BUTTON
      new GBC(2, 2, 1, 1),
      // SYMBOL SELECTOR
      new GBC(1, 2, 1, 1),
      // VOID LABELS
      new GBC(1, 1, 1, 1),
      new GBC(1, 3, 1, 1)
    };

    JComponent[] componentsRight = new JComponent[]{
      // LABEL INITIAL STATE
      new JLabel("Modifiez l'état initial ici -->"),
      // INITIAL STATE SELECTOR
      initialStateSelector,
      // LEFT BUTTON
      initialTapeLeft,
      // RIGHT BUTTON
      initialTapeRight,
      // SYMBOL SELECTOR
      initialTapeSymbolSelector,
      // VOID LABELS
      new JLabel(" "),
      new JLabel(" ")
    };

    JPanel controls = new JPanel(new GridLayout());
    controls.setBorder(BorderFactory.createTitledBorder(border, "EXECUTION"));

    JPanel p = new JPanel(new GridBagLayout());
    p.setBorder(BorderFactory.createTitledBorder(border, "Gestion de la machine"));
    { //--
      JPanel q = new JPanel(new GridBagLayout());
      { //--
        for(int i = 0; i < componentsLeft.length; i+= 1){
          q.add(componentsLeft[i], constraintsLeft[i]);
        }
      } //--
      p.add(isActiveCheckBox, new GBC(0, 0, 1, 1));
      p.add(q, new GBC(0, 1, 1, 1));
    } //--
    controls.add(p);

    p = new JPanel(new GridBagLayout());
    p.setBorder(BorderFactory.createTitledBorder(border, "Initialisation de la machine"));
    { //--
      JPanel q = new JPanel(new GridBagLayout());
      {
        for(int i = 0; i < componentsRight.length; i+= 1){
          q.add(componentsRight[i], constraintsRight[i]);
        }
      }
      p.add(q, new GBC(0, 0, 1, 1));
      p.add(initialTape, new GBC(0, 1, 1, 1));
    } //--
    controls.add(p);
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
        refreshMachineActivationCheckBox();
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
        started = true;
        refreshView();
        fireRunEvent();
      }
    });

    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        started = false;
        refreshView();
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

    isActiveCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        refreshView();
      }
    });

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
