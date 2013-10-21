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
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.spy.memcached.MemcachedClient;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.commons.serialization.IObjectSerializer;
import org.eclipse.scout.commons.serialization.SerializationUtility;
import org.eclipse.scout.rt.server.services.common.session.AbstractSessionStoreService;

public class AmazonSessionService extends AbstractSessionStoreService {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(AmazonSessionService.class);
  // String configEndpoint = "scoutcache.zjs9xm.cfg.use1.cache.amazonaws.com";
  String configEndpoint = "localhost";
  String cookieStoreKey = "cookies";
  String cookieName = "clientid";
  Integer clusterPort = 11211;
  Integer cacheTime = 3600;

  MemcachedClient client;

  public AmazonSessionService() {
    try {
      LOG.info("Starte Initialisierung MemcachedClient");
      client = new MemcachedClient(new InetSocketAddress(configEndpoint, clusterPort));
      client.set(cookieStoreKey, cacheTime, new ArrayList<String>());
      LOG.info("Initialisierung MemcachedClient abgeschlossen");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setAttribute(HttpServletRequest req, HttpServletResponse res, String key, Object value) {
    if (value != null) {
      String clientid = getClientId(req, res);
      byte[] bytes = serialize(value);
      String str = StringUtility.bytesToHex(bytes);
      LOG.info("Speichern des Strings: \n" + str);
      client.set(clientid + '_' + key, cacheTime, str);
    }
  }

  @Override
  public Object getAttribute(HttpServletRequest req, HttpServletResponse res, String key) {
    String clientid = getClientId(req, res);
    String str = (String) client.get(clientid + '_' + key);
    if (str != null) {
      LOG.info("Laden des Strings : \n" + str);
      byte[] bytes = StringUtility.hexToBytes(str);
      return deserialize(bytes);
    }
    else {
      return null;
    }
  }

  private String getClientId(HttpServletRequest req, HttpServletResponse res) {
    Cookie[] cookies = req.getCookies();

    for (int i = 0; i < cookies.length; i++) {
      Cookie cookie = cookies[i];

      if (cookie.getName().equals(cookieName)) {
        LOG.info("ClientdId gefunden: " + cookie.getValue());
        return cookie.getValue();
      }
    }
    if (req.getAttribute(cookieName) != null) {
      LOG.info("Innerhalb dieses Calls wurde bereits ein Cookie gesetzt");
      return (String) req.getAttribute(cookieName);
    }

    return getNewClientId(req, res);
  }

  private String getNewClientId(HttpServletRequest req, HttpServletResponse res) {
    do {
      //TODO Löschen alter CookieKeys
      String newClientId = new BigInteger(130, new SecureRandom()).toString(64);
      LOG.info("Neu ClientID erstellt: " + newClientId);

      @SuppressWarnings("unchecked")
      ArrayList<String> existingClientIds = (ArrayList<String>) client.get(cookieStoreKey);
      if (existingClientIds == null) {
        LOG.info("ClientID Array neu angelegt");
        existingClientIds = new ArrayList<String>();
      }
      if (!existingClientIds.contains(newClientId)) {
        Cookie cookie = new Cookie(cookieName, newClientId);
        req.setAttribute(cookieName, newClientId);
        res.addCookie(cookie);
        existingClientIds.add(newClientId);
        //TODO Sollte höhere Cachetime haben -> unendlich
        client.set(cookieStoreKey, cacheTime, existingClientIds);
        LOG.info("Neue ClientdId angelegt: " + cookie.getValue());
        return newClientId;
      }

    }
    while (true);
  }

  private byte[] serialize(Object obj) {
    LOG.info("Starte Serialisierung von" + obj.getClass().getName());
    byte[] bytes = null;

    try {
      IObjectSerializer objs = SerializationUtility.createObjectSerializer();
      bytes = objs.serialize(obj);
      LOG.info("Serialisierung erfolgreich");
    }
    catch (IOException e) {
      e.printStackTrace();
      LOG.info("Serialisierung fehlgeschlagen");
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
