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
package org.eclipsescout.demo.minicrm.shared.notification;

import org.eclipse.scout.rt.shared.services.common.clientnotification.AbstractClientNotification;
import org.eclipse.scout.rt.shared.services.common.clientnotification.IClientNotification;

public class MessageNotification extends AbstractClientNotification {

  private static final long serialVersionUID = 1L;

  private final String m_message;
  private final String m_sender;

  public MessageNotification(String senderName, String message) {
    m_sender = senderName;
    m_message = message;
  }

  @Override
  public boolean coalesce(IClientNotification existingNotification) {
    return false;
  }

  public String getMessage() {
    return m_message;
  }

  public String getSenderName() {
    return m_sender;
  }
}
