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
package org.eclipsescout.demo.minicrm.client;

import java.io.Serializable;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.scout.commons.UriUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.ClientJob;
import org.eclipse.scout.rt.client.services.common.clientnotification.IClientNotificationConsumerService;
import org.eclipse.scout.rt.client.servicetunnel.http.ClientHttpServiceTunnel;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.client.services.IBahBahNotificationConsumerService;
import org.eclipsescout.demo.minicrm.client.ui.desktop.Desktop;
import org.eclipsescout.demo.minicrm.shared.services.process.IUserProcessService;

public class ClientSession extends AbstractClientSession implements Serializable {
  private static final long serialVersionUID = 1L;
  private static IScoutLogger logger = ScoutLogManager.getLogger(ClientSession.class);

  public ClientSession() {
    super(true);
  }

  /**
   * @return session in current ThreadContext
   */
  public static ClientSession get() {
    return ClientJob.getCurrentSession(ClientSession.class);
  }

  @Override
  public void execLoadSession() throws ProcessingException {
    setServiceTunnel(new ClientHttpServiceTunnel(this, UriUtility.toUrl(getBundle().getBundleContext().getProperty("server.url"))));

    //pre-load all known code types
    CODES.getAllCodeTypes(org.eclipsescout.demo.minicrm.shared.Activator.PLUGIN_ID);

    setDesktop(new Desktop());

    // turn client notification polling on
    getServiceTunnel().setClientNotificationPollInterval(500L);

    // set the notification listener service (this service will be called when the client receives a notification)
    IBahBahNotificationConsumerService notificationHandlerService = SERVICES.getService(IBahBahNotificationConsumerService.class);
    SERVICES.getService(IClientNotificationConsumerService.class).addClientNotificationConsumerListener(this, notificationHandlerService);
  }

  @Override
  public void execStoreSession() throws ProcessingException {
  }

  @Override
  public void stopSession() {
    try {
      SERVICES.getService(IUserProcessService.class).unregisterUser();
    }
    catch (ProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    stopSession(IApplication.EXIT_OK);
  }
}
