/*******************************************************************************
 * Copyright (c) 2013 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipsescout.demo.widgets.client.ui.forms;

import java.util.Date;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractProposalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCloseButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipsescout.demo.widgets.client.services.lookup.CompanyLookupCall;
import org.eclipsescout.demo.widgets.client.services.lookup.GenderLookupCall;
import org.eclipsescout.demo.widgets.client.ui.forms.DetailForm.MainBox.GroupBox.ValueLastField;
import org.eclipsescout.demo.widgets.client.ui.forms.TableFieldEditableForm.MainBox.CloseButton;
import org.eclipsescout.demo.widgets.client.ui.forms.TableFieldEditableForm.MainBox.GroupBox;
import org.eclipsescout.demo.widgets.client.ui.forms.TableFieldEditableForm.MainBox.GroupBox.EditableTableField;

public class TableFieldEditableForm extends AbstractForm implements IPageForm {

  public TableFieldEditableForm() throws ProcessingException {
    super();
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TableFieldEditable");
  }

  @Override
  public void startPageForm() throws ProcessingException {
    startInternal(new PageFormHandler());
  }

  @Override
  public CloseButton getCloseButton() {
    return getFieldByClass(CloseButton.class);
  }

  public EditableTableField getEditableTableField() {
    return getFieldByClass(EditableTableField.class);
  }

  public GroupBox getGroupBox() {
    return getFieldByClass(GroupBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public ValueLastField getValueLastField() {
    return getFieldByClass(ValueLastField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class GroupBox extends AbstractGroupBox {

      @Order(10.0)
      public class EditableTableField extends AbstractTableField<EditableTableField.Table> {

        @Override
        protected int getConfiguredGridH() {
          return 8;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected void execInitField() throws ProcessingException {
          Object data[][] = new Object[][]{
              {1L, "Ralph Mueller", new Date(), "Eclipse", 2l},
              {2L, "Andreas Hoegger", new Date(), "Business Systems Integration AG", 2l},
              {3L, "Matthias Zimmermann", new Date(), "BSI AG", null},
              {4L, "Homer Simpson", new Date(), null, 2l},
              {5L, "Mart Simpson", new Date(), null, 1l}};
          getTable().addRowsByMatrix(data);
        }

        @Order(10.0)
        public class Table extends AbstractTable {

          @Override
          protected boolean getConfiguredMultiSelect() {
            return false;
          }

          public PersonNrColumn getCompanyNrColumn() {
            return getColumnSet().getColumnByClass(PersonNrColumn.class);
          }

          public NameColumn getNameColumn() {
            return getColumnSet().getColumnByClass(NameColumn.class);
          }

          public BirthdayColumn getBirthdayColumn() {
            return getColumnSet().getColumnByClass(BirthdayColumn.class);
          }

          public CompanyColumn getCompanyColumn() {
            return getColumnSet().getColumnByClass(CompanyColumn.class);
          }

          public GenderColumn getGenderColumn() {
            return getColumnSet().getColumnByClass(GenderColumn.class);
          }

          @Order(10.0)
          public class PersonNrColumn extends AbstractLongColumn {
            @Override
            protected boolean getConfiguredDisplayable() {
              return false;
            }
          }

          @Order(20.0)
          public class NameColumn extends AbstractStringColumn {

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Name");
            }

            @Override
            protected boolean execIsEditable(ITableRow row) throws ProcessingException {
              if (getCompanyNrColumn().getValue(row) % 2 == 0) {
                return super.execIsEditable(row);
              }
              return false;
            }
          }

          @Order(30.0)
          public class BirthdayColumn extends AbstractDateColumn {

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Symbol");
            }

          }

          @Order(30.0)
          public class CompanyColumn extends AbstractProposalColumn<Long> {

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
              return CompanyLookupCall.class;
            }

            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Company");
            }

          }

          @Order(30.0)
          public class GenderColumn extends AbstractSmartColumn<Long> {

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
              return GenderLookupCall.class;
            }

            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Gender");
            }

          }
        }
      }
    }

    @Order(20.0)
    public class CloseButton extends AbstractCloseButton {
    }
  }

  public class PageFormHandler extends AbstractFormHandler {
  }
}
