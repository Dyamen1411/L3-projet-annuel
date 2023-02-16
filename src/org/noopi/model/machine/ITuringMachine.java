package org.noopi.model.machine;

import org.noopi.utils.exceptions.MachineDecidabilityExecption;
import org.noopi.utils.listeners.machine.MachineResetEventListener;
import org.noopi.utils.listeners.machine.MachineStepEventListener;
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

public interface ITuringMachine {

  /**
   * <p>Resets all the rules and the final states of this machine then
   * sets the current state of this machine to <code>state</code>.</p>
   * <p>Will send a <code>MachineResetEvent</code> to every subsribed
   * listener.</p>
   * @param state the new current state.
   */
  void reset(State state);

  /**
   * Adds the transition <code>t</code> to the machine.
   */
  void addTransition(Transition t);

  /**
   * Removes the transition <code>t</code> if the machine has that transition.
   */
  void removeTransition(Transition t);

  /**
   * <p>
   *  Given a symbol <code>s</code>, changes it's state then fires a
   *  <code>MachineStepEvent</code> to every subscribed listener.
   * </p>
   * <p>
   *  If there is no registered transition for
   *  <p>
   *    <code>(getState(), s)</code>
   *  </p>
   *  then will fire a
   *  <code>MachineDecidabilityExecption</code> exception.
   * </p>
   * @param s the symbol.
   * @throws MachineDecidabilityExecption
   */
  void step(Symbol s) throws MachineDecidabilityExecption;

  /**
   * Sets the current state of the machine to <code>s</code>.
   */
  void setState(State s);

  /**
   * @return the current state.
   */
  State getState();

  /**
   * Adds a new final state to the machine.
   */
  void addFinalState(State finalState);

  /**
   * Tells wether the machine has a current state or not.
   */
  boolean isOperationnal();

  /**
   * Tells wether the current state is a final state or not.
   */
  boolean isDone();

  /**
   * When the <code>reset</code> method is called, it will send an
   * <code>MachineResetEvent</code> event to this listener.
   */
  void addResetEventListener(MachineResetEventListener l);

  /**
   * When the <code>step</code> method is called, it will send an
   * <code>MachineStepEvent</code> event to this listener.
   */
  void addStepEventListener(MachineStepEventListener l);

  void removeResetEventListener(MachineResetEventListener l);
  void removeStepEventListener(MachineStepEventListener l);
}
