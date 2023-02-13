package org.noopi.model.tape;

import javax.swing.event.EventListenerList;

import org.noopi.utils.events.TapeMovedEvent;
import org.noopi.utils.events.TapeResetEvent;
import org.noopi.utils.events.TapeWriteEvent;
import org.noopi.utils.listeners.TapeMovedEventListener;
import org.noopi.utils.listeners.TapeResetEventListener;
import org.noopi.utils.listeners.TapeWriteEventListener;
import org.noopi.utils.machine.Direction;
import org.noopi.utils.machine.Symbol;

public abstract class AbstractTape implements ITape {
  private EventListenerList listenerList;

  protected TapeResetEvent tapeResetEvent;
  protected TapeMovedEvent tapeMovedEvent;
  protected TapeWriteEvent tapeWriteEvent;

  public AbstractTape() {
    listenerList = new EventListenerList();
  }

  protected void fireResetEvent() {
    Object[] list = listenerList.getListenerList();
    for (int i = list.length - 2; i >= 0; i -= 2) {
      if (list[i] != TapeResetEventListener.class) {
        continue;
      }
      if (tapeResetEvent == null) {
        tapeResetEvent = new TapeResetEvent();
      }
      ((TapeResetEventListener) list[i + 1]).onTapeReset(tapeResetEvent);
    }
  }

  protected void fireTapeMovedEvent(Direction d) {
    assert d != null;
    Object[] list = listenerList.getListenerList();
    boolean b = false;
    for (int i = list.length - 2; i >= 0; i -= 2) {
      if (list[i] != TapeMovedEventListener.class) {
        continue;
      }
      if (tapeMovedEvent == null || !b) {
        tapeMovedEvent = new TapeMovedEvent(d);
        b = true;
      }
      ((TapeMovedEventListener) list[i + 1]).onTapeMoved(tapeMovedEvent);
    }
  }

  protected void fireTapeWriteEvent(Symbol s) {
    Object[] list = listenerList.getListenerList();
    boolean b = false;
    for (int i = list.length - 2; i >= 0; i -= 2) {
      if (list[i] != TapeWriteEventListener.class) {
        continue;
      }
      if (tapeWriteEvent == null || !b) {
        tapeWriteEvent = new TapeWriteEvent(s);
        b = true;
      }
      ((TapeWriteEventListener) list[i + 1]).onTapeWrite(tapeWriteEvent);
    }
  }

  @Override
  public void addTapeResetEventListener(TapeResetEventListener l) {
    assert l != null;
    listenerList.add(TapeResetEventListener.class, l);
  }

  @Override
  public void addTapeMovedEventListener(TapeMovedEventListener l) {
    assert l != null;
    listenerList.add(TapeMovedEventListener.class, l);
  }

  @Override
  public void addTapeWriteEventListener(TapeWriteEventListener l) {
    assert l != null;
    listenerList.add(TapeWriteEventListener.class, l);
  }

  @Override
  public void removeTapeResetEventListener(TapeResetEventListener l) {
    assert l != null;
    listenerList.remove(TapeResetEventListener.class, l);
  }

  @Override
  public void removeTapeMovedEventListener(TapeMovedEventListener l) {
    assert l != null;
    listenerList.remove(TapeMovedEventListener.class, l);
  }

  @Override
  public void removeTapeWriteEventListener(TapeWriteEventListener l) {
    assert l != null;
    listenerList.remove(TapeWriteEventListener.class, l);
  }
}
