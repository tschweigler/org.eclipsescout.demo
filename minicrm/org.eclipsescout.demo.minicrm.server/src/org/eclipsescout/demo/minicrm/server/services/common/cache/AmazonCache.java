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

import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;

/**
 *
 */
public class AmazonCache {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(AmazonCache.class);
  String configEndpoint = "scoutcache.zjs9xm.cfg.use1.cache.amazonaws.com";
  //String configEndpoint = "localhost";
  Integer clusterPort = 11211;
  Integer cacheTime = 3600;

  private static AmazonCache instance;
  private MemcachedClient client;

  private AmazonCache() {
    try {
      LOG.info("Starte Initialisierung MemcachedClient");
      client = new MemcachedClient(new InetSocketAddress(configEndpoint, clusterPort));
      LOG.info("Initialisierung MemcachedClient abgeschlossen");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static AmazonCache getInstance() {
    if (instance == null) {
      instance = new AmazonCache();
    }
    return instance;
  }

  public OperationFuture<Boolean> add(String key, int exp, Object o) {
    return client.add(key, exp, o);
  }

  public OperationFuture<Boolean> set(String key, int exp, Object o) {
    return client.set(key, exp, o);
  }

  public Object get(String key) {
    return client.get(key);
  }

}
