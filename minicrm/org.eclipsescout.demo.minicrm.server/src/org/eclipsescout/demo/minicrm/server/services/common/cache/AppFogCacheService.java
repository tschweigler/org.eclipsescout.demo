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

import org.eclipse.scout.cloud.cachestore.redis.RedisCacheService;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.shared.services.IAppFogConfigService;
import org.osgi.framework.ServiceRegistration;

/**
 *
 */
public class AppFogCacheService extends RedisCacheService {

  @SuppressWarnings("rawtypes")
  @Override
  public void initializeService(ServiceRegistration registration) {
    IAppFogConfigService service = SERVICES.getService(IAppFogConfigService.class);
    setHost(service.getRedisHost());
    setPort(service.getRedisPort());
    setAuth(true);
    setPassword(service.getRedisPassword());
    setClient();
  }

}
