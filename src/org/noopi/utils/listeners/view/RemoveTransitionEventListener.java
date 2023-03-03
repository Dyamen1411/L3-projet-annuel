package org.noopi.utils.listeners.view;

import java.util.EventListener;

import org.noopi.utils.events.view.RemoveTransitionEvent;

public interface RemoveTransitionEventListener extends EventListener {
  void onTransitionRemoved(RemoveTransitionEvent e);
}
