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
package org.eclipse.scout.tutorial.jaxws.shared.services.outline;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.service.IService;

public interface IStandardOutlineService extends IService {

  /**
   * @return
   * @throws ProcessingException
   */
  Object[][] getCompanyTableData() throws ProcessingException;

  /**
   * @return
   * @throws ProcessingException
   */
  Object[][] getWsLogTableData() throws ProcessingException;
}
