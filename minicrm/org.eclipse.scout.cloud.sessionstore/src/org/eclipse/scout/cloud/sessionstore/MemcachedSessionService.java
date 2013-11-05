package org.eclipse.scout.cloud.sessionstore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.commons.serialization.IObjectSerializer;
import org.eclipse.scout.commons.serialization.SerializationUtility;
import org.eclipse.scout.http.servletfilter.session.ISessionStoreService;
import org.eclipse.scout.service.AbstractService;

import net.spy.memcached.MemcachedClient;

public class MemcachedSessionService extends AbstractService implements ISessionStoreService {

	private static final IScoutLogger LOG = ScoutLogManager.getLogger(MemcachedSessionService.class);
	//private static MemcachedSessionStore clientcache;
	private MemcachedClient client;
	private String configEndpoint = "localhost";
	private Integer clusterPort = 11211;
	private Integer cacheTime = 3600;
	private String cookieName = "clientid";
	private String cookieStoreKey = "exsistingClientIds";
	private IObjectSerializer objectSerializer;

	public MemcachedSessionService() {
		objectSerializer = SerializationUtility.createObjectSerializer();

		try {
			client = new MemcachedClient(new InetSocketAddress(configEndpoint, clusterPort));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static MemcachedSessionStore getInstance() {
//		if (clientcache == null) {
//			clientcache = new MemcachedSessionStore();
//		}
//		return clientcache;
//	}

	public Object getAttribute(HttpServletRequest req, HttpServletResponse res, String key) {
	    LOG.info("[MemcachedSessionStore] getAttribute: " + key);

		try {

			String str = (String) client.get(getClientId(req, res) + "_" + key);
			if(str != null) {
			return objectSerializer.deserialize(StringUtility.hexToBytes(str), Object.class);
			} else {
				LOG.info("No value in cache for " + key);
			}
		} catch (ClassNotFoundException e) {
			LOG.info("Unable to deserialisize value for " + key);
		} catch (IOException e) {
			LOG.info("Unable to deserialisize value for " + key);
		}
		return null;
	}

	public void setAttribute(HttpServletRequest req, HttpServletResponse res, String key, Object value) {
	    LOG.info("[MemcachedSessionStore] setAttribute: " + key);

		try {
			client.set(getClientId(req, res) + "_" + key, cacheTime, StringUtility.bytesToHex(objectSerializer.serialize(value)));
		} catch (IOException e) {
			LOG.info("Unable to serialisize value for " + key);
		}
	}

	private String getClientId(HttpServletRequest req, HttpServletResponse res) {
		Cookie[] cookies = req.getCookies();

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];

			if (cookie.getName().equals(cookieName)) {
				LOG.info("# ClientdId gefunden: " + cookie.getValue());
				return cookie.getValue();
			}
		}
		if (req.getAttribute(cookieName) != null) {
			LOG.info("# Innerhalb dieses Calls wurde bereits ein Cookie gesetzt");
			return (String) req.getAttribute(cookieName);
		}

		return getNewClientId(req, res);
	}

	private String getNewClientId(HttpServletRequest req, HttpServletResponse res) {
		do {
			//TODO TSW Löschen alter CookieKeys
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
				// TODO Sollte höhere Cachetime haben -> unendlich
				client.set(cookieStoreKey, cacheTime, existingClientIds);
				LOG.info("Neue ClientdId angelegt: " + cookie.getValue());
				return newClientId;
			}

		} while (true);
	}


}
