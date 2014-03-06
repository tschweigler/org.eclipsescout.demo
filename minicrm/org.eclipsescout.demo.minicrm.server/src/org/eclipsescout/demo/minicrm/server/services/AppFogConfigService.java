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
package org.eclipsescout.demo.minicrm.server.services;

import java.util.HashMap;

import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipsescout.demo.minicrm.shared.services.IAppFogConfigService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.ServiceRegistration;

/**
 *
 */
public class AppFogConfigService implements IAppFogConfigService {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(AppFogConfigService.class);

  HashMap<String, String> mysqlCredentials;
  HashMap<String, String> redisCredentials;
  HashMap<String, String> rabbitmqCredentials;

  @Override
  public void initializeService(ServiceRegistration registration) {

    JSONArray jsonArray;
    JSONObject jsonObject;
    JSONObject credentials;
    JSONObject json;

    mysqlCredentials = new HashMap<String, String>();
    redisCredentials = new HashMap<String, String>();
    rabbitmqCredentials = new HashMap<String, String>();

    try {
      json = new JSONObject(System.getenv("VCAP_SERVICES"));
    }
    catch (JSONException e) {
      LOG.error("Unable to load configuration from VCAP_SERVICES");
      return;
    }

    try {
      jsonArray = (JSONArray) json.get("mysql-5.1");
      jsonObject = (JSONObject) jsonArray.get(0);
      credentials = (JSONObject) jsonObject.get("credentials");
      mysqlCredentials.put("port", ((Integer) credentials.get("port")).toString());
      mysqlCredentials.put("username", (String) credentials.get("username"));
      mysqlCredentials.put("host", (String) credentials.get("host"));
      mysqlCredentials.put("password", (String) credentials.get("password"));
      mysqlCredentials.put("name", (String) credentials.get("name"));
    }
    catch (JSONException e) {
      LOG.error("MySQL is not bound to the App");
    }

    try {
      jsonArray = (JSONArray) json.get("redis-2.2");
      jsonObject = (JSONObject) jsonArray.get(0);
      credentials = (JSONObject) jsonObject.get("credentials");
      redisCredentials.put("port", ((Integer) credentials.get("port")).toString());
      redisCredentials.put("host", (String) credentials.get("host"));
      redisCredentials.put("password", (String) credentials.get("password"));
    }
    catch (JSONException e) {
      LOG.error("Redis is not bound to the App");
    }

    try {
      jsonArray = (JSONArray) json.get("rabbitmq-2.4");
      jsonObject = (JSONObject) jsonArray.get(0);
      credentials = (JSONObject) jsonObject.get("credentials");
      rabbitmqCredentials.put("port", ((Integer) credentials.get("port")).toString());
      rabbitmqCredentials.put("username", (String) credentials.get("username"));
      rabbitmqCredentials.put("host", (String) credentials.get("host"));
      rabbitmqCredentials.put("password", (String) credentials.get("password"));
      rabbitmqCredentials.put("url", (String) credentials.get("url"));
      rabbitmqCredentials.put("vhost", (String) credentials.get("vhost"));
    }
    catch (JSONException e) {
      LOG.error("RabbitMQ is not bound to the App");
    }

  }

  @Override
  public String getMySQLURL() {
    return "jdbc:mysql://" + mysqlCredentials.get("host") + ":" + mysqlCredentials.get("port") + "/" + mysqlCredentials.get("name");
  }

  @Override
  public String getMySQLUsername() {
    return mysqlCredentials.get("username");
  }

  @Override
  public String getMySQLPassword() {
    return mysqlCredentials.get("password");
  }

  @Override
  public String getRedisHost() {
    return redisCredentials.get("host");
  }

  @Override
  public int getRedisPort() {
    return Integer.parseInt(redisCredentials.get("port"));
  }

  @Override
  public String getRedisPassword() {
    return redisCredentials.get("password");
  }

  @Override
  public String getRabbitMqUri() {
    return rabbitmqCredentials.get("url");
  }

}
