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

import java.util.WeakHashMap;

import org.eclipse.scout.rt.server.IServerSession;
import org.eclipse.scout.rt.server.services.common.clientnotification.IClientNotificationFilter;
import org.eclipse.scout.rt.shared.services.common.clientnotification.IClientNotification;

/**
 *
 */
public class QueueElement {
  private IClientNotification m_notification;
  private IClientNotificationFilter m_filter;
  private Object m_consumedBySessionsLock;
  private WeakHashMap<IServerSession, Object> m_consumedBySessions;

  public QueueElement(IClientNotification notification, IClientNotificationFilter filter) {
    m_notification = notification;
    m_filter = filter;
    m_consumedBySessionsLock = new Object();
  }

  public IClientNotification getClientNotification() {
    return m_notification;
  }

  public IClientNotificationFilter getFilter() {
    return m_filter;
  }

  /**
   * @return true if this notifcation is already consumed by the session
   *         specified
   */
  public boolean isConsumedBy(IServerSession session) {
    // fast check
    if (session == null) {
      return false;
    }
    if (m_consumedBySessions == null) {
      return false;
    }
    //
    synchronized (m_consumedBySessionsLock) {
      if (m_consumedBySessions != null) {
        return m_consumedBySessions.containsKey(session);
      }
      else {
        return false;
      }
    }
  }

  /**
   * keeps in mind that this notifcation was consumed by the session specified
   */
  public void setConsumedBy(IServerSession session) {
    if (session != null) {
      synchronized (m_consumedBySessionsLock) {
        if (m_consumedBySessions == null) {
          m_consumedBySessions = new WeakHashMap<IServerSession, Object>();
        }
        m_consumedBySessions.put(session, null);
      }
    }
  }
}
