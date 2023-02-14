package org.noopi.utils.events.view;

import org.noopi.utils.machine.Transition;

public class AddRuleEvent {
  // ATTRIBUTS

  private final Transition ruleAdded;

  // CONSTRUCTEUR

  public AddRuleEvent(Transition t){
    assert t != null;
    ruleAdded = t;
  }

  // REQUETES

  public Transition getRuleAdded() {
    return ruleAdded;
  }
}
