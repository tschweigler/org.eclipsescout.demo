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
public class CloudFoundryConfigService implements IAppFogConfigService {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(CloudFoundryConfigService.class);

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
      LOG.error(System.getenv("VCAP_SERVICES"));
      //json = new JSONObject(System.getenv("VCAP_SERVICES"));
      json = new JSONObject("{\"cloudamqp-n/a\":[{\"name\":\"scoutcloud-rabbitmq\",\"label\":\"cloudamqp-n/a\",\"tags\":[\"amqp\",\"rabbitmq\"],\"plan\":\"lemur\",\"credentials\":{\"uri\":\"amqp://wepmwtea:mBFQ90S1TMK4pqXkxUrr9XIc_iKLukHS@tiger.cloudamqp.com/wepmwtea\"}}],\"rediscloud-n/a\":[{\"name\":\"scoutcloud-cache\",\"label\":\"rediscloud-n/a\",\"tags\":[\"redis\",\"key-value\"],\"plan\":\"25mb\",\"credentials\":{\"port\":\"14474\",\"hostname\":\"pub-redis-14474.us-east-1-3.1.ec2.garantiadata.com\",\"password\":\"0ADbjaTMgcZj2J26\"}}],\"cleardb-n/a\":[{\"name\":\"scoutcloud-mysql\",\"label\":\"cleardb-n/a\",\"tags\":[\"mysql\",\"relational\"],\"plan\":\"spark\",\"credentials\":{\"jdbcUrl\":\"jdbc:mysql://b74b0663848113:3c67496e@us-cdbr-east-05.cleardb.net:3306/ad_2fcb2fb8caf0220\",\"uri\":\"mysql://b74b0663848113:3c67496e@us-cdbr-east-05.cleardb.net:3306/ad_2fcb2fb8caf0220?reconnect=true\",\"name\":\"ad_2fcb2fb8caf0220\",\"hostname\":\"us-cdbr-east-05.cleardb.net\",\"port\":\"3306\",\"username\":\"b74b0663848113\",\"password\":\"3c67496e\"}}]} ");
    }
    catch (JSONException e) {
      LOG.error("Unable to load configuration from VCAP_SERVICES");
      return;
    }

    try {
      jsonArray = (JSONArray) json.get("cleardb-n/a");
      jsonObject = (JSONObject) jsonArray.get(0);
      credentials = (JSONObject) jsonObject.get("credentials");
      mysqlCredentials.put("port", ((String) credentials.get("port")));
      mysqlCredentials.put("username", (String) credentials.get("username"));
      mysqlCredentials.put("name", (String) credentials.get("name"));
      mysqlCredentials.put("host", (String) credentials.get("hostname"));
      mysqlCredentials.put("password", (String) credentials.get("password"));
    }
    catch (JSONException e) {
      LOG.error("MySQL is not bound to the App");
    }

    try {
      jsonArray = (JSONArray) json.get("rediscloud-n/a");
      jsonObject = (JSONObject) jsonArray.get(0);
      credentials = (JSONObject) jsonObject.get("credentials");
      redisCredentials.put("port", ((String) credentials.get("port")));
      redisCredentials.put("host", (String) credentials.get("hostname"));
      redisCredentials.put("password", (String) credentials.get("password"));
    }
    catch (JSONException e) {
      LOG.error("Redis is not bound to the App");
    }

    try {
      jsonArray = (JSONArray) json.get("cloudamqp-n/a");
      jsonObject = (JSONObject) jsonArray.get(0);
      credentials = (JSONObject) jsonObject.get("credentials");
      rabbitmqCredentials.put("uri", (String) credentials.get("uri"));
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
    return rabbitmqCredentials.get("uri");
  }

}
