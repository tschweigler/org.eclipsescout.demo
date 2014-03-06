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
package org.eclipsescout.demo.minicrm.shared.services;

import org.eclipse.scout.service.IService;

/**
 *
 */
public interface IAppFogConfigService extends IService {
  public String getMySQLURL();

  public String getMySQLPassword();

  public String getMySQLUsername();

  public String getRedisPassword();

  public String getRedisHost();

  public int getRedisPort();

  public String getRabbitMqUri();
}
