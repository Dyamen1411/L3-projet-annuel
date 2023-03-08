package org.noopi.utils;

public class Transition {
  private final State oldState;
  private final Symbol oldSymbol;
  private final MachineAction newDirection;
  private final State newState;
  private final Symbol newSymbol;

  public Transition(
    State oldState,
    Symbol oldSymbol,
    MachineAction newDirection,
    State newState,
    Symbol newSymbol
  ) {
    this.oldState = oldState;
    this.oldSymbol = oldSymbol;
    this.newDirection = newDirection;
    this.newState = newState;
    this.newSymbol = newSymbol;
  }

  public State getOldState() {
    return oldState;
  }

  public Symbol getOldSymbol() {
    return oldSymbol;
  }

  public MachineAction getNewDirection() {
    return newDirection;
  }

  public State getNewState() {
    return newState;
  }

  public Symbol getNewSymbol() {
    return newSymbol;
  }
  @Override
  public String toString() {
    return
      "(" + oldState + ", " + oldSymbol + ")"
      + " => "
      + "(" + newState + ", " + newSymbol + ", " + newDirection + ")"
    ;
  }


  public Transition.Left toLeft() {
    return new Transition.Left(oldSymbol, oldState);
  }

  public Transition.Right toRight() {
    return new Transition.Right(newSymbol, newDirection, newState);
  }

  public static class Left {
    public final Symbol symbol;
    public final State state;

    public Left(Symbol symbol, State state) {
      assert symbol != null;
      assert state != null;
      this.symbol = symbol;
      this.state = state;
    } 
  }

  public static class Right {
    private Symbol symbol;
    private MachineAction action;
    private State state;

    public Right(
      Symbol symbol,
      MachineAction action,
      State state
    ) {
      assert symbol != null;
      assert action != null;
      assert state != null;
      this.symbol = symbol;
      this.action = action;
      this.state = state;
    }

    public Symbol getSymbol() {
      return symbol;
    }

    public MachineAction getMachineAction() {
      return action;
    }
    
    public State getState() {
      return state;
    }

    public void setSymbol(Symbol symbol) {
      assert symbol != null;
      this.symbol = symbol;
    }

    public void setMachineAction(MachineAction action) {
      assert action != null;
      this.action = action;
    }
    
    public void setState(State state) {
      assert state != null;
      this.state = state;
    }
  }
  

}
