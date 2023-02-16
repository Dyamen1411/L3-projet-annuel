package org.noopi.model.tape;

import org.noopi.utils.machine.Direction;
import org.noopi.utils.machine.Symbol;

public class TapeTester {

  private static final Symbol DEFAULT_SYMBOL = new Symbol("_");
  private static final Symbol TEST_SYMBOL = new Symbol("#");

  public static void main(String[] args) {
    ITape tape = new Tape(DEFAULT_SYMBOL);

    assert tape.readSymbol().equals(DEFAULT_SYMBOL);

    tape.writeSymbol(TEST_SYMBOL);
    assert tape.readSymbol().equals(TEST_SYMBOL);

    tape.shift(Direction.RIGHT);
    assert tape.readSymbol().equals(DEFAULT_SYMBOL);

    tape.shift(Direction.LEFT);
    assert tape.readSymbol().equals(TEST_SYMBOL);

    tape.shift(Direction.LEFT);
    assert tape.readSymbol().equals(DEFAULT_SYMBOL);

    tape.reset(TEST_SYMBOL);
    assert tape.readSymbol().equals(TEST_SYMBOL);

    tape.shift(Direction.LEFT);
    assert tape.readSymbol().equals(TEST_SYMBOL);

    Symbol[] testList = { TEST_SYMBOL, DEFAULT_SYMBOL, DEFAULT_SYMBOL, TEST_SYMBOL };
    tape.reset(DEFAULT_SYMBOL, testList);
    for (Symbol s : testList) {
      assert tape.readSymbol().equals(s);
      tape.shift(Direction.RIGHT);
    }

    System.out.println("yipee");
  }
}
