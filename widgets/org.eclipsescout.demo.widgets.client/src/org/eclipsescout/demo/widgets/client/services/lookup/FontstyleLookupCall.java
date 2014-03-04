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
package org.eclipsescout.demo.widgets.client.services.lookup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class FontstyleLookupCall extends LocalLookupCall<Integer> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<ILookupRow<Integer>> execCreateLookupRows() throws ProcessingException {
    ArrayList<ILookupRow<Integer>> rows = new ArrayList<ILookupRow<Integer>>();
    rows.add(new LookupRow<Integer>(0, TEXTS.get("Default")));
    rows.add(new LookupRow<Integer>(1, TEXTS.get("Bold")));
    rows.add(new LookupRow<Integer>(2, TEXTS.get("Italic")));
    rows.add(new LookupRow<Integer>(3, TEXTS.get("Bold") + " " + TEXTS.get("Italic")));
    return rows;
  }
}
