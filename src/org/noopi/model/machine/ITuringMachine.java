package org.noopi.model.machine;

import org.noopi.utils.listeners.machine.MachineStepEventListener;
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Transition;

public interface ITuringMachine {

  void reset();

  void addTransition(Transition t);
  void removeTransition(Transition t);

  void step();
  void stepBack();

  State getState();

  void addStepEventListener(MachineStepEventListener l);
  void addResetEventListener(MachineStepEventListener l);

  void removeStepEventListener(MachineStepEventListener l);
  void removeResetEventListener(MachineStepEventListener l);
}
