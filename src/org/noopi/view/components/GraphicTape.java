package org.noopi.view.components;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.noopi.utils.machine.Symbol;

public class GraphicTape extends JList<String> {

  public static final int CELL_COUNT = 9;
  private static final int PREFERED_WIDTH =  800;
  private static final int PREFERED_HEIGHT = 200;

  // ATTRIBUTS
  private DefaultListModel<String> model;
  private Symbol defaultSymbol;

  // CONSTRUCTEUR

  public GraphicTape(Symbol defaultSymbol) {
    assert defaultSymbol != null;
    
    model = new DefaultListModel<>();
    for (int i = CELL_COUNT; i > 0; --i) {
      model.add(0, defaultSymbol.toString());
    }
    setModel(model);

    setEnabled(false);
    setVisibleRowCount(1);
    setPreferredSize(null);
    setLayoutOrientation(JList.HORIZONTAL_WRAP);
    setFixedCellWidth(PREFERED_WIDTH / CELL_COUNT);
    setFixedCellHeight(PREFERED_HEIGHT);
    this.defaultSymbol = defaultSymbol;
  }

  // COMMANDES

  public void shiftTapeRight() {
    model.remove(CELL_COUNT - 1);
    model.add(0, defaultSymbol.toString());
  }

  public void shiftTapeLeft() {
    model.remove(0);
    model.add(CELL_COUNT - 1, defaultSymbol.toString());
  }

  public void setSymbol(Symbol s) {
    assert s != null;
    model.remove(CELL_COUNT / 2);
    model.add(CELL_COUNT / 2,  s.toString());
  }

  public void setDefaultSymbol(Symbol defaultSymbol) {
    this.defaultSymbol = defaultSymbol;
  }
}

