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

import org.eclipse.scout.service.IService;

public interface IBuddyIconProviderService extends IService {
  /**
   * the default buddy icon used when the user has not uploaded an icon yet. icon must be located in client plugin
   * under resources/icons
   */
  final static String BUDDY_DEFAULT_ICON = "default_buddy_icon";
}
