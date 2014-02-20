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
package org.eclipsescout.demo.minicrm.client.ui.forms;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.plannerfield.AbstractPlannerField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.extension.client.ui.basic.activitymap.AbstractExtensibleActivityMap;
import org.eclipse.scout.rt.extension.client.ui.basic.table.AbstractExtensibleTable;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.PlannerTestField;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.PlannerTestField.ActivityMap;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.PlannerTestField.ResourceTable;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.UserBox.CancelButton;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.UserBox.OkButton;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.UserBox.PasswordField;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.UserBox.UserRoleField;
import org.eclipsescout.demo.minicrm.client.ui.forms.UserForm.MainBox.UserBox.UsernameField;
import org.eclipsescout.demo.minicrm.shared.security.CreateUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.UpdateUserPermission;
import org.eclipsescout.demo.minicrm.shared.services.code.UserRoleCodeType;
import org.eclipsescout.demo.minicrm.shared.services.code.UserRoleCodeType.UserCode;
import org.eclipsescout.demo.minicrm.shared.services.process.IUserProcessService;
import org.eclipsescout.demo.minicrm.shared.services.process.UserFormData;
import org.eclipsescout.demo.minicrm.shared.util.SharedUserUtility;

@FormData(value = UserFormData.class, sdkCommand = SdkCommand.CREATE)
public class UserForm extends AbstractForm {

  private Long m_userId;

  public UserForm() throws ProcessingException {
    super();
  }

  @FormData
  public Long getUserId() {
    return m_userId;
  }

  @FormData
  public void setUserId(Long userId) {
    m_userId = userId;
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  public PlannerTestField getPlannerTestField() {
    return getFieldByClass(PlannerTestField.class);
  }

  public UserRoleField getUserRoleField() {
    return getFieldByClass(UserRoleField.class);
  }

  public UsernameField getUsernameField() {
    return getFieldByClass(UsernameField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected int getConfiguredGridColumnCount() {
      return 1;
    }

    @Order(10.0)
    public class UserBox extends AbstractGroupBox {

      @Override
      protected int getConfiguredGridColumnCount() {
        return 1;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("User");
      }

      public class tstButton extends AbstractButton {

        @Override
        protected String getConfiguredLabel() {
          return "test Split";
        }

        @Override
        protected void execClickAction() throws ProcessingException {
          //getPlannerTestField().setSplitterPosition(299);
          getPlannerTestField().setSplitterPosition(300);
        }

      }

      @Order(10.0)
      public class UsernameField extends AbstractStringField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Username");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 32;
        }

        @Override
        protected String execValidateValue(String rawValue) throws ProcessingException {
          SharedUserUtility.checkUsername(rawValue);
          return rawValue;
        }
      }

      @Order(20.0)
      public class PasswordField extends AbstractStringField {

        @Override
        protected boolean getConfiguredInputMasked() {
          return true;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Password");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 64;
        }

        @Override
        protected String execValidateValue(String rawValue) throws ProcessingException {
          SharedUserUtility.checkPassword(rawValue);
          return rawValue;
        }
      }

      @Order(30.0)
      public class UserRoleField extends AbstractSmartField<Integer> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("UserRole");
        }

        @Override
        protected Class<? extends ICodeType<Integer>> getConfiguredCodeType() {
          return UserRoleCodeType.class;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected void execInitField() throws ProcessingException {
          setValue(UserCode.ID);
        }
      }

      @Order(40.0)
      public class OkButton extends AbstractOkButton {
      }

      @Order(50.0)
      public class CancelButton extends AbstractCancelButton {
      }
    }

    @Order(20.0)
    public class PlannerTestField extends AbstractPlannerField<ResourceTable, ActivityMap, Long, Long> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("PlannerTest");
      }

      @Override
      protected Object[][] execLoadActivityMapData(Long[] resourceIds, ITableRow[] resourceRows) throws ProcessingException {
        return new Object[0][];
      }

      @Order(10.0)
      public class ResourceTable extends AbstractExtensibleTable {

        @Order(10.0f)
        public class ResourceIdColumn extends AbstractLongColumn {

          @Override
          protected String getConfiguredHeaderText() {
            return "ResourceId";
          }

          @Override
          protected boolean getConfiguredDisplayable() {
            return true;
          }

          @Override
          protected boolean getConfiguredPrimaryKey() {
            return true;
          }
        }

        @Override
        protected boolean getConfiguredAutoResizeColumns() {
          return true;
        }

        @Override
        protected boolean getConfiguredSortEnabled() {
          return false;
        }
      }

      @Override
      protected int getConfiguredGridH() {
        return 5;
      }

      @Override
      protected int getConfiguredMiniCalendarCount() {
        return 1;
      }

      @Order(10.0)
      public class ActivityMap extends AbstractExtensibleActivityMap<Long, Long> {

      }
    }
  }

  public class ModifyHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() throws ProcessingException {
      setEnabledPermission(new UpdateUserPermission());
      getPasswordField().setVisibleGranted(false);
      getPasswordField().setMandatory(false);
    }

    @Override
    protected void execStore() throws ProcessingException {
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      SERVICES.getService(IUserProcessService.class).updateUser(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() throws ProcessingException {
      setEnabledPermission(new CreateUserPermission());
    }

    @Override
    protected void execStore() throws ProcessingException {
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      SERVICES.getService(IUserProcessService.class).createUser(formData);
    }
  }
}
