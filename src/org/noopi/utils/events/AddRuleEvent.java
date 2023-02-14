package org.noopi.utils.events;

import org.noopi.utils.machine.Transition;

public class AddRuleEvent implements ILayoutEvent {
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
