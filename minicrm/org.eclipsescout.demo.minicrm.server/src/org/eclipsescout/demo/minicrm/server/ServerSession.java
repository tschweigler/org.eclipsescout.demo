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
package org.eclipsescout.demo.minicrm.server;

import java.io.Serializable;
import java.security.AccessController;

import javax.security.auth.Subject;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.server.AbstractServerSession;
import org.eclipse.scout.rt.server.ServerJob;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.shared.services.process.IUserProcessService;

public class ServerSession extends AbstractServerSession implements Serializable {
  private static final long serialVersionUID = 1L;
  private static IScoutLogger logger = ScoutLogManager.getLogger(ServerSession.class);

  public ServerSession() {
    super(true);
  }

  /**
   * @return session in current ThreadContext
   */
  public static ServerSession get() {
    return ServerJob.getCurrentSession(ServerSession.class);
  }

  @Override
  protected void execLoadSession() throws ProcessingException {
    //logger.info("created a new session for " + getUserId());
    if (getUserId() != null && Subject.getSubject(AccessController.getContext()) != Activator.getDefault().getBackendSubject()) {
      logger.info("created a new session for " + getUserId());
      setPermission(SERVICES.getService(IUserProcessService.class).getUserPermission(getUserId()));

      SERVICES.getService(IUserProcessService.class).registerUser();
    }
  }

  @SuppressWarnings("unchecked")
  @FormData
  public ICode<Integer> getPermission() {
    return getSharedContextVariable(IUserProcessService.PERMISSION_KEY, ICode.class);
  }

  @FormData
  public void setPermission(ICode<Integer> newValue) {
    setSharedContextVariable(IUserProcessService.PERMISSION_KEY, ICode.class, newValue);
  }
}
