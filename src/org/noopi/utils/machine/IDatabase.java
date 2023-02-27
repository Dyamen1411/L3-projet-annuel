package org.noopi.utils.machine;

import org.noopi.utils.exceptions.DatabaseDuplicateException;
import org.noopi.utils.exceptions.DatabaseMissingEntryException;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public interface IDatabase<R, T> {
  boolean contains(R name);

  T registerEntry(R name) throws DatabaseDuplicateException;
  void unregisterEntry(R name) throws DatabaseMissingEntryException;

  void addDatabaseRegisterEventListener(DatabaseRegisterEventListener<T> l);
  void addDatabaseUnregisterEventListener(DatabaseUnregisterEventListener<T> l);
}
