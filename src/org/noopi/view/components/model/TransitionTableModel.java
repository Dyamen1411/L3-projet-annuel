package org.noopi.view.components.model;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import org.noopi.utils.IDatabase;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.events.database.DatabaseRegisterEvent;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;

public class TransitionTableModel extends AbstractTableModel {

  private IDatabase<String, Symbol> symbols;
  private IDatabase<String, State> states;

  private DatabaseComboboxModel<String> symbolsComboboxModel;
  private DatabaseComboboxModel<String> statesComboboxModel;

  private HashMap<Key, JPanel> map;

  private final JLabel separator;

  public TransitionTableModel(
    IDatabase<String, Symbol> symbols,
    IDatabase<String, State> states
  ) {
    assert symbols != null;
    assert states != null;
    this.symbols = symbols;
    this.states = states;
    symbolsComboboxModel = new DatabaseComboboxModel<>(symbols);
    statesComboboxModel = new DatabaseComboboxModel<>(states);
    separator = new JLabel(", ");
    map = new HashMap<>();

    symbols.addDatabaseRegisterEventListener(
      new DatabaseRegisterEventListener<Symbol>() {
        @Override
        public void onRegisterEvent(DatabaseRegisterEvent<Symbol> e) {
          fireTableDataChanged();
        }
      }
    );

    states.addDatabaseRegisterEventListener(
      new DatabaseRegisterEventListener<State>() {
        @Override
        public void onRegisterEvent(DatabaseRegisterEvent<State> e) {
          fireTableStructureChanged();
        }
      }
    );
  }

  @Override
  public int getRowCount() {
    return symbols.size();
  }

  @Override
  public int getColumnCount() {
    return states.size() + 1;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return columnIndex == 0
      ? "Tape Symbol"
      : states.values()[columnIndex - 1].toString();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return columnIndex == 0
      ? String.class
      : JPanel.class;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
      return symbols.entries()[rowIndex];
    }
    Symbol symbol = symbols.values()[rowIndex];
    State state = states.values()[columnIndex - 1];
    Key k = new Key(symbol, state);
    JPanel p = map.get(k);
    if (p == null) {
      p = new JPanel();
      p.add(new JComboBox<String>(symbolsComboboxModel));
      p.add(separator);
      p.add(new JComboBox<>(MachineAction.values()));
      p.add(separator);
      p.add(new JComboBox<String>(statesComboboxModel));
      map.put(k, p);
    }
    return p;
  }

  private class Key {
    final Symbol symbol;
    final State state;

    public Key(Symbol symbol, State state) {
      this.symbol = symbol;
      this.state = state;
    }

    @Override
    public boolean equals(Object o) {
      Key oo = (Key) o;
      return oo.symbol.equals(symbol) && oo.state.equals(state);
    }
  }
  
}
