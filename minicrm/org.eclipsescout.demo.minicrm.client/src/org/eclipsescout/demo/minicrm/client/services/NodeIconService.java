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
package org.eclipsescout.demo.minicrm.client.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.service.IService;
import org.eclipsescout.demo.minicrm.shared.Icons;
import org.osgi.framework.ServiceRegistration;

/**
 *
 */
public class NodeIconService implements IService {

  List<String> m_icons;
  Map<String, String> m_nodeIcons;

  @Override
  public void initializeService(ServiceRegistration registration) {
    m_icons = new ArrayList<String>();
    m_icons.add(Icons.DotBlue);
    m_icons.add(Icons.DotOrgange);
    m_icons.add(Icons.DotGreen);

    m_nodeIcons = new HashMap<String, String>();
  }

  public String getIcon(String nodeId) {

    if (!m_nodeIcons.containsKey(nodeId)) {
      String icon = m_icons.get(0);
      if (icon != null) {
        m_nodeIcons.put(nodeId, icon);
        m_icons.remove(0);
        return icon;
      }
      else {
        m_nodeIcons.put(nodeId, Icons.Building);
      }
    }

    return m_nodeIcons.get(nodeId);
  }

}
