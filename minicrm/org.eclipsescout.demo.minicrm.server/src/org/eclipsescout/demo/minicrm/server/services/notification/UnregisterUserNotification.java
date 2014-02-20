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

import org.eclipse.scout.rt.server.services.common.notification.INotification;

/**
 *
 */
public class UnregisterUserNotification implements INotification {

  private static final long serialVersionUID = 5245312868353225657L;
  private String m_userName;

  public UnregisterUserNotification(String username) {
    m_userName = username;
  }

  public String getUserName() {
    return m_userName;
  }
}
