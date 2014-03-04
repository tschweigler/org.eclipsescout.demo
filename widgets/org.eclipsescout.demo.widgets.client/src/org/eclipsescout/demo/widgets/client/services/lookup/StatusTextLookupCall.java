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
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class StatusTextLookupCall extends LocalLookupCall<Long> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<ILookupRow<Long>> execCreateLookupRows() throws ProcessingException {
    ArrayList<ILookupRow<Long>> rows = new ArrayList<ILookupRow<Long>>();
    rows.add(new LookupRow<Long>(1L, TEXTS.get("Fatal"), "status_error", null, "000000", "ffffff", new FontSpec("Courir", FontSpec.STYLE_BOLD + FontSpec.STYLE_ITALIC, 12)));
    rows.add(new LookupRow<Long>(2L, TEXTS.get("Error0"), "status_error", null, "ff0000", "000000", new FontSpec("Courir", FontSpec.STYLE_BOLD, 12)));
    rows.add(new LookupRow<Long>(3L, TEXTS.get("Warning"), "status_warning", null, "ffff00", "000000", new FontSpec("Courir", FontSpec.STYLE_ITALIC, 12)));
    return rows;
  }
}
