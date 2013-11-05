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

import java.util.Set;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.validate.IValidationStrategy;
import org.eclipse.scout.rt.shared.validate.InputValidation;
import org.eclipse.scout.service.IService;

@InputValidation(IValidationStrategy.PROCESS.class)
public interface IUserProcessService extends IService {

  static final String PERMISSION_KEY = "permission_id";

  public void registerUser() throws ProcessingException;

  public void unregisterUser() throws ProcessingException;

  public Set<String> getUsersOnline() throws ProcessingException;

  ICode<Integer> getUserPermission(String userName) throws ProcessingException;

  Object[][] getUsers() throws ProcessingException;

  void deleteUser(Long[] u_id) throws ProcessingException;

  void createUser(UserFormData formData) throws ProcessingException;

  void updateUser(UserFormData formData) throws ProcessingException;
}
