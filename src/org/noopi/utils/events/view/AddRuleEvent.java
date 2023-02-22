package org.noopi.utils.events.view;

import org.noopi.utils.machine.Transition;

public class AddRuleEvent {

  // ATTRIBUTS

  private Transition transition;

  // CONSTRUCTEURS

  public AddRuleEvent(Transition t) {
    transition = t;
  }

  // REQUETES

  public Transition geTransition() {
    return transition;
  }
}
