package org.noopi.view.components.model;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.noopi.utils.IReadableDatabase;
import org.noopi.utils.events.database.DatabaseRegisterEvent;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public class DatabaseComboboxModel<T>
  extends AbstractListModel<T>
  implements ComboBoxModel<T>
{

  private IReadableDatabase<T, ?> d;

  private Object selectedObject;

  public <E> DatabaseComboboxModel(IReadableDatabase<T, E> d) {
    assert d != null;
    this.d = d;
    d.addDatabaseRegisterEventListener(new DatabaseRegisterEventListener<E>() {
      @Override
      public void onRegisterEvent(DatabaseRegisterEvent<E> e) {
        fireIntervalAdded(this, d.size() - 1, d.size() - 1);
      }
    });
    d.addDatabaseUnregisterEventListener(
      new DatabaseUnregisterEventListener<E>() {
        @Override
        public void onUnregisterEvent(DatabaseUnregisterEvent<E> e) {
          fireIntervalAdded(this, 0, getSize() - 1);
        }
      }
    );
  }

  @Override
  public int getSize() {
    return d.size();
  }

  @Override
  public T getElementAt(int index) {
    return d.entries()[index];
  }

  @Override
  public void setSelectedItem(Object anObject) {
    if (
      (selectedObject != null && !selectedObject.equals(anObject))
      || selectedObject == null && anObject != null
    ) {
      selectedObject = anObject;
      fireContentsChanged(this, -1, -1);
    }
  }

  @Override
  public Object getSelectedItem() {
    return selectedObject;
  }
}
