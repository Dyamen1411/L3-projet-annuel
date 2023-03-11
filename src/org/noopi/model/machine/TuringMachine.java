package org.noopi.model.machine;

import org.noopi.model.TransitionTableModel;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.Transition;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public final class TuringMachine implements ITuringMachine {

  private final TransitionTableModel transitionTable;
  private State currentState;
  private boolean done;

  public TuringMachine(TransitionTableModel transitionTable) {
    assert transitionTable != null;
    this.transitionTable = transitionTable;
    currentState = State.DEFAULT;
    done = false;

    transitionTable.getStatesDatabase().addDatabaseUnregisterEventListener(
      new DatabaseUnregisterEventListener<State>() {
        @Override
        public void onUnregisterEvent(DatabaseUnregisterEvent<State> e) {
          if (e.getValue().equals(currentState)) {
            // TODO: set current state to State.DEFAULT and fire an event to
            //  warn listeners that the machine is not operable.
          }
        }
      }
    );
  }

  @Override
  public void reset(State defaultState) {
    currentState = defaultState;
    done = false;
  }

  @Override
  public Transition.Right step(Symbol s) {
    assert !done;
    assert s != null;
    assert currentState != State.DEFAULT;
    Transition.Right v = transitionTable.getTransition(s, currentState);
    currentState = v.getState();
    if (v.getMachineAction() == MachineAction.MACHINE_STOP) {
      done = true;
    }
    // DEBUG
    System.out.println(v + " ==> " + done);
    return v.copy();
  }

  @Override
  public void setState(State s) {
    this.currentState = s;
  }

  @Override
  public State getState() {
    return currentState;
  }

  @Override
  public boolean isDone() {
    return done;
  }
}
