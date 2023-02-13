package org.noopi.utils.listeners;

import org.noopi.utils.events.NewFileEvent;

public interface NewFileEventListener {
  void onNewFile(NewFileEvent e);
}
