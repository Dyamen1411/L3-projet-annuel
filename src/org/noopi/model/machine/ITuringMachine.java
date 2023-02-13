package org.noopi.model.machine;

import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Transition;

public interface ITuringMachine {

  void reset();

  void addTransition(Transition t);
  void removeTransition(Transition t);

  void step();
  void stepBack();

  State getState();
}
