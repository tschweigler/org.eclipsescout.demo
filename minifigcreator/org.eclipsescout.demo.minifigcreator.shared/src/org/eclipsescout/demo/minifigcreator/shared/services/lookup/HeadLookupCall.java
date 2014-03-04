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
package org.eclipsescout.demo.minifigcreator.shared.services.lookup;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipsescout.demo.minifigcreator.shared.minifig.part.Part;

public class HeadLookupCall extends LookupCall<Part> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<Part>> getConfiguredService() {
    return IHeadLookupService.class;
  }
}
