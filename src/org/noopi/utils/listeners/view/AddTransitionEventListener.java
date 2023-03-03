package org.noopi.utils.listeners.view;

import java.util.EventListener;

import org.noopi.utils.events.view.AddTransitionEvent;

public interface AddTransitionEventListener extends EventListener {
  void onTransitionAdded(AddTransitionEvent e);
}
