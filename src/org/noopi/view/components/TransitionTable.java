package org.noopi.view.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.noopi.utils.IDatabase;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.view.components.model.TransitionTableCellRenderer;
import org.noopi.view.components.model.TransitionTableModel;

public class TransitionTable extends JPanel {
  private final JTable table;

  public TransitionTable(
    IDatabase<String, Symbol> symbols,
    IDatabase<String, State> states
  ) {
    assert symbols != null;
    assert states != null;
    table = new JTable(new TransitionTableModel(symbols, states));
    setLayout(new BorderLayout());

    JPanel editor = new JPanel(new FlowLayout());
    editor.add(new JComboBox<>());
    editor.add(new JComboBox<>());
    editor.add(new JComboBox<>());
    add(editor, BorderLayout.NORTH);

    JPanel center = new JPanel(new BorderLayout());
    center.add(table.getTableHeader(), BorderLayout.NORTH);
    center.add(table, BorderLayout.CENTER);
    add(center, BorderLayout.CENTER);

    table.setRowHeight(40);
  }
}
