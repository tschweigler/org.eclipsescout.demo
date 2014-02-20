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
package org.eclipsescout.demo.minicrm.server.services.notification;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.server.services.common.notification.IDistributedNotification;
import org.eclipse.scout.rt.server.services.common.notification.IDistributedNotificationListener;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.shared.services.process.IUserProcessService;

/**
 *
 */
public class RegisterUserNotificationListener implements IDistributedNotificationListener {

  @Override
  public void onNewNotification(IDistributedNotification notification) {
    if (isInteresting(notification)) {
      RegisterUserNotification registerUserNotification = (RegisterUserNotification) notification.getNotification();
      try {
        SERVICES.getService(IUserProcessService.class).registerUserInternal(registerUserNotification.getUserName());
      }
      catch (ProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onUpdateNotification(IDistributedNotification notification) {
  }

  @Override
  public void onRemoveNotification(IDistributedNotification notification) {
  }

  @Override
  public boolean isInteresting(IDistributedNotification notification) {
    return (notification.getNotification() instanceof RegisterUserNotification);
  }

}
