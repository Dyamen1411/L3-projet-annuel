package org.noopi.gui;

import javax.swing.JFrame;

import org.noopi.controller.IController;
import org.noopi.model.tape.ITape;
import org.noopi.model.history.ITransitionHistory;
import org.noopi.model.machine.ITuringMachine;
import org.noopi.view.IFrameLayout;

public final class Window {

  // Model
  private ITuringMachine machine;
  private ITape tape;
  private ITransitionHistory history;

  // View
  private JFrame frame;
  private IFrameLayout layout;

  public Window() {
    createModel();
    createView();
    placeComponents();
    createController();
  }

  public void display() {
    refreshView();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.pack();
  }

  private void createModel() {

  }

  private void createView() {

  }

  private void placeComponents() {

  }

  private void createController() {
    
  }

  private void refreshView() {

  }
}
