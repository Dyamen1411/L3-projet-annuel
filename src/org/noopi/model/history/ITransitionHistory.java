package org.noopi.model.history;

import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

public interface ITransitionHistory {

  void reset();

  void pushAction(Action a);
  Action popAction();

  boolean isEmpty();

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
