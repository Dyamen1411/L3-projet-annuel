package org.noopi.view.components;

import javax.swing.JTextField;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Color;

public class HintableTextField extends JTextField {
  private String hint;
  private boolean hinted;

  public HintableTextField() {
    this("", "");
  }

  public HintableTextField(String text) {
    this(text, "");
  }

  public HintableTextField(String text, String hint) {
    super(text);
    this.hint = hint;
    hint();
    
    addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        restore();
      }
      @Override
      public void focusLost(FocusEvent e) {
        hint();
      }
    });
  }

  public void setHint(String hint) {
    this.hint = hint == null
      ? ""
      : hint;
  }

  public String getHint() {
    return hint;
  }

  @Override
  public String getText() {
    System.out.println("bite");
    return hinted
      ? ""
      : super.getText();
  }

  private void restore() {
    if (hinted) {
      setText("");
      setForeground(Color.BLACK);
    }
  }

  private void hint() {
    if (hinted = super.getText().isEmpty()) {
      setForeground(Color.GRAY);
      setText(hint);
    }
  }
}
