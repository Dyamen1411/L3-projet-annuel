package org.noopi.utils.listeners.machine;

import org.noopi.utils.events.machine.MachineStepEvent;

public interface MachineStepEventListener {
  void onMachineStepped(MachineStepEvent e);
}
