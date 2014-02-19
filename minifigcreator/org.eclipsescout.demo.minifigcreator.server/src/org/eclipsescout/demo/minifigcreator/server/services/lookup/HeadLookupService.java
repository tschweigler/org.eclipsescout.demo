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
package org.eclipsescout.demo.minifigcreator.server.services.lookup;

import java.util.List;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minifigcreator.server.data.IMinifigDataStoreService;
import org.eclipsescout.demo.minifigcreator.shared.minifig.part.Part;
import org.eclipsescout.demo.minifigcreator.shared.services.lookup.IHeadLookupService;

public class HeadLookupService extends AbstractPartLookupService implements IHeadLookupService {

  @Override
  protected List<Part> createPartsList(ILookupCall<Part> call) {
    return SERVICES.getService(IMinifigDataStoreService.class).getAvailableHeads();
  }
}
