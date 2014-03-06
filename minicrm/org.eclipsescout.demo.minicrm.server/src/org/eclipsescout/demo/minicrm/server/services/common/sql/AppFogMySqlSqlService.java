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
package org.eclipsescout.demo.minicrm.server.services.common.sql;

import org.eclipse.scout.service.IService;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.shared.services.IAppFogConfigService;
import org.osgi.framework.ServiceRegistration;

import com.bsiag.scout.rt.server.jdbc.AbstractMySqlSqlService;

public class AppFogMySqlSqlService extends AbstractMySqlSqlService implements IService {

  @SuppressWarnings("rawtypes")
  @Override
  public void initializeService(ServiceRegistration registration) {
  }

  @Override
  protected String getConfiguredJdbcMappingName() {
    return SERVICES.getService(IAppFogConfigService.class).getMySQLURL();
  }

  @Override
  protected String getConfiguredPassword() {
    return SERVICES.getService(IAppFogConfigService.class).getMySQLPassword();
  }

  @Override
  protected String getConfiguredUsername() {
    return SERVICES.getService(IAppFogConfigService.class).getMySQLUsername();
  }
}
