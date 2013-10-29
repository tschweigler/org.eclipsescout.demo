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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.scout.commons.EventListenerList;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.commons.serialization.IObjectSerializer;
import org.eclipse.scout.commons.serialization.SerializationUtility;
import org.eclipse.scout.rt.server.IServerSession;
import org.eclipse.scout.rt.server.ThreadContext;
import org.eclipse.scout.rt.server.services.common.clientnotification.ClientNotificationQueueEvent;
import org.eclipse.scout.rt.server.services.common.clientnotification.IClientNotificationFilter;
import org.eclipse.scout.rt.server.services.common.clientnotification.IClientNotificationQueueListener;
import org.eclipse.scout.rt.shared.services.common.clientnotification.IClientNotification;
import org.eclipse.scout.rt.shared.servicetunnel.ServiceTunnelObjectReplacer;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

/**
 *
 */
public class AmazonSQS {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(AmazonClientNotificationQueue.class);
  private AmazonSQSClient sqs;
  private String queueUrl;
  private int maxNumberOfMessages;
  private IObjectSerializer objectSerializer;
  private Object m_queueLock = new Object();
  private EventListenerList m_listenerList = new EventListenerList();

  public AmazonSQS() {
    try {
      sqs = new AmazonSQSClient(new PropertiesCredentials(AmazonSQS.class.getResourceAsStream("AwsCredentials.properties")));
      queueUrl = "http://sqs.us-east-1.amazonaws.com/490838598349/scoutcloud";
      maxNumberOfMessages = 10;
      objectSerializer = SerializationUtility.createObjectSerializer(new ServiceTunnelObjectReplacer());

    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private QueueElement getQueueElement(Message m) {
    try {
      return (QueueElement) objectSerializer.deserialize(StringUtility.hexToBytes(m.getBody()), Object.class);
    }
    catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  private List<Message> getMessages() {
    List<Message> messages = new ArrayList<Message>();
    ReceiveMessageRequest req = new ReceiveMessageRequest(queueUrl);
    req.setMaxNumberOfMessages(maxNumberOfMessages);

    //Bei einem Aufruf sind nicht immer alle Messages enthalten
    //Durch die Sperrfrist wird verwindert, dass eine Message mehrmals abgerufen wird
    for (int i = 0; i < 5; i++) {
      ReceiveMessageResult res = sqs.receiveMessage(req);
      List<Message> requestMessages = res.getMessages();
      messages.addAll(requestMessages);
    }
    LOG.info("Neue Nachrichten gefunden: " + messages.size());
    return messages;
  }

  private void removeMessages(List<Message> messages) {
    Collection<DeleteMessageBatchRequestEntry> entries = new ArrayList<DeleteMessageBatchRequestEntry>();
    for (Message m : messages) {
      DeleteMessageBatchRequestEntry entry = new DeleteMessageBatchRequestEntry();
      entry.setId(m.getMessageId());
      entry.setReceiptHandle(m.getReceiptHandle());
      entries.add(entry);
    }
    DeleteMessageBatchRequest deleteMessageBatchRequest = new DeleteMessageBatchRequest();
    deleteMessageBatchRequest.setEntries(entries);
    sqs.deleteMessageBatch(deleteMessageBatchRequest);
    LOG.info("Nachrichten gelöcht" + messages.size());
  }

  public void putNotification(IClientNotification notification, IClientNotificationFilter filter) {

    if (notification == null) {
      throw new IllegalArgumentException("notification must not be null");
    }
    if (filter == null) {
      throw new IllegalArgumentException("filter must not be null");
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("put " + notification + " for " + filter);
    }
    LOG.info("put " + notification + " for " + filter);

    QueueElement qe = new QueueElement(notification, filter);

    try {
      String messageBody = StringUtility.bytesToHex(objectSerializer.serialize(qe));
      SendMessageRequest smr = new SendMessageRequest(queueUrl, messageBody);
      sqs.sendMessage(smr);
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    fireEvent(notification, filter);
  }

  public IClientNotification[] getNextNotifications(long blockingTimeout) {
    long endTime = System.currentTimeMillis() + blockingTimeout;
    ArrayList<IClientNotification> list = new ArrayList<IClientNotification>();
    List<Message> removeableMessages = new ArrayList<Message>();
    LOG.info("Suche nach neuen Notifications");
    synchronized (m_queueLock) {
      //List<QueueElement> m_queue = getQueueElements();
      List<Message> m_queue = getMessages();

      while (true) {
        if (!m_queue.isEmpty()) {
          for (Iterator<Message> it = m_queue.iterator(); it.hasNext();) {

            Message m = it.next();
            QueueElement e = getQueueElement(m);

            if (e.getFilter().isActive()) {
              IServerSession serverSession = ThreadContext.getServerSession();
              if (!e.isConsumedBy(serverSession)) {
                if (e.getFilter().accept()) {
                  list.add(e.getClientNotification());
                  removeableMessages.add(m);
                  //TODO TSW: Das gibt Probleme mit der Queue (Nicht veränderbar)
//                  if (e.getFilter().isMulticast()) {
//                    e.setConsumedBy(serverSession);
//                  }
//                  else {
//                    it.remove();
//                  }
                }
              }
            }
            else {
              removeableMessages.add(m);
            }
          }
        }
        long dt = endTime - System.currentTimeMillis();
        if (list.size() > 0 || dt <= 0) {
          break;
        }
        else {
          try {
            m_queueLock.wait(dt);
          }
          catch (InterruptedException ie) {
          }
        }
      }
      removeMessages(removeableMessages);
    }
    LOG.info(list.size() + " neue Notifications gefunden");
    return list.toArray(new IClientNotification[list.size()]);
  }

  public void addClientNotificationQueueListener(IClientNotificationQueueListener listener) {
    m_listenerList.add(IClientNotificationQueueListener.class, listener);
  }

  public void removeClientNotificationQueueListener(IClientNotificationQueueListener listener) {
    m_listenerList.remove(IClientNotificationQueueListener.class, listener);
  }

  private void fireEvent(IClientNotification notification, IClientNotificationFilter filter) {
    IClientNotificationQueueListener[] listeners = m_listenerList.getListeners(IClientNotificationQueueListener.class);
    if (listeners != null && listeners.length > 0) {
      for (int i = 0; i < listeners.length; i++) {
        (listeners[i]).queueChanged(new ClientNotificationQueueEvent(notification, filter, ClientNotificationQueueEvent.TYPE_NOTIFICATION_ADDED));
      }
    }
  }

}
