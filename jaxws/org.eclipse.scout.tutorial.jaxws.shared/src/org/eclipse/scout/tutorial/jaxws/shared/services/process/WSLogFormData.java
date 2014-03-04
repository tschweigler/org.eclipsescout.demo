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
package org.eclipse.scout.tutorial.jaxws.shared.services.process;

import java.util.Map;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.ValidationRule;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated, no manual modifications recommended.
 * 
 * @generated
 */
public class WSLogFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public WSLogFormData() {
  }

  public Date getDate() {
    return getFieldByClass(Date.class);
  }

  public Operation getOperation() {
    return getFieldByClass(Operation.class);
  }

  public Port getPort() {
    return getFieldByClass(Port.class);
  }

  public Request getRequest() {
    return getFieldByClass(Request.class);
  }

  public Response getResponse() {
    return getFieldByClass(Response.class);
  }

  public Service getService() {
    return getFieldByClass(Service.class);
  }

  /**
   * access method for property WSLogNr.
   */
  public Long getWSLogNr() {
    return getWSLogNrProperty().getValue();
  }

  /**
   * access method for property WSLogNr.
   */
  public void setWSLogNr(Long wSLogNr) {
    getWSLogNrProperty().setValue(wSLogNr);
  }

  public WSLogNrProperty getWSLogNrProperty() {
    return getPropertyByClass(WSLogNrProperty.class);
  }

  public static class Date extends AbstractValueFieldData<java.util.Date> {

    private static final long serialVersionUID = 1L;

    public Date() {
    }
  }

  public static class Operation extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;

    public Operation() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.MAX_LENGTH, 4000);
    }
  }

  public static class Port extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;

    public Port() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.MAX_LENGTH, 4000);
    }
  }

  public static class Request extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;

    public Request() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.MAX_LENGTH, Integer.MAX_VALUE);
    }
  }

  public static class Response extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;

    public Response() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.MAX_LENGTH, Integer.MAX_VALUE);
    }
  }

  public static class Service extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;

    public Service() {
    }

    /**
     * list of derived validation rules.
     */
    @Override
    protected void initValidationRules(Map<String, Object> ruleMap) {
      super.initValidationRules(ruleMap);
      ruleMap.put(ValidationRule.MAX_LENGTH, 4000);
    }
  }

  public static class WSLogNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;

    public WSLogNrProperty() {
    }
  }
}
