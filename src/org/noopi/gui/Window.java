package org.noopi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.noopi.model.tape.ITape;
import org.noopi.model.tape.Tape;
import org.noopi.utils.exceptions.DatabaseDuplicateException;
import org.noopi.utils.exceptions.DatabaseMissingEntryException;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.events.history.HistoryPopEvent;
import org.noopi.utils.events.history.HistoryPushEvent;
import org.noopi.utils.events.history.HistoryResetEvent;
import org.noopi.utils.events.view.ElementAddedEvent;
import org.noopi.utils.events.view.ElementRemovedEvent;
import org.noopi.utils.events.view.RunEvent;
import org.noopi.utils.events.view.SpeedChangeEvent;
import org.noopi.utils.events.view.StepEvent;
import org.noopi.utils.events.view.StopEvent;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.StateDatabase;
import org.noopi.utils.Symbol;
import org.noopi.utils.SymbolDatabase;
import org.noopi.utils.Transition;
import org.noopi.utils.events.tape.TapeInitializationEvent;
import org.noopi.utils.listeners.tape.TapeInitializationEventListener;
import org.noopi.utils.listeners.view.ActiveMachineListener;
import org.noopi.utils.listeners.view.ElementAddedEventListener;
import org.noopi.utils.listeners.view.ElementRemovedEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;
import org.noopi.utils.listeners.history.HistoryPopEventListener;
import org.noopi.utils.listeners.history.HistoryPushEventListener;
import org.noopi.utils.listeners.history.HistoryResetEventListener;
import org.noopi.utils.listeners.view.SpeedChangeEventListener;
import org.noopi.utils.listeners.view.StepEventListener;
import org.noopi.utils.listeners.view.StopEventListener;
import org.noopi.utils.listeners.view.InitialTapeSymbolWrittenEventListener;
import org.noopi.utils.listeners.view.MachineInitialStateChangedEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.TapeShiftEventListener;
import org.noopi.model.TransitionTableModel;
import org.noopi.model.history.ITransitionHistory;
import org.noopi.model.history.TransitionHistory;
import org.noopi.model.machine.ITuringMachine;
import org.noopi.model.machine.TuringMachine;
import org.noopi.view.FrameLayout;
import org.noopi.view.IFrameLayout;

public final class Window {

  // ATTRIBUTES

  private final int SECOND_CONV = 100;

  // Model
  private ITuringMachine machine;
  private State initialState;
  private ITape tape;
  private ITape initialTape;
  private ITransitionHistory history;

  private SymbolDatabase symbols;
  private StateDatabase states;
  private TransitionTableModel transitions;
  private Timer machineTimer;

  // View
  private JFrame frame;
  private IFrameLayout layout;

  public Window() {
    createModel();
    createView();
    placeComponents();
    createController();
  }

