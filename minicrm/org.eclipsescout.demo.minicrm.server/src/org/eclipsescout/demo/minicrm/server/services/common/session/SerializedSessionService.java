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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.serialization.IObjectSerializer;
import org.eclipse.scout.commons.serialization.SerializationUtility;
import org.eclipse.scout.rt.server.services.common.session.AbstractSessionStoreService;

public class SerializedSessionService extends AbstractSessionStoreService {

  @Override
  public void setAttribute(HttpServletRequest req, HttpServletResponse res, String key, Object value) {
    if (value != null) {
      req.getSession().setAttribute(key, StringUtility.bytesToHex(serialize(value)));
    }
  }

  @Override
  public Object getAttribute(HttpServletRequest req, HttpServletResponse res, String key) {
    String hex = (String) req.getSession().getAttribute(key);
    if (hex != null) {
      return deserialize(StringUtility.hexToBytes(hex));
    }
    else {
      return null;
    }
  }

  private byte[] serialize(Object obj) {

    byte[] bytes = null;

    try {
      IObjectSerializer objs = SerializationUtility.createObjectSerializer();
      bytes = objs.serialize(obj);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return bytes;
  }

  private Object deserialize(byte[] bytes) {

    Object obj = null;

    try {
      if (bytes != null) {
        IObjectSerializer objs = SerializationUtility.createObjectSerializer();
        obj = objs.deserialize(bytes, Object.class);
      }
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return obj;
  }
}
