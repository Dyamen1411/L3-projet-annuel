package org.noopi.view;

import javax.swing.JComponent;
import javax.swing.JMenuBar;

import org.noopi.utils.listeners.tape.TapeInitializationEventListener;
import org.noopi.utils.listeners.view.NewFileEventListener;
import org.noopi.utils.listeners.view.OpenFileEventListener;
import org.noopi.utils.listeners.view.RunEventListener;
import org.noopi.utils.listeners.view.SaveEventListener;
import org.noopi.utils.listeners.view.SpeedChangeEventListener;
import org.noopi.utils.listeners.view.StepEventListener;
import org.noopi.utils.listeners.view.StopEventListener;
import org.noopi.utils.listeners.view.TransitionModifiedEventListener;
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

public interface IFrameLayout {

  /**
   * 
   * @return the <code>JComponent</code> that represents <code>this</code>.
   */
  JComponent getView();

  /**
   * 
   * @return <code>this</code>'s menu bar.
   */
  JMenuBar getMenuBar();

  //------------------------//
  //--- GUI  interaction ---//
  //------------------------//

  /**
   * Shifts the representation of the tape to the right.
   */
  void shiftTapeRight();

  /**
   * Shifts the representation of the tape to the left.
   */
  void shiftTapeLeft();

  /**
   * Sets a new symbol on the tape under the head.
   */
  void setSymbolOnTape(Symbol s);

  /**
   * Sets the machine state.
   */
  void setMachineState(State s);

  /**
   * Adds a new rule on the rule board.
   */
  void addRule(Transition t);

  /**
   * Removes a rule from the rule board.
   */
  void removeRule(Transition t);

  /**
   * Removes all the rules from the rule board.
   */
  void resetRules();

  /**
   * Adds an action to the top of the history.
   */
  void pushHistory(Transition t);

  /**
   * Removes the last action from the top of the history.
   */
  void popHistory();

  /**
   * Asks the user if they accecp something ot not.
   * @param message The message to show.
   * @return wether the action is acepted or not.
   */
  boolean showConfirmDialog(String message);

  /**
   * Shows a message to the user.
   * @param message The message to show.
   */
  void showInformation(String message);

  /**
   * Shows an error message to the user.
   * @param message The message to show.
   */
  void showError(String message);

  //------------------------//
  //--- Listener binding ---//
  //------------------------//

  /**
   * Adds a listener to an event <code>TransitionModifiedEvent</code>.
   * This event is fired when the user wants to add a new rule from the GUI.
   * @param l the listener.
   */
  void addTransitionAddedEventListener(TransitionModifiedEventListener l);

  /**
   * Adds a listener to an event <code>TransitionModifiedEvent</code>.
   * This event is fired when the user wants to remove a rule from the GUI.
   * @param l the listener.
   */
  void addTransitionRemovedEventListener(TransitionModifiedEventListener l);

  /**
   * Adds a listener to an event <code>TapeInitializationEvent</code>.
   * This event is fired when the tape has been initialized.
   * The listener is used to set the initial state of the tape once it's ready.
   * @param l the listener.
   */
  void addTapeInitializationEventListener(TapeInitializationEventListener l);

  /**
   * Adds a listener to an event <code>StepEvent</code>.
   * This event is fired when the user wants the machine to step.
   * @param l the listener.
   */
  void addStepEventListener(StepEventListener l);

  /**
   * Adds a listener to an event <code>RunEvent</code>.
   * This event is fired when the user wants the machine to run without going
   * step by step.
   * @param l the listener.
   */
  void addRunEventListener(RunEventListener l);

  /**
   * Adds a listener to an event <code>StopEvent</code>.
   * This event is fired when the user wants to stop the machine and thus going
   * into a "step by step mode".
   * @param l the listener.
   */
  void addStopEventListener(StopEventListener l);

  /**
   * Adds a listener to an event <code>SpeedChangeEvent</code>.
   * This event is fired when the user wants to change the execution speed of
   * the machine.
   * @param l the listener.
   */
  void addSpeedChangeEventListener(SpeedChangeEventListener l);

  /**
   * Adds a listener to an event <code>OpenFileEvent</code>.
   * This event is fired when the user wants to load a machine from a file.
   * @param l the listener.
   */
  void addOpenFileEventListener(OpenFileEventListener l);

  /**
   * Adds a listener to an event <code>NewFileEvent</code>.
   * This event is fired when the user wants to create an empty machine.
   * @param l the listener.
   */
  void addNewFileEventListener(NewFileEventListener l);

  /**
   * Adds a listener to an event <code>SaveEvent</code>.
   * This event is fired when the user wants to save the machine into a file.
   * @param l the listener.
   */
  void addSaveEventListener(SaveEventListener l);
}
