package org.noopi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
import org.noopi.utils.events.view.NewFileEvent;
import org.noopi.utils.events.view.OpenFileEvent;
import org.noopi.utils.events.view.RunEvent;
import org.noopi.utils.events.view.SaveEvent;
import org.noopi.utils.events.view.SpeedChangeEvent;
import org.noopi.utils.events.view.StepEvent;
import org.noopi.utils.events.view.StopEvent;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.StateDatabase;
import org.noopi.utils.Symbol;
import org.noopi.utils.SymbolDatabase;
import org.noopi.utils.Transition;
import org.noopi.utils.Utils;
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
import org.noopi.utils.listeners.view.NewFileEventListener;
import org.noopi.utils.listeners.view.OpenFileEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.SaveEventListener;
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

  @SuppressWarnings("unused")
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
    // DEBUG
    // debug();
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
        String element = e.getElement();
        try {
          if (!symbols.contains(element))
            symbols.registerEntry(element);
          else
            // TODO: add accents
            layout.showError(
              "Le symbole \"" + element + "\" est deja entregistre !"
            );
        } catch (DatabaseDuplicateException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });

    layout.addSymbolUnRegisteredEventListener(new ElementRemovedEventListener() {
      @Override
      public void onElementRemoved(ElementRemovedEvent e) {
        String element = e.getElement();
        try {
          if (symbols.contains(element))
            symbols.unregisterEntry(e.getElement());
          else
            // TODO: add accents
            layout.showError(
              "Le symbole \"" + element + "\" n'a jamais ete enregistre !"
            );
        } catch (DatabaseMissingEntryException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });
  }

  private void createStateController() {
    layout.addStateRegisteredEventListener(new ElementAddedEventListener() {
      @Override
      public void onElementAdded(ElementAddedEvent e) {
        String element = e.getElement();
        try {
          if (!states.contains(element))
            states.registerEntry(element);
          else
            // TODO: add accents
            layout.showError(
              "L'etat \"" + element + "\" est deja entregistre !"
            );
        } catch (DatabaseDuplicateException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });
    
    layout.addStateUnRegisteredEventListener(new ElementRemovedEventListener() {
      @Override
      public void onElementRemoved(ElementRemovedEvent e) {
        String element = e.getElement();
        try {
          if (states.contains(element))
            states.unregisterEntry(e.getElement());
          else
            // TODO: add accents
            layout.showError(
              "L'etat \"" + element + "\" n'a jamais ete enregistre !"
            );
        } catch (DatabaseMissingEntryException e1) {
          // Should never happen
          e1.printStackTrace();
        }
      }
    });
  }

  private void createInitialTapeController() {
    layout.addInitialTapeShiftEventListener(new TapeShiftEventListener() {
      @Override
      public void onTapeShifted(MachineAction a) {
        initialTape.shift(a);
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
      }
    });

    history.addHistoryPopEventListener(new HistoryPopEventListener() {
      @Override
      public void onHistoryPop(HistoryPopEvent e) {
        // TODO: fix
      }
    });

    history.addHistoryResetEventListener(new HistoryResetEventListener() {
      @Override
      public void onHistoryReset(HistoryResetEvent e) {
        // TODO: fix
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

    layout.addOpenFileEventListener(new OpenFileEventListener() {
      @Override
      public void onFileOpened(OpenFileEvent e) {
        File f = layout.openFile(new File("."));
        if (f == null || !f.exists() || f.isDirectory()) {
          // TODO: accents
          layout.showError("Le fichier selectionne est incorrect !");
          return;
        }
        if (loadFile(f)) {
          layout.showError("Impossible d'ouvrir le fichier !");
          return;
        }
      }
    });

    layout.addSaveEventListener(new SaveEventListener() {
      @Override
      public void onSave(SaveEvent e) {
        File f = layout.selectFileToSave(new File("."));
        if (f == null || f.isDirectory()) {
          // TODO: accents
          layout.showError("Le fichier selectionne est incorrect !");
          return;
        }
        if (saveFile(f)) {
          layout.showError("Impossible d'ouvrir le fichier !");
          return;
        }
      }
    });

    layout.addNewFileEventListener(new NewFileEventListener() {
      @Override
      public void onNewFile(NewFileEvent e) {
        // TODO: accents
        if (
          (symbols.size() != 0 || states.size() != 0)
          && !layout.showConfirmDialog("Vous allez perdre votre travail.\nEtes vous sur ?")
        ) {
          return;
        }
        tape.reset();
        initialTape.reset();
        symbols.clear();
        states.clear();
      }
    });
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

  private boolean loadFile(File f) {
    symbols.clear();
    states.clear();
    tape.reset();
    initialTape.reset();
    initialState = null;
    try (
      DataInputStream dis = new DataInputStream(new FileInputStream(f))
    ) {
      String[] symbols = new String[dis.readInt()];
      String[] states = new String[dis.readInt()];
      Symbol[] actualSymbols = new Symbol[symbols.length];
      State[] actualStates = new State[states.length];

      // Symbols
      for (int i = 0; i < symbols.length; ++i) {
        String symbol = dis.readUTF();
        if (
          symbol == null
          || symbol.equals("")
          || this.symbols.contains(symbol)
        )
        { throw new Exception("duplicate symbol"); }
        symbols[i] = symbol;
        actualSymbols[i] = this.symbols.registerEntry(symbol);
      }

      // States
      for (int i = 0; i < states.length; ++i) {
        String state = dis.readUTF();
        if (
          state == null
          || state.equals("")
          || this.states.contains(state)
        )
        { throw new Exception("duplicate state"); }
        states[i] = state;
        actualStates[i] = this.states.registerEntry(state);
      }

      // Tape
      int tapeSize = dis.readInt();
      int tapeOffset = dis.readInt();
      System.out.println("tape offset : " + tapeOffset);

      for (int i = 0; i < tapeSize; ++i) {
        int pointer = dis.readInt();
        if ((pointer < 0 && pointer != -1) || pointer >= symbols.length) {
          throw new Exception("Unknown tape symbol : index " + pointer);
        }
        initialTape.writeSymbol(
          pointer == -1
          ? Symbol.DEFAULT
          : this.symbols.get(symbols[pointer])
        );
        initialTape.shift(MachineAction.TAPE_RIGHT);
      }
      for (int i = 0; i < tapeOffset; ++i) {
        initialTape.shift(MachineAction.TAPE_LEFT);
      }

      // Transition table
      for (int i = 0; i < symbols.length; i++) {
        for (int j = 0; j < states.length; j++) {
          final int stateIndex = dis.readInt();
          final int action = dis.readInt();
          final int symbolIndex = dis.readInt();
          if (stateIndex < 0 || stateIndex > states.length) {
            throw new Exception("Unknown transition state");
          }
          if (action < 0 || action > MachineAction.values().length) {
            throw new Exception("Unknown transition action");
          }
          if (symbolIndex < 0 || symbolIndex > symbols.length) {
            throw new Exception("Unknown transition symbol");
          }
          transitions.update(new Transition(
            actualStates[j],
            actualSymbols[i],
            MachineAction.values()[action],
            actualStates[stateIndex],
            actualSymbols[symbolIndex]
          ));
        }
      }
    } catch (Exception e) {
      System.err.println("Error while loading the file :");
      symbols.clear();
      states.clear();
      initialTape.reset();
      initialState = null;
      e.printStackTrace();
      return true;
    }
    return false;
  }

  private boolean saveFile(File f) {
    Symbol[] symbols = this.symbols.values();
    State[] states = this.states.values();
    try (
      DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))
    ) {
      dos.writeInt(symbols.length);
      dos.writeInt(states.length);
      for (Symbol s : symbols) {
        dos.writeUTF(s.toString());
      }
      for (State s : states) {
        dos.writeUTF(s.toString());
      }
      initialTape.save(dos, symbols);
      for (int i = 0; i < symbols.length; i++) {
        for (int j = 0; j < states.length; j++) {
          Transition.Right t = transitions.getTransition(symbols[i], states[j]);
          dos.writeInt(Utils.indexOf(states, t.getState()));
          dos.writeInt(t.getMachineAction().ordinal());
          dos.writeInt(Utils.indexOf(symbols, t.getSymbol()));
        }
      }
    } catch (Exception e) {
      System.err.println("Error while saving the file :");
      e.printStackTrace();
      return true;
    }
    return false;
  }
}
