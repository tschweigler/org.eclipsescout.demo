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
package org.eclipsescout.demo.minicrm.client.ui.desktop.outlines;

import java.util.Collection;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.ui.UserAgentUtility;
import org.eclipsescout.demo.minicrm.client.ui.desktop.outlines.pages.ClusterSynchronizationStatusTablePage;
import org.eclipsescout.demo.minicrm.client.ui.desktop.outlines.pages.UserAdministrationTablePage;
import org.eclipsescout.demo.minicrm.shared.security.CreateUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.DeleteUserPermission;
import org.eclipsescout.demo.minicrm.shared.security.ResetPasswordPermission;
import org.eclipsescout.demo.minicrm.shared.security.UpdateUserPermission;

public class AdministrationOutline extends AbstractOutline {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Administration");
  }

  @Override
  protected void execCreateChildPages(Collection<IPage> pageList) throws ProcessingException {
    UserAdministrationTablePage usersTablePage = new UserAdministrationTablePage();
    ClusterSynchronizationStatusTablePage clusterTablePage = new ClusterSynchronizationStatusTablePage();
    pageList.add(usersTablePage);
    pageList.add(clusterTablePage);
  }

  @Override
  protected void execInitTree() throws ProcessingException {
    setVisible(UserAgentUtility.isDesktopDevice() &&
        (ACCESS.check(new CreateUserPermission()) || ACCESS.check(new DeleteUserPermission()) ||
            ACCESS.check(new ResetPasswordPermission()) || ACCESS.check(new UpdateUserPermission())));
  }
}
