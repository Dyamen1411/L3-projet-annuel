package org.noopi.view.components.model;

import javax.swing.table.AbstractTableModel;

import org.noopi.utils.IDatabase;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.Transition;
import org.noopi.utils.listeners.TransitionTableUpdatedEventListener;

public class TransitionTableModel extends AbstractTableModel {

  private IDatabase<String, Symbol> symbols;
  private IDatabase<String, State> states;

  private final org.noopi.model.TransitionTableModel transitions;

  public TransitionTableModel(
    IDatabase<String, Symbol> symbols,
    IDatabase<String, State> states,
    org.noopi.model.TransitionTableModel transitions
  ) {
    assert symbols != null;
    assert states != null;
    this.symbols = symbols;
    this.states = states;
    this.transitions = transitions;

    transitions.addTableUpdatedEventListener(
      new TransitionTableUpdatedEventListener() {
        @Override
        public void onTableUpdated() {
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
    return String.class;
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
    Transition.Right v = transitions.getTransition(
      new Transition.Left(
        symbols.values()[rowIndex],
        states.values()[columnIndex - 1]
      )
    );
    return v.getSymbol() + ", " + v.getMachineAction() + ", " + v.getState();
  }
  
}
