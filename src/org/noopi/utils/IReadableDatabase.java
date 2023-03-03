package org.noopi.utils;

import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public interface IReadableDatabase<R, T> {
  boolean contains(R name);

  T[] values();
  R[] entries();

  void addDatabaseRegisterEventListener(DatabaseRegisterEventListener<T> l);
  void addDatabaseUnregisterEventListener(DatabaseUnregisterEventListener<T> l);
}
