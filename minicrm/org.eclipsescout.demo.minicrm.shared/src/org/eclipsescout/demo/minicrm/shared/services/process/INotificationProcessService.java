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
package org.eclipsescout.demo.minicrm.shared.services.process;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.validate.IValidationStrategy;
import org.eclipse.scout.rt.shared.validate.InputValidation;
import org.eclipse.scout.service.IService;

@InputValidation(IValidationStrategy.NO_CHECK.class)
public interface INotificationProcessService extends IService {

  public void sendRefreshBuddies() throws ProcessingException;

  public void sendRefreshBuddiesInternal() throws ProcessingException;

  public void sendMessage(String buddyName, String message) throws ProcessingException;
}
