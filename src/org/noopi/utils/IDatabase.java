package org.noopi.utils;

import org.noopi.utils.exceptions.DatabaseDuplicateException;
import org.noopi.utils.exceptions.DatabaseMissingEntryException;

public interface IDatabase<R, T> extends IReadableDatabase<R, T> {

  T registerEntry(R name) throws DatabaseDuplicateException;

  void unregisterEntry(R name) throws DatabaseMissingEntryException;

  IReadableDatabase<R, T> toReadable();
}
