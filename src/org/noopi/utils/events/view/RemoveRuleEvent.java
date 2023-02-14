package org.noopi.utils.events.view;

import org.noopi.utils.machine.Transition;

public class RemoveRuleEvent {
  // ATTRIBUTS

  private final Transition r;

  // CONSTRUCTEUR

  public RemoveRuleEvent(Transition t){
    assert t != null;
    r = t;
  }

  // REQUETES

  public Transition getRemovedRule() {
    return r;
  }
}
