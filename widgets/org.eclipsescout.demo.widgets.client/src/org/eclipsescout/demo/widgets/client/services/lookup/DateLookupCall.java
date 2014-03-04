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
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class DateLookupCall extends LocalLookupCall<Long> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<ILookupRow<Long>> execCreateLookupRows() throws ProcessingException {
    ArrayList<ILookupRow<Long>> rows = new ArrayList<ILookupRow<Long>>();
    for (long l = 0L; l <= 5L; l++) {
      ILookupRow<Long> year = new LookupRow<Long>(l * 5, "201" + l);
      year.setEnabled(true);
      rows.add(year);
      rows.add(new LookupRow<Long>(l * 5 + 1, "Jan", null, null, "FFFFFF", "000000", new FontSpec("Courir", FontSpec.STYLE_PLAIN, 12), true, l * 5));
      rows.add(new LookupRow<Long>(l * 5 + 2, "Mar", null, null, "FFFFFF", "000000", new FontSpec("Courir", FontSpec.STYLE_PLAIN, 12), true, l * 5));
      rows.add(new LookupRow<Long>(l * 5 + 3, "Sep", null, null, "FFFFFF", "000000", new FontSpec("Courir", FontSpec.STYLE_PLAIN, 12), true, l * 5));
      rows.add(new LookupRow<Long>(l * 5 + 4, "Nov", null, null, "FFFFFF", "000000", new FontSpec("Courir", FontSpec.STYLE_PLAIN, 12), true, l * 5));
    }
    return rows;
  }
}
