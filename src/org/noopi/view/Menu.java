package org.noopi.view;

import java.util.EnumMap;
import java.util.Map;

public enum Menu {
  FILE("Fichier"),
  EDIT("Aide");
  
  private String label;

  Menu(String lb) {
    label = lb;
  }

  String label() {
    return label;
  }

  static final Map<Menu, Item[]> STRUCT;
  static {
    STRUCT = new EnumMap<Menu, Item[]>(Menu.class);
    STRUCT.put(
      Menu.FILE,
      new Item[] {
        Item.NEW,
        Item.OPEN,
        null,
        Item.SAVE,
        Item.SAVE_AS
      }
    );
  }
}
