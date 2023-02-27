package org.noopi.utils.listeners.view;

import java.util.EventListener;

import org.noopi.utils.events.view.AddRuleEvent;

public interface AddRuleEventListener extends EventListener {
  void onRuleAdded(AddRuleEvent e);
}
