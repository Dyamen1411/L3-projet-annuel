package org.noopi.model.machine;

import org.noopi.utils.exceptions.MachineDecidabilityExecption;
import org.noopi.utils.listeners.machine.MachineResetEventListener;
import org.noopi.utils.listeners.machine.MachineStepEventListener;
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

public interface ITuringMachine {

  void reset(State defaultState);

  void addTransition(Transition t);
  void removeTransition(Transition t);

  void step(Symbol s) throws MachineDecidabilityExecption;
  void setState(State s);

  State getState();

  void addFinalState(State finalState);

  boolean isOperationnal();
  boolean isDone();

  void addStepEventListener(MachineStepEventListener l);
  void addResetEventListener(MachineResetEventListener l);

  void removeStepEventListener(MachineStepEventListener l);
  void removeResetEventListener(MachineResetEventListener l);
}
