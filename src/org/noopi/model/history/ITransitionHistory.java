package org.noopi.model.history;

import org.noopi.utils.listeners.history.HistoryPopEventListener;
import org.noopi.utils.listeners.history.HistoryPushEventListener;
import org.noopi.utils.listeners.history.HistoryResetEventListener;

import org.noopi.utils.machine.Transition;

public interface ITransitionHistory {

  void reset();

  void pushAction(Transition a);
  Transition popAction();

  boolean isEmpty();

  void addHistoryResetEventListener(HistoryResetEventListener l);
  void addHistoryPushEventListener(HistoryPushEventListener l);
  void addHistoryPopEventListener(HistoryPopEventListener l);

  void removeHistoryResetEventListener(HistoryResetEventListener l);
  void removeHistoryPushEventListener(HistoryPushEventListener l);
  void removeHistoryPopEventListener(HistoryPopEventListener l);
}
