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
package org.eclipse.scout.cloud.sessionstore.amazon;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.spy.memcached.MemcachedClient;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.http.servletfilter.session.AbstractSessionStoreService;

public class AmazonSessionService extends AbstractSessionStoreService {

	private static final IScoutLogger LOG = ScoutLogManager.getLogger(AmazonSessionService.class);
	private Integer cacheTime = 3600;
	private String configEndpoint = "localhost";
	private Integer clusterPort = 11211;
	private MemcachedClient client;

	public AmazonSessionService() {
		super();
		try {
			client = new MemcachedClient(new InetSocketAddress(configEndpoint, clusterPort));
		} catch (IOException e) {
			LOG.info("Unable to instantiate Amazon Memcached Client");
		}
	}

	@Override
	public void setAttribute(HttpServletRequest req, HttpServletResponse res, String key, Object value) {
		if (value != null) {
			String clientid = getSessionId(req, res);
			byte[] bytes = serialize(value);
			String str = StringUtility.bytesToHex(bytes);
			LOG.info("Speichern des Strings: \n" + str);
			client.set(clientid + '_' + key, cacheTime, str);
		}
	}

	@Override
	public Object getAttribute(HttpServletRequest req, HttpServletResponse res, String key) {
		String clientid = getSessionId(req, res);
		String str = (String) client.get(clientid + '_' + key);
		if (str != null) {
			byte[] bytes = StringUtility.hexToBytes(str);
			Object obj = deserialize(bytes);
			return obj;
		} else {
			return null;
		}
	}

}
