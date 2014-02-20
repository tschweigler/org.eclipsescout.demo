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
package org.eclipsescout.demo.minicrm.server.services.process;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.commons.holders.IntegerHolder;
import org.eclipse.scout.commons.holders.NVPair;
import org.eclipse.scout.rt.server.services.common.jdbc.SQL;
import org.eclipse.scout.rt.server.services.common.notification.INotificationService;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.service.AbstractService;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.server.ServerSession;
import org.eclipsescout.demo.minicrm.server.services.notification.RegisterUserNotification;
import org.eclipsescout.demo.minicrm.server.services.notification.UnregisterUserNotification;
import org.eclipsescout.demo.minicrm.server.util.UserUtility;
import org.eclipsescout.demo.minicrm.shared.security.CreateUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.DeleteUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.ReadUsersPermission;
import org.eclipsescout.demo.minicrm.shared.security.RegisterUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.UnregisterUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.UpdateUserPermission;
import org.eclipsescout.demo.minicrm.shared.services.code.UserRoleCodeType;
import org.eclipsescout.demo.minicrm.shared.services.process.INotificationProcessService;
import org.eclipsescout.demo.minicrm.shared.services.process.IUserProcessService;
import org.eclipsescout.demo.minicrm.shared.services.process.UserFormData;
import org.osgi.framework.ServiceRegistration;

public class UserProcessService extends AbstractService implements IUserProcessService {
  private Set<String> m_users;

  @Override
  public void initializeService(ServiceRegistration registration) {
    super.initializeService(registration);
    m_users = Collections.synchronizedSet(new HashSet<String>());
  }

  @Override
  public void registerUser() throws ProcessingException {
    if (!ACCESS.check(new RegisterUserPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    m_users.add(ServerSession.get().getUserId());
    SERVICES.getService(INotificationProcessService.class).sendRefreshBuddiesInternal();
    SERVICES.getService(INotificationService.class).publishNotification(new RegisterUserNotification(ServerSession.get().getUserId()));
  }

  @Override
  public void registerUserInternal(String userId) throws ProcessingException {
    //TODO TSW [13] prüfen warum das mit der BackendSession nicht geht
//    if (!ACCESS.check(new RegisterUserPermission())) {
//      throw new VetoException(TEXTS.get("AuthorizationFailed"));
//    }
    m_users.add(userId);
    SERVICES.getService(INotificationProcessService.class).sendRefreshBuddiesInternal();
  }

  @Override
  public void unregisterUser() throws ProcessingException {
    if (!ACCESS.check(new UnregisterUserPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    m_users.remove(ServerSession.get().getUserId());
    SERVICES.getService(INotificationProcessService.class).sendRefreshBuddiesInternal();
    SERVICES.getService(INotificationService.class).publishNotification(new UnregisterUserNotification(ServerSession.get().getUserId()));
  }

  @Override
  public void createUser(UserFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateUserPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    UserUtility.createNewUser(formData.getUsername().getValue(), formData.getPassword().getValue(), formData.getUserRole().getValue());
  }

  @Override
  public void deleteUser(Long[] u_id) throws ProcessingException {
    if (!ACCESS.check(new DeleteUserPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    // check that we are not deleting ourselves
    IntegerHolder holder = new IntegerHolder();
    SQL.selectInto("SELECT u_id FROM TABUSERS WHERE username = :username INTO :myId", new NVPair("myId", holder), new NVPair("username", ServerSession.get().getUserId()));
    for (Long uid : u_id) {
      if (uid.equals(holder.getValue().longValue())) {
        throw new VetoException(TEXTS.get("CannotDeleteYourself"));
      }
    }

    SQL.delete("DELETE FROM TABUSERS WHERE u_id = :ids", new NVPair("ids", u_id));

    //TODO: what to do if the deleted user is still logged in somewhere?
  }

  @Override
  public void updateUser(UserFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateUserPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    SQL.update("UPDATE TABUSERS SET username = :newUsername, permission_id = :newPermId WHERE u_id = :uid",
        new NVPair("newUsername", formData.getUsername().getValue()), new NVPair("newPermId", formData.getUserRole().getValue()), new NVPair("uid", formData.getUserId()));
  }

  @Override
  public Object[][] getUsers() throws ProcessingException {
    if (!ACCESS.check(new ReadUsersPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    return SQL.select("SELECT u_id, username, permission_id FROM TABUSERS");
  }

  @Override
  public Set<String> getUsersOnline() throws ProcessingException {
    if (!ACCESS.check(new ReadUsersPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    return m_users;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ICode<Integer> getUserPermission(String userName) throws ProcessingException {
    IntegerHolder ih = new IntegerHolder(0);
    SQL.selectInto("SELECT permission_id FROM TABUSERS WHERE username = :username INTO :permission", new NVPair("username", userName), new NVPair("permission", ih));

    return CODES.getCodeType(UserRoleCodeType.class).getCode(ih.getValue());
  }

  @Override
  public void unregisterUserInternal(String userName) throws ProcessingException {
    m_users.remove(userName);
    SERVICES.getService(INotificationProcessService.class).sendRefreshBuddiesInternal();
  }
}
