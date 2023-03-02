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
import org.noopi.utils.exceptions.MachineDecidabilityExecption;
import org.noopi.utils.events.history.HistoryPopEvent;
import org.noopi.utils.events.history.HistoryPushEvent;
import org.noopi.utils.events.history.HistoryResetEvent;
import org.noopi.utils.events.tape.TapeMovedEvent;
import org.noopi.utils.events.tape.TapeResetEvent;
import org.noopi.utils.events.tape.TapeWriteEvent;
import org.noopi.utils.events.view.ElementAddedEvent;
import org.noopi.utils.events.view.ElementRemovedEvent;
import org.noopi.utils.events.view.NewFileEvent;
import org.noopi.utils.events.view.OpenFileEvent;
import org.noopi.utils.events.view.RunEvent;
import org.noopi.utils.events.view.SaveEvent;
import org.noopi.utils.events.view.SpeedChangeEvent;
import org.noopi.utils.events.view.StepEvent;
import org.noopi.utils.events.view.StopEvent;
import org.noopi.utils.listeners.history.HistoryPopEventListener;
import org.noopi.utils.listeners.history.HistoryPushEventListener;
import org.noopi.utils.listeners.history.HistoryResetEventListener;
import org.noopi.utils.listeners.tape.TapeMovedEventListener;
import org.noopi.utils.listeners.tape.TapeResetEventListener;
import org.noopi.utils.listeners.tape.TapeWriteEventListener;
import org.noopi.utils.listeners.view.ElementAddedEventListener;
import org.noopi.utils.listeners.view.ElementRemovedEventListener;
import org.noopi.utils.listeners.view.NewFileEventListener;
import org.noopi.utils.listeners.view.OpenFileEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.SaveEventListener;
import org.noopi.utils.listeners.view.SpeedChangeEventListener;
import org.noopi.utils.listeners.view.StepEventListener;
import org.noopi.utils.listeners.view.StopEventListener;
import org.noopi.utils.machine.StateDatabase;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.SymbolDatabase;
import org.noopi.model.history.ITransitionHistory;
import org.noopi.model.history.TransitionHistory;
import org.noopi.model.machine.ITuringMachine;
import org.noopi.model.machine.TuringMachine;
import org.noopi.view.FrameLayout;
import org.noopi.view.IFrameLayout;

public final class Window {

  // Model
  private ITuringMachine machine;
  private ITape tape;
  private ITransitionHistory history;
  private Timer timer;

