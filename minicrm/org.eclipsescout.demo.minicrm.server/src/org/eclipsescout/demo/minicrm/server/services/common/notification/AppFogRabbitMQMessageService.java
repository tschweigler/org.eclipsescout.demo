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
package org.eclipsescout.demo.minicrm.server.services.common.notification;

import org.eclipse.scout.cloud.notification.rabbitmq.RabbitMQMessageService;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.commons.serialization.SerializationUtility;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.shared.services.IAppFogConfigService;
import org.osgi.framework.ServiceRegistration;

public class AppFogRabbitMQMessageService extends RabbitMQMessageService {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(RabbitMQMessageService.class);

  @SuppressWarnings("rawtypes")
  @Override
  public void initializeService(ServiceRegistration registration) {
    IAppFogConfigService service = SERVICES.getService(IAppFogConfigService.class);

    String uri = service.getRabbitMqUri();

    LOG.error("Set URI for RabbitMQ: " + uri);

    setUri(uri);
    setObjcet(SerializationUtility.createObjectSerializer());
  }

}
