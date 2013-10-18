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
package org.eclipsescout.demo.minicrm.server.services.common.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.rt.server.services.common.session.AbstractSessionStoreService;
import org.eclipse.scout.service.IService2;

public class NoSessionService extends AbstractSessionStoreService implements IService2 {
  @Override
  public void setAttribute(HttpServletRequest req, HttpServletResponse res, String key, Object value) {
  }

  @Override
  public Object getAttribute(HttpServletRequest req, HttpServletResponse res, String key) {
    return null;
  }
}
