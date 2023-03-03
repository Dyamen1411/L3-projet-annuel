package org.noopi.utils.events.view;

import org.noopi.utils.Transition;

public class AddTransitionEvent {

  // ATTRIBUTS

  private Transition transition;

  // CONSTRUCTEURS

  public AddTransitionEvent(Transition t) {
    transition = t;
  }

  // REQUETES

  public Transition geTransition() {
    return transition;
  }
}
