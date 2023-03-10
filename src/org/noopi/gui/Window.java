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
import org.noopi.utils.events.history.HistoryPopEvent;
import org.noopi.utils.events.history.HistoryPushEvent;
import org.noopi.utils.events.history.HistoryResetEvent;
import org.noopi.utils.events.tape.TapeResetEvent;
import org.noopi.utils.events.view.ElementAddedEvent;
import org.noopi.utils.events.view.ElementRemovedEvent;
import org.noopi.utils.events.view.NewFileEvent;
import org.noopi.utils.events.view.OpenFileEvent;
import org.noopi.utils.events.view.RunEvent;
import org.noopi.utils.events.view.SaveEvent;
import org.noopi.utils.events.view.SpeedChangeEvent;
import org.noopi.utils.events.view.StepEvent;
import org.noopi.utils.events.view.StopEvent;
import org.noopi.utils.MachineAction;
import org.noopi.utils.StateDatabase;
import org.noopi.utils.Symbol;
import org.noopi.utils.SymbolDatabase;
import org.noopi.utils.events.tape.TapeInitializationEvent;
import org.noopi.utils.listeners.tape.TapeInitializationEventListener;
import org.noopi.utils.listeners.view.ElementAddedEventListener;
import org.noopi.utils.listeners.view.ElementRemovedEventListener;
import org.noopi.utils.listeners.history.HistoryPopEventListener;
import org.noopi.utils.listeners.history.HistoryPushEventListener;
import org.noopi.utils.listeners.history.HistoryResetEventListener;
import org.noopi.utils.listeners.tape.TapeResetEventListener;
import org.noopi.utils.listeners.view.NewFileEventListener;
import org.noopi.utils.listeners.view.OpenFileEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.SaveEventListener;
import org.noopi.utils.listeners.view.SpeedChangeEventListener;
import org.noopi.utils.listeners.view.StepEventListener;
import org.noopi.utils.listeners.view.StopEventListener;
import org.noopi.utils.listeners.view.InitialTapeSymbolWrittenEventListener;
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
    refreshView();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.pack();
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
        machine.step(tape.readSymbol());
      }
    });
    tape = new Tape();
    machine = new TuringMachine(transitions);
    history = new TransitionHistory();
  }

  private void createView() {
    frame = new JFrame();
    layout = new FrameLayout(transitions, tape, initialTape);
  }

  private void placeComponents() {
    frame.setContentPane(layout.getView());
    frame.setJMenuBar(layout.getMenuBar());
  }

  private void createController() {

    // TAPE

    tape.addTapeResetEventListener(new TapeResetEventListener() {
      @Override
      public void onTapeReset(TapeResetEvent e) {
        // TODO: fix
        tape.reset(null);
      }
    });

    // HISTORY

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

    // LISTENERS ON VIEW

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
          // Should mever happen
          e1.printStackTrace();
        }
      }
    });

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
          // Should mever happen
          e1.printStackTrace();
        }
      }
    });

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
          initialTape.writeSymbol(s);
          // layout.setSymbolOnInitialTape(s);
        }
      }
    );

    layout.addTapeInitializationEventListener(
      new TapeInitializationEventListener() {
        @Override
        public void onTapeInitialized(TapeInitializationEvent e) {
          tape.from(initialTape);
        }
    });

    layout.addRunEventListener(new RunEventListener() {

      @Override
      public void onRun(RunEvent e) {
        machineTimer.start();
      }
      
    });

    layout.addStepEventListener(new StepEventListener() {
      @Override
      public void onStep(StepEvent e) {
        // TODO: fix
        machine.step(tape.readSymbol());
      }
    });

    layout.addStopEventListener(new StopEventListener() {

      @Override
      public void onStop(StopEvent e) {
        machineTimer.stop();
      }
      
    });

    layout.addSpeedChangeEventListener(new SpeedChangeEventListener() {

      @Override
      public void onSpeedChanged(SpeedChangeEvent e) {
        machineTimer.setDelay((((int)(-e.getSpeed() * 100)) + 100) * SECOND_CONV);
      }
      
    });

    layout.addNewFileEventListener(new NewFileEventListener() {

      @Override
      public void onNewFile(NewFileEvent e) {
        // TODO: A voir comment on s'organise
      }
      
    });

    layout.addOpenFileEventListener(new OpenFileEventListener() {

      @Override
      public void onFileOpened(OpenFileEvent e) {
        // TODO: A voir comment on s'organise
      }
      
    });

    layout.addSaveEventListener(new SaveEventListener() {

      @Override
      public void onSave(SaveEvent e) {
        // TODO: A voir comment on sauvegarde
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

    // LISTENERS ON MACHINE
  }

  private void refreshView() {

  }
}
