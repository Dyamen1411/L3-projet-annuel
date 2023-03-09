package org.noopi.utils.listeners.view;

import java.util.EventListener;

import org.noopi.utils.MachineAction;

public interface TapeShiftEventListener extends EventListener {
  void onTapeShifted(MachineAction a);
}
