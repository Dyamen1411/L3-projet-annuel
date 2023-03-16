package org.noopi.gui;

import javax.swing.JFrame;

import org.noopi.model.tape.ITape;
import org.noopi.model.tape.Tape;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.StateDatabase;
import org.noopi.utils.Symbol;
import org.noopi.utils.SymbolDatabase;
import org.noopi.utils.events.tape.TapeInitializationEvent;
import org.noopi.utils.events.view.ElementAddedEvent;
import org.noopi.utils.events.view.ElementRemovedEvent;
import org.noopi.utils.exceptions.DatabaseDuplicateException;
import org.noopi.utils.exceptions.DatabaseMissingEntryException;
import org.noopi.utils.listeners.tape.TapeInitializationEventListener;
import org.noopi.utils.listeners.view.ElementAddedEventListener;
import org.noopi.utils.listeners.view.ElementRemovedEventListener;
import org.noopi.utils.listeners.view.InitialTapeSymbolWrittenEventListener;
import org.noopi.utils.listeners.view.MachineInitialStateChangedEventListener;
import org.noopi.utils.listeners.view.TapeShiftEventListener;
import org.noopi.model.TransitionTableModel;
import org.noopi.model.history.ITransitionHistory;
import org.noopi.model.machine.ITuringMachine;
import org.noopi.view.FrameLayout;
import org.noopi.view.IFrameLayout;

public final class Window {

  // Model
  private ITuringMachine machine;
  private ITape tape;
  private ITape initialTape;
  private ITransitionHistory history;
  private SymbolDatabase symbols;
  private StateDatabase states;
  private TransitionTableModel transitions;

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
  }

  private void createView() {
    frame = new JFrame("Machine de Turing");
    layout = new FrameLayout(
      symbols.toReadable(),
      states.toReadable(),
      transitions,
      tape,
      initialTape
    );
  }

  private void placeComponents() {
    frame.setContentPane(layout.getView());
    frame.setJMenuBar(layout.getMenuBar());
  }

  private void createController() {
    layout.addMachineInitialStateChangedEventListener(
      new MachineInitialStateChangedEventListener() {
        @Override
        public void onInitialStateChanged(State state) {
          // TODO: set initial state to <code>state</code>
        }
      }
    );

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
      }
    );
  }

  private void refreshView() {

  }
}
