package org.noopi.utils.listeners;

import org.noopi.utils.events.OpenFileEvent;

public interface OpenFileEventListener {
  void onFileOpened(OpenFileEvent e);
}
