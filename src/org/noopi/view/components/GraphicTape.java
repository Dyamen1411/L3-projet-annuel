package org.noopi.view.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.noopi.model.tape.ITape;
import org.noopi.utils.Symbol;
import org.noopi.utils.listeners.view.TapeUpdatedEventListener;

public class GraphicTape extends JList<String> {

  public static final int CELL_COUNT = 9;

  private static final String DEFAULT_SYMBOL = Symbol.DEFAULT.toString();
  private static final int START_INDEX = 0;
  private static final int END_INDEX = CELL_COUNT - 1;
  private static final Color colorTab[] = {Color.BLUE, Color.WHITE, Color.RED};

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
    setCellRenderer(new CellRenderer());

    setEnabled(selectable);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setSelectedIndex(CELL_COUNT / 2);
    setVisibleRowCount(1);
    setLayoutOrientation(JList.HORIZONTAL_WRAP);

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

  // TYPE IMBRIQUE

  class CellRenderer extends JLabel implements ListCellRenderer<Object> {


    public CellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        setText(value.toString());


        setBackground(colorTab[index % colorTab.length]);

        return this;
    }
}
}

