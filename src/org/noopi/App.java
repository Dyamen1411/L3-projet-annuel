package org.noopi;

import javax.swing.SwingUtilities;

import org.noopi.gui.Window;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new Window().display();
      }
    });
  }
}
