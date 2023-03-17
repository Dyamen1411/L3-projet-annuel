package org.noopi.view.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.noopi.utils.IDatabase;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.Transition;
import org.noopi.view.components.model.DatabaseComboboxModel;
import org.noopi.view.components.model.TransitionTableModel;

public class TransitionTable extends JPanel {
  private final JTable table;

  private IDatabase<String, Symbol> symbols;
  private IDatabase<String, State> states;
  private org.noopi.model.TransitionTableModel transitions;

  private TransitionTableModel model;

  private JComboBox<String> symbolEditor;
  private JComboBox<String> stateEditor;
  private JComboBox<MachineAction> actionEditor;

  public TransitionTable(
    IDatabase<String, Symbol> symbols,
    IDatabase<String, State> states,
    org.noopi.model.TransitionTableModel transitions
  ) {
    assert transitions != null;
    assert symbols != null;
    assert states != null;
    this.transitions = transitions;
    this.symbols = symbols;
    this.states = states;
    model = new TransitionTableModel(symbols, states, transitions);
    table = new JTable(model);
    symbolEditor = new JComboBox<>(new DatabaseComboboxModel<>(symbols));
    stateEditor = new JComboBox<>(new DatabaseComboboxModel<>(states));
    actionEditor = new JComboBox<>(MachineAction.values());

    setLayout(new BorderLayout());

    JPanel editor = new JPanel(new FlowLayout());
    editor.add(symbolEditor);
    editor.add(stateEditor);
    editor.add(actionEditor);
    add(editor, BorderLayout.NORTH);

    JScrollPane scroll = new JScrollPane(table);
    scroll.setPreferredSize(new Dimension(300, 140));
    add(scroll, BorderLayout.CENTER);


    table.setRowHeight(40);

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setCellSelectionEnabled(true);

    table.getTableHeader().setReorderingAllowed(false);

    ListSelectionListener l = new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        final int x = table.getSelectedColumn();
        final int y = table.getSelectedRow();
        final boolean enabled = x > 0 && y >= 0;
        setEditorEnabled(enabled);
        if (!enabled) {
          symbolEditor.setSelectedItem(null);
          stateEditor.setSelectedItem(null);
          actionEditor.setSelectedItem(null);
          return;
        }
        Symbol symbol = symbols.values()[y];
        State state = states.values()[x - 1];
        Transition.Left k = new Transition.Left(symbol, state);
        Transition.Right v = transitions.getTransition(k);

        symbolEditor.setSelectedItem(v.getSymbol().toString());
        actionEditor.setSelectedItem(v.getMachineAction());
        stateEditor.setSelectedItem(v.getState().toString());
      }
    };

    table.getColumnModel().getSelectionModel().addListSelectionListener(l);
    table.getSelectionModel().addListSelectionListener(l);

    symbolEditor.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        updateSymbol((String) e.getItem());
      }
    });

    actionEditor.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        updateAction((MachineAction) e.getItem());
      }
    });

    stateEditor.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        updateState((String) e.getItem());
      }
    });

    setEditorEnabled(false);
  }

  public void setActive(boolean active){
    symbolEditor.setEnabled(active);
    stateEditor.setEnabled(active);
    actionEditor.setEnabled(active);
  }
  
  private void setEditorEnabled(boolean enabled) {
    symbolEditor.setEnabled(enabled);
    stateEditor.setEnabled(enabled);
    actionEditor.setEnabled(enabled);
  }

  private void updateSymbol(String symbol) {
    assert symbol != null;
    updateCell(symbol, null, null, true, false, false);
  }

  private void updateAction(MachineAction action) {
    assert action != null;
    updateCell(null, action, null, false, true, false);
  }

  private void updateState(String state) {
    assert state != null;
    updateCell(null, null, state, false, false, true);
  }

  private void updateCell(
    String symbol, MachineAction action, String state,
    boolean updateSymbol, boolean updateAction, boolean updateState
  ) {
    final int x = table.getSelectedColumn();
    final int y = table.getSelectedRow();
    if(x <= 0 || y < 0) {
      return;
    }
    Symbol oldSymbol = symbols.values()[y];
    Symbol newSymbol = updateSymbol ? symbols.get(symbol) : null;
    State oldState = states.values()[x - 1];
    State newState = updateState ? states.get(state) : null;
    Transition.Left k = new Transition.Left(oldSymbol, oldState);
    Transition.Right v = transitions.getTransition(k);

    if (updateSymbol) {
      v.setSymbol(newSymbol);
    }
    if (updateAction) {
      v.setMachineAction(action);
    }
    if (updateState) {
      v.setState(newState);
    }

    table.repaint();
  }
}
