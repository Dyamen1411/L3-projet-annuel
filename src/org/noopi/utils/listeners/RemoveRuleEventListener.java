package org.noopi.utils.listeners;

import org.noopi.utils.events.RemoveRuleEvent;

public interface RemoveRuleEventListener {
  void onRuleRemoved(RemoveRuleEvent e);
}
