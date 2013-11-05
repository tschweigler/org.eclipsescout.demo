/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipsescout.demo.minicrm.server.services.custom.security;

import java.security.Permissions;

import org.eclipse.scout.rt.server.services.common.security.AbstractAccessControlService;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipsescout.demo.minicrm.server.ServerSession;
import org.eclipsescout.demo.minicrm.shared.security.CreateNotificationPermission;
import org.eclipsescout.demo.minicrm.shared.security.CreateUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.DeleteUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.ReadUsersPermission;
import org.eclipsescout.demo.minicrm.shared.security.RegisterUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.ResetPasswordPermission;
import org.eclipsescout.demo.minicrm.shared.security.UnregisterUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.UpdateIconPermission;
import org.eclipsescout.demo.minicrm.shared.security.UpdateUserPermission;
import org.eclipsescout.demo.minicrm.shared.services.code.UserRoleCodeType.AdministratorCode;
import org.eclipsescout.demo.minicrm.shared.services.code.UserRoleCodeType.UserCode;

public class AccessControlService extends AbstractAccessControlService {

  @Override
  protected Permissions execLoadPermissions() {
    Permissions permissions = new Permissions();

    ICode<Integer> permission = ServerSession.get().getPermission();
    if (permission != null) {
      // USERS
      if (permission.getId() >= UserCode.ID) {
        permissions.add(new RemoteServiceAccessPermission("*.shared.*", "*"));

        permissions.add(new CreateNotificationPermission());
        permissions.add(new ReadUsersPermission());
        permissions.add(new RegisterUserPermission());
        permissions.add(new UnregisterUserPermission());
        permissions.add(new UpdateIconPermission());
      }

      // ADMIN
      if (permission.getId() >= AdministratorCode.ID) {
        permissions.add(new CreateUserPermission());
        permissions.add(new DeleteUserPermission());
        permissions.add(new ResetPasswordPermission());
        permissions.add(new UpdateUserPermission());
      }
    }
    return permissions;
  }
}
