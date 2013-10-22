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
package org.eclipsescout.demo.minicrm.server.services.common.cache;

import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.server.services.common.clientnotification.IClientNotificationFilter;
import org.eclipse.scout.rt.server.services.common.clientnotification.IClientNotificationQueueListener;
import org.eclipse.scout.rt.server.services.common.clientnotification.IClientNotificationService;
import org.eclipse.scout.rt.shared.services.common.clientnotification.IClientNotification;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.eclipse.scout.service.AbstractService;

/**
 *
 */
public class AmazonClientNotificationService extends AbstractService implements IClientNotificationService {
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(AmazonClientNotificationService.class);

  private AmazonClientNotificationQueue m_clientNotificationQueue;

  public AmazonClientNotificationService() {
    m_clientNotificationQueue = new AmazonClientNotificationQueue();
  }

  @Override
  public IClientNotification[] getNextNotifications(long blockingTimeout) {
    return m_clientNotificationQueue.getNextNotifications(blockingTimeout);
  }

  @Override
  @RemoteServiceAccessDenied
  public void putNotification(IClientNotification notification, IClientNotificationFilter filter) {
    m_clientNotificationQueue.putNotification(notification, filter);
  }

  @Override
  @RemoteServiceAccessDenied
  public void addClientNotificationQueueListener(IClientNotificationQueueListener listener) {
    m_clientNotificationQueue.addClientNotificationQueueListener(listener);
  }

  @Override
  @RemoteServiceAccessDenied
  public void removeClientNotificationQueueListener(IClientNotificationQueueListener listener) {
    m_clientNotificationQueue.removeClientNotificationQueueListener(listener);
  }

}
