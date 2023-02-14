package org.noopi.utils.events;

import org.noopi.utils.machine.Transition;

public class RemoveRuleEvent implements ILayoutEvent {
  // ATTRIBUTS

  private final Transition ruleRemoved;

  // CONSTRUCTEUR

  public RemoveRuleEvent(Transition t){
    ruleRemoved = t;
  }

  // REQUETES

  public Transition getRuleRemoved(){
    return ruleRemoved;
  }
}
