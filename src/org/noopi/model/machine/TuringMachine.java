package org.noopi.model.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.noopi.utils.exceptions.MachineDecidabilityExecption;
import org.noopi.utils.machine.Direction;
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

public final class TuringMachine extends AbstractTuringMachine {

  private State currentState;
  
  private final HashMap<K, V> rules;
  private final List<State> finalStates;

  private boolean operationnal;
  private boolean done;

  public TuringMachine() {
    rules = new HashMap<>();
    finalStates = new ArrayList<>();
    done = false;
    operationnal = false;
  }

  @Override
  public void reset(State defaultState) {
    rules.clear(); 
    finalStates.clear();
    currentState = defaultState;
    operationnal = true;
    done = false;
    fireResetEvent();
  }

  @Override
  public void addTransition(Transition t) {
    final K k = new K(t.getOldState(), t.getOldSymbol());
    final V v = new V(t.getNewState(), t.getNewSymbol(), t.getNewDirection());
    rules.put(k, v);
  }

  @Override
  public void removeTransition(Transition t) {
    final K k = new K(t.getOldState(), t.getOldSymbol());
    final V v = new V(t.getNewState(), t.getNewSymbol(), t.getNewDirection());
    rules.remove(k, v);
  }

  @Override
  public void addFinalState(State finalState) {
    finalStates.add(finalState);
  }

  @Override
  public void step(Symbol s) throws MachineDecidabilityExecption {
    assert !done;
    final K k = new K(currentState, s);
    final V v = rules.get(k);
    if (v == null) {
      throw new MachineDecidabilityExecption();
    }
    currentState = v.s;
    if (finalStates.contains(currentState)) {
      done = true;
    }
    fireStepEvent(new Transition(k.s, k.w, v.d, v.s, v.w));
  }

  @Override
  public void stepBack() {
    throw new UnsupportedOperationException();
  }

  @Override
  public State getState() {
    return currentState;
  }

  @Override
  public boolean isOperationnal() {
    return operationnal;
  }

  @Override
  public boolean isDone() {
    return done;
  }

  private final class K {
    final State s;
    final Symbol w;

    public K(State s, Symbol w) {
      this.s = s;
      this.w = w;
    }
    
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof K)) {
        return false;
      }
      K k = (K) o;
      return s.equals(k.s) && w.equals(k.w); 
    }
  }

  private final class V {
    final State s;
    final Symbol w;
    final Direction d;

    public V(State s, Symbol w, Direction d) {
      this.s = s;
      this.w = w;
      this.d = d;
    } 
  }
  
}