  private SymbolDatabase symbolDatabase;
  private StateDatabase stateDatabase;

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
    timer = new Timer(0, new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: fix
        try {
          machine.step(null);
        } catch (MachineDecidabilityExecption ex) {

        }
      }

    });

    tape = new Tape(Symbol.EMPTY_SYMBOL);
    machine = new TuringMachine();
    history = new TransitionHistory();

    symbolDatabase = new SymbolDatabase();
    stateDatabase = new StateDatabase();
  }

  private void createView() {
    frame = new JFrame();
    layout = new FrameLayout();
    
  }

  private void placeComponents() {
    frame.setContentPane(layout.getView());
    frame.setJMenuBar(layout.getMenuBar());
  }

  private void createController() {

    // TAPE

    tape.addTapeMovedEventListener(new TapeMovedEventListener() {
      @Override
      public void onTapeMoved(TapeMovedEvent e) {
        switch(e.getDirection()) {
          case TAPE_LEFT:
            layout.shiftTapeLeft();
            break;
          case TAPE_RIGHT:
            layout.shiftTapeRight();
            break;
          case MACHINE_STOP:
            // What does it do ?
            break;
        }
      }
    });

    tape.addTapeResetEventListener(new TapeResetEventListener() {
      @Override
      public void onTapeReset(TapeResetEvent e) {
        // TODO: fix
        tape.reset(null);
      }
    });

    tape.addTapeWriteEventListener(new TapeWriteEventListener() {

      @Override
      public void onTapeWritten(TapeWriteEvent e) {
        layout.setSymbolOnTape(e.getSymbol());
      }
      
    });

    // HISTORY

    history.addHistoryPushEventListener(new HistoryPushEventListener() {

      @Override
      public void onHistoryPush(HistoryPushEvent e) {
        // TODO: fix
        layout.addRule(null);
      }
    });

    history.addHistoryPopEventListener(new HistoryPopEventListener() {

      @Override
      public void onHistoryPop(HistoryPopEvent e) {
        // TODO: fix
        layout.removeRule(null);
      }
      
    });

    history.addHistoryResetEventListener(new HistoryResetEventListener() {

      @Override
      public void onHistoryReset(HistoryResetEvent e) {
        layout.resetRules();
      }
      
    });

    // LISTENERS ON VIEW

    // TODO: fix
    // layout.addAddRuleEventListener(new AddRuleEventListener() {

    //   @Override
    //   public void onRuleAdded(AddRuleEvent e) {
    //     machine.addTransition(e.getRuleAdded());
    //   }
      
    // });

    // TODO: fix
    // layout.addRemoveRuleEventListener(new RemoveRuleEventListener() {

    //   @Override
    //   public void onRuleRemoved(RemoveRuleEvent e) {
    //     machine.removeTransition(e.getRemovedRule());
    //   }
      
    // });

    layout.addRunEventListener(new RunEventListener() {

      @Override
      public void onRun(RunEvent e) {
        timer.start();
      }
      
    });

    layout.addStepEventListener(new StepEventListener() {
      @Override
      public void onStep(StepEvent e) {
        // TODO: fix
        try {
          machine.step(null);
        } catch(MachineDecidabilityExecption ex) {}
      }
    });

    layout.addStopEventListener(new StopEventListener() {

      @Override
      public void onStop(StopEvent e) {
        timer.stop();
      }
      
    });

    layout.addSpeedChangeEventListener(new SpeedChangeEventListener() {

      @Override
      public void onSpeedChanged(SpeedChangeEvent e) {
        // TODO: Ajouter un Timer a la machine pour pouvoir regler sa vitesse
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
          if (symbolDatabase.contains(newValue)) {
            throw new PropertyVetoException("Symbol already contained", e);
          }
        }
      }
    );

    layout.addSymbolRegisteredEventListener(new ElementAddedEventListener() {
      @Override
      public void onElementAdded(ElementAddedEvent e) {
        try {
          symbolDatabase.registerEntry(e.getElement());
        } catch (DatabaseDuplicateException e1) {
          // Should never happen.
        }
      }
    });

    layout.addSymbolUnRegisteredVetoableChangeListener(
      new VetoableChangeListener() {
        @Override
        public void vetoableChange(PropertyChangeEvent evt)
          throws PropertyVetoException
        {
          if (!symbolDatabase.contains((String) evt.getOldValue())) {
            throw new PropertyVetoException("Unknown symbol", evt);
          }
        }
      }
    );

    layout.addSymbolUnRegisteredEventListener(
      new ElementRemovedEventListener() {
        @Override
        public void onElementRemoved(ElementRemovedEvent e) {
          try {
            symbolDatabase.unregisterEntry(e.getElement());
          } catch (DatabaseMissingEntryException ex) {
            // Should never happen.
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
          if (stateDatabase.contains(newValue)) {
            throw new PropertyVetoException("State already contained", e);
          }
        }
      }
    );

    layout.addStateRegisteredEventListener(new ElementAddedEventListener() {
      @Override
      public void onElementAdded(ElementAddedEvent e) {
        try {
          stateDatabase.registerEntry(e.getElement());
        } catch (DatabaseDuplicateException e1) {
          // Should never happen.
        }
      }
    });

    layout.addStateRUnegisteredVetoableChangeListener(
      new VetoableChangeListener() {
        @Override
        public void vetoableChange(PropertyChangeEvent evt)
          throws PropertyVetoException
        {
          if (!stateDatabase.contains((String) evt.getOldValue())) {
            throw new PropertyVetoException("Unknown State", evt);
          }
        }
      }
    );

    layout.addStateUnRegisteredEventListener(
      new ElementRemovedEventListener() {
        @Override
        public void onElementRemoved(ElementRemovedEvent e) {
          try {
            stateDatabase.unregisterEntry(e.getElement());
          } catch (DatabaseMissingEntryException ex) {
            // Should never happen.
          }
        }
      }
    );
  }

  private void refreshView() {

  }
}
