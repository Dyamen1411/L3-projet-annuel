package org.noopi.model.tape;

import org.noopi.utils.machine.MachineAction;
import org.noopi.utils.machine.Symbol;

public final class Tape extends AbstractTape {

  private Cell currentCell;
  private Symbol defaultSymbol;

  public Tape(Symbol defaultSymbol) {
    reset(defaultSymbol);
  }

  @Override
  public void reset(Symbol defaultSymbol) {
    reset(defaultSymbol, new Symbol[]{});
  }

  @Override
  public void reset(Symbol defaultSymbol, Symbol[] symbols) {
    assert defaultSymbol != null;
    assert symbols != null;
    for (Symbol s : symbols) {
      assert s != null;
    }
    this.defaultSymbol = defaultSymbol;
    final Cell orig = new Cell();
    currentCell = orig;
    for (Symbol s : symbols) {
      currentCell.symbol = s;
      shift(MachineAction.TAPE_RIGHT);
    }
    currentCell = orig;
    fireResetEvent();
  }

  @Override
  public void shift(MachineAction d) {
    assert d != null;
    switch (d) {
      case TAPE_LEFT: currentCell = currentCell.getPrev(); break;
      case TAPE_RIGHT: currentCell = currentCell.getNext(); break;
    }
    fireTapeMovedEvent(d);
  }

  @Override
  public Symbol readSymbol() {
    return currentCell.symbol;
  }

  @Override
  public void writeSymbol(Symbol symbol) {
    assert symbol != null;
    currentCell.symbol = symbol;
    fireTapeWriteEvent(symbol);
  }

  private class Cell {
    private Cell next;
    private Cell prev;
    private Symbol symbol;

    public Cell() {
      this(Tape.this.defaultSymbol);
    }

    public Cell(Symbol s) {
      setSymbol(s);
    }

    public void setSymbol(Symbol s) {
      assert s != null;
      symbol = s;
    }

    public Cell getNext() {
      if (next == null) {
        next = new Cell();
        next.prev = this;
      }
      return next;
    }

    public Cell getPrev() {
      if (prev == null) {
        prev = new Cell();
        prev.next = this;
      }
      return prev;
    }
  }
}
