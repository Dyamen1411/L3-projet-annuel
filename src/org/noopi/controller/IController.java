package org.noopi.controller;

import javax.swing.JFrame;

import org.noopi.utils.listeners.view.AddRuleEventListener;

public interface IController {
  void createFrameController(final JFrame frame);

  AddRuleEventListener createAddRuleEventListener();
  // ...
}
