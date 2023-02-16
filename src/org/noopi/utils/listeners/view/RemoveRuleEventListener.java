package org.noopi.utils.listeners.view;

import java.util.EventListener;

import org.noopi.utils.events.view.RemoveRuleEvent;

public interface RemoveRuleEventListener extends EventListener {
  void onRuleRemoved(RemoveRuleEvent e);
}
