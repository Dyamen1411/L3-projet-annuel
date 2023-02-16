package org.noopi.model.machine;

import javax.swing.event.EventListenerList;

import org.noopi.utils.events.machine.MachineResetEvent;
import org.noopi.utils.events.machine.MachineStepEvent;
import org.noopi.utils.listeners.machine.MachineResetEventListener;
import org.noopi.utils.listeners.machine.MachineStepEventListener;
import org.noopi.utils.machine.Transition;

/**
 * Manages all the listener mechanics of a IAbstractTuringMachine.
 */
public abstract class AbstractTuringMachine implements ITuringMachine {

  private EventListenerList listenerList;

  private MachineStepEvent stepEvent;
  private MachineResetEvent resetEvent;

  public AbstractTuringMachine() {
    listenerList = new EventListenerList();
  }

  @Override
  public void addStepEventListener(MachineStepEventListener l) {
    assert l != null;
    listenerList.add(MachineStepEventListener.class, l);
  }

  @Override
  public void addResetEventListener(MachineResetEventListener l) {
    assert l != null;
    listenerList.add(MachineResetEventListener.class, l);
  }

  @Override
  public void removeStepEventListener(MachineStepEventListener l) {
    assert l != null;
    listenerList.add(MachineStepEventListener.class, l);
  }

  @Override
  public void removeResetEventListener(MachineResetEventListener l) {
    assert l != null;
    listenerList.add(MachineResetEventListener.class, l);
  }

  /**
   * Sends a <code>MachineStepEvent</code> to every subscribed listeners.
   */
  protected void fireStepEvent(Transition t) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != MachineStepEventListener.class) {
        continue;
      }
      if (!b || stepEvent == null) {
        stepEvent = new MachineStepEvent(t);
        b = true;
      }
      ((MachineStepEventListener) listeners[i + 1]).onMachineStepped(stepEvent);
    }
  }

  /**
   * Sends a <code>MachineResetEvent</code> to every subscribed listeners.
   */
  protected void fireResetEvent() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != MachineResetEventListener.class) {
        continue;
      }
      if (resetEvent == null) {
        resetEvent = new MachineResetEvent(this);
      }
      ((MachineResetEventListener) listeners[i + 1]).onReset(resetEvent);
    }
  }

}