  public void display() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.pack();
  }

  private void debug() {
    try {
      // Three state busy beaver
      State state_a = states.registerEntry("a");
      State state_b = states.registerEntry("b");
      State state_c = states.registerEntry("c");
      Symbol symbol_0 = symbols.registerEntry("0");
      Symbol symbol_1 = symbols.registerEntry("1");
      transitions.update(new Transition(state_a, symbol_0, MachineAction.TAPE_LEFT   , state_b, symbol_1));
      transitions.update(new Transition(state_b, symbol_0, MachineAction.TAPE_LEFT   , state_c, symbol_0));
      transitions.update(new Transition(state_c, symbol_0, MachineAction.TAPE_RIGHT  , state_c, symbol_1));
      transitions.update(new Transition(state_a, symbol_1, MachineAction.MACHINE_STOP, state_a, symbol_1));
      transitions.update(new Transition(state_b, symbol_1, MachineAction.TAPE_LEFT   , state_b, symbol_1));
      transitions.update(new Transition(state_c, symbol_1, MachineAction.TAPE_RIGHT  , state_a, symbol_1));
      for (int i = 0; i < 8; ++i) {
        initialTape.writeSymbol(symbol_0);
        initialTape.shift(MachineAction.TAPE_RIGHT);
      }
      for (int i = 0; i < 6; ++i) {
        initialTape.shift(MachineAction.TAPE_LEFT);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void createModel() {
    symbols = new SymbolDatabase();
    states = new StateDatabase();
    transitions = new TransitionTableModel(symbols, states);
    tape = new Tape();
    initialTape = new Tape();    
    machineTimer = new Timer(0, new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        stepMachine();
        if (machine.isDone()) {
          machineTimer.stop();
        }
      }
    });
    tape = new Tape();
    machine = new TuringMachine(transitions);
    history = new TransitionHistory();
    debug();
  }

  private void createView() {
    frame = new JFrame();
    layout = new FrameLayout(transitions, tape, initialTape);
  }

  private void placeComponents() {
    frame.setContentPane(layout.getView());
    frame.setJMenuBar(layout.getMenuBar());
  }

  private void createSymbolController() {
    layout.addSymbolRegisteredEventListener(new ElementAddedEventListener() {
      @Override
      public void onElementAdded(ElementAddedEvent e) {
        try {
          symbols.registerEntry(e.getElement());
        } catch (DatabaseDuplicateException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });

    layout.addSymbolUnRegisteredEventListener(new ElementRemovedEventListener() {
      @Override
      public void onElementRemoved(ElementRemovedEvent e) {
        try {
          symbols.unregisterEntry(e.getElement());
        } catch (DatabaseMissingEntryException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });

    layout.addSymbolRegisteredVetoableChangeListener(
      new VetoableChangeListener() {
        @Override
        public void vetoableChange(PropertyChangeEvent e)
          throws PropertyVetoException
        {
          String newValue = (String) e.getNewValue();
          if (symbols.contains(newValue)) {
            throw new PropertyVetoException("Symbol already contained", e);
          }
        }
      }
    );

    layout.addSymbolUnregisteredVetoableChangeListener(
      new VetoableChangeListener() {
        @Override
        public void vetoableChange(PropertyChangeEvent evt)
          throws PropertyVetoException
        {
          if (!symbols.contains((String) evt.getOldValue())) {
            throw new PropertyVetoException("Unknown symbol", evt);
          }
        }
      }
    );
  }

  private void createStateController() {
    layout.addStateRegisteredEventListener(new ElementAddedEventListener() {
      @Override
      public void onElementAdded(ElementAddedEvent e) {
        try {
          states.registerEntry(e.getElement());
        } catch (DatabaseDuplicateException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });
    
    layout.addStateUnRegisteredEventListener(new ElementRemovedEventListener() {
      @Override
      public void onElementRemoved(ElementRemovedEvent e) {
        try {
          states.unregisterEntry(e.getElement());
        } catch (DatabaseMissingEntryException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });

    layout.addStateRegisteredVetoableChangeListener(
      new VetoableChangeListener() {
        @Override
        public void vetoableChange(PropertyChangeEvent e)
          throws PropertyVetoException
        {
          String newValue = (String) e.getNewValue();
          if (states.contains(newValue)) {
            throw new PropertyVetoException("State already contained", e);
          }
        }
      }
    );

    layout.addStateUnregisteredVetoableChangeListener(
      new VetoableChangeListener() {
        @Override
        public void vetoableChange(PropertyChangeEvent evt)
          throws PropertyVetoException
        {
          if (!states.contains((String) evt.getOldValue())) {
            throw new PropertyVetoException("Unknown State", evt);
          }
        }
      }
    );
  }

  private void createInitialTapeController() {
    layout.addInitialTapeShiftEventListener(new TapeShiftEventListener() {
      @Override
      public void onTapeShifted(MachineAction a) {
        initialTape.shift(a);
        // initialTape.getSlice(9);
        // switch (a) {
        //   case TAPE_LEFT: layout.shiftInitialTapeLeft(); break;
        //   case TAPE_RIGHT: layout.shiftInitialTapeRight(); break;
        //   default: /* error, should never happen */ break;
        // }
      }
    });

    layout.addInitialTapeSymbolWrittenEventListener(
      new InitialTapeSymbolWrittenEventListener() {
        @Override
        public void onTapeWritten(Symbol s) {
          if (s == null) {
            return;
          }
          initialTape.writeSymbol(s);
          // layout.setSymbolOnInitialTape(s);
        }
      }
    );

  }

  private void createTapeController() {
    layout.addTapeInitializationEventListener(
      new TapeInitializationEventListener() {
        @Override
        public void onTapeInitialized(TapeInitializationEvent e) {
          tape.from(initialTape);
        }
    });

    symbols.addDatabaseUnregisterEventListener(
      new DatabaseUnregisterEventListener<Symbol>() {
        @Override
        public void onUnregisterEvent(DatabaseUnregisterEvent<Symbol> e) {
          initialTape.removeAllOccurencesOfSymbol(e.getValue());
          tape.removeAllOccurencesOfSymbol(e.getValue());
        }
      }
    );

  }

  private void createMachineController() {
    layout.addSpeedChangeEventListener(new SpeedChangeEventListener() {
      @Override
      public void onSpeedChanged(SpeedChangeEvent e) {
        final double ratio = e.getSpeed();
        final int integerRatio = (int) (ratio * 100);
        final int delay = (100 - integerRatio) * SECOND_CONV;
        machineTimer.setDelay(delay);
      }
    });

    layout.addStepEventListener(new StepEventListener() {
      @Override
      public void onStep(StepEvent e) {
        stepMachine();
      }
    });

    layout.addActiveMachineListener(new ActiveMachineListener() {
      @Override
      public void onActiveStateChange(boolean active) {
        if (active) {
          tape.from(initialTape);
          machine.reset(initialState);
          layout.setMachineState(initialState);
        } else {
          layout.setMachineState(State.DEFAULT); // ?
        }
      }
    });

    layout.addMachineInitialStateChangedEventListener(
      new MachineInitialStateChangedEventListener() {
        @Override
        public void onInitialStateChanged(State state) {
          initialState = state;
        }
      }
    );

    layout.addRunEventListener(new RunEventListener() {
      @Override
      public void onRun(RunEvent e) {
        if (!machineTimer.isRunning()) machineTimer.start();
      }
    });

    layout.addStopEventListener(new StopEventListener() {
      @Override
      public void onStop(StopEvent e) {
        if (machineTimer.isRunning()) machineTimer.stop();
      }
    });
  }

  private void createHistoryController() {
    history.addHistoryPushEventListener(new HistoryPushEventListener() {

      @Override
      public void onHistoryPush(HistoryPushEvent e) {
        // TODO: fix
        // layout.addRule(null);
      }
    });

    history.addHistoryPopEventListener(new HistoryPopEventListener() {

      @Override
      public void onHistoryPop(HistoryPopEvent e) {
        // TODO: fix
        // layout.removeRule(null);
      }
      
    });

    history.addHistoryResetEventListener(new HistoryResetEventListener() {

      @Override
      public void onHistoryReset(HistoryResetEvent e) {
        // layout.resetRules();
      }
      
    });
  }

  private void createController() {
    createSymbolController();
    createStateController();
    createInitialTapeController();
    createTapeController();
    createMachineController();
    createHistoryController();
  }

  private void stepMachine() {
    Symbol s = tape.readSymbol();
    Transition.Right result = machine.step(s);
    tape.writeSymbol(result.getSymbol());
    switch (result.getMachineAction()) {
      case MACHINE_STOP: layout.showInformation("La machine s'arrete"); break;
      case TAPE_LEFT: tape.shift(MachineAction.TAPE_RIGHT); break;
      case TAPE_RIGHT: tape.shift(MachineAction.TAPE_LEFT); break;
    }
    layout.setMachineState(result.getState());
  }

}
