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
package org.eclipsescout.demo.minicrm.shared.services.process;

import java.util.Map;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.ValidationRule;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;
import org.eclipsescout.demo.minicrm.shared.services.code.UserRoleCodeType;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated, no manual modifications recommended.
 * 
 * @generated
 */
public class UserFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public UserFormData() {
  }

  public Password getPassword() {
    return getFieldByClass(Password.class);
  }

  /**
   * access method for property UserId.
   */
  public Long getUserId() {
    return getUserIdProperty().getValue();
  }

  /**
   * access method for property UserId.
   */
  public void setUserId(Long userId) {
    getUserIdProperty().setValue(userId);
  }

  public UserIdProperty getUserIdProperty() {
    return getPropertyByClass(UserIdProperty.class);
  }

  public UserRole getUserRole() {
    return getFieldByClass(UserRole.class);
  }

  public Username getUsername() {
    return getFieldByClass(Username.class);
  }

  public static class Password extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;

    public Password() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.MANDATORY, true);
      ruleMap.put(ValidationRule.MAX_LENGTH, 64);
    }
  }

  public static class UserIdProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;

    public UserIdProperty() {
    }
  }

  public static class UserRole extends AbstractValueFieldData<Integer> {

    private static final long serialVersionUID = 1L;

    public UserRole() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.CODE_TYPE, UserRoleCodeType.class);
      ruleMap.put(ValidationRule.MANDATORY, true);
      ruleMap.put(ValidationRule.ZERO_NULL_EQUALITY, true);
    }
  }

  public static class Username extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;

    public Username() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.MANDATORY, true);
      ruleMap.put(ValidationRule.MAX_LENGTH, 32);
    }
  }
}
