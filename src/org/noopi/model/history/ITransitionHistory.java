package org.noopi.model.history;

import org.noopi.utils.listeners.history.HistoryPopEventListener;
import org.noopi.utils.listeners.history.HistoryPushEventListener;
import org.noopi.utils.listeners.history.HistoryResetEventListener;

import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

public interface ITransitionHistory {

  void reset();

  void pushAction(Action a);
  Action popAction();

  boolean isEmpty();

  void addHistoryResetEventListener(HistoryResetEventListener l);
  void addHistoryPushEventListener(HistoryPushEventListener l);
  void addHistoryPopEventListener(HistoryPopEventListener l);

  void removeHistoryResetEventListener(HistoryResetEventListener l);
  void removeHistoryPushEventListener(HistoryPushEventListener l);
  void removeHistoryPopEventListener(HistoryPopEventListener l);

  class Action {
    public final Symbol symbol;
    public final Transition transition;
    
    public Action(Symbol symbol, Transition transition) {
      this.symbol = symbol;
      this.transition = transition;
    }

    public Symbol getSymbol() {
      return symbol;
    }

    public Transition getTransition() {
      return transition;
    }
  }
}
