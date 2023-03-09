package org.noopi.view.components;

import java.awt.Graphics;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.noopi.model.tape.ITape;
import org.noopi.utils.MachineAction;
import org.noopi.utils.Symbol;
import org.noopi.utils.events.tape.TapeMovedEvent;
import org.noopi.utils.events.tape.TapeWriteEvent;
import org.noopi.utils.listeners.tape.TapeMovedEventListener;
import org.noopi.utils.listeners.tape.TapeWriteEventListener;
import org.noopi.utils.listeners.view.TapeUpdatedEventListener;

public class GraphicTape extends JList<String> {

  public static final int CELL_COUNT = 9;

  private static final String DEFAULT_SYMBOL = Symbol.DEFAULT.toString();
  private static final int START_INDEX = 0;
  private static final int END_INDEX = CELL_COUNT - 1;
  private static final int HEAD_INDEX = END_INDEX / 2;

  private ITape model;

  // ATTRIBUTS
  private DefaultListModel<String> list;

  // CONSTRUCTEUR

  public GraphicTape(ITape tape, boolean selectable) {
    assert tape != null;
    this.model = tape;

    list = new DefaultListModel<>();
    for (int i = CELL_COUNT; i > 0; --i) {
      list.add(0, DEFAULT_SYMBOL);
    }
    setModel(list);

    setEnabled(selectable);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setSelectedIndex(CELL_COUNT / 2);
    setVisibleRowCount(1);
    setLayoutOrientation(JList.HORIZONTAL_WRAP);

    // tape.addTapeMovedEventListener(new TapeMovedEventListener() {
    //   @Override
    //   public void onTapeMoved(TapeMovedEvent e) {
    //     MachineAction a = e.getDirection();
    //     if (a == MachineAction.MACHINE_STOP) {
    //       // error : should never happen.
    //       return;
    //     }

    //     Symbol[] slice = tape.getSlice(CELL_COUNT);

    //     switch (e.getDirection()) {
    //       case TAPE_LEFT: 
    //         list.remove(START_INDEX);
    //         list.add(END_INDEX, slice[END_INDEX].toString());
    //       break;
    //       case TAPE_RIGHT: 
    //         list.remove(END_INDEX);
    //         list.add(START_INDEX, slice[START_INDEX].toString());
    //       break;

    //       default: return;
    //     }

    //     repaint();
    //   }
    // });

    // tape.addTapeWriteEventListener(new TapeWriteEventListener() {
    //   @Override
    //   public void onTapeWritten(TapeWriteEvent e) {
    //     list.remove(HEAD_INDEX);
    //     list.add(HEAD_INDEX, e.getSymbol().toString());
    //   }
    // });

    tape.addTapeUpdatedEventListener(new TapeUpdatedEventListener() {
      @Override
      public void onUpdate() {
        list.removeAllElements();
        Symbol[] symbols = model.getSlice((CELL_COUNT - 1) / 2);
        for (int i = END_INDEX; i >= START_INDEX; --i) {
          list.add(0, symbols[i].toString());
        }
      }
    });
  }

  // COMMANDES

  @Override
  public void paint(Graphics g) {
    int w = getWidth() / CELL_COUNT;
    int h = getHeight();
    w = h = Math.min(w, h);
    setFixedCellWidth(w);
    setFixedCellHeight(h);
    setSize(CELL_COUNT * w, h);
    super.paint(g);
  }
}

