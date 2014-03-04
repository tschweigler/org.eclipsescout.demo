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
package org.eclipsescout.demo.bahbah.server.services.process;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.commons.holders.ByteArrayHolder;
import org.eclipse.scout.commons.holders.NVPair;
import org.eclipse.scout.rt.server.services.common.jdbc.SQL;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.service.AbstractService;
import org.eclipsescout.demo.bahbah.server.ServerSession;
import org.eclipsescout.demo.bahbah.shared.security.UpdateIconPermission;
import org.eclipsescout.demo.bahbah.shared.services.process.IIconProcessService;

public class IconProcessService extends AbstractService implements IIconProcessService {
  public final static int MAX_SIZE = 16;

  private byte[] resize(byte[] content) {
    try {
      BufferedImage img = ImageIO.read(new ByteArrayInputStream(content));
      int w = img.getWidth();
      int h = img.getHeight();
      if (w > MAX_SIZE || h > MAX_SIZE) {
        float longer = w;
        if (h > w) longer = h;
        float fl = MAX_SIZE / longer;
        w = Math.round(fl * w);
        h = Math.round(fl * h);
        BufferedImage scaledImg = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D gScaledImg = scaledImg.createGraphics();
        gScaledImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        gScaledImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gScaledImg.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        gScaledImg.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        gScaledImg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gScaledImg.drawImage(img, 0, 0, w, h, null);
        gScaledImg.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(scaledImg, "png", out);
        return out.toByteArray();
      }
      else {
        return content;
      }
    }
    catch (Throwable e) {
      return null;
    }
  }

  @Override
  public byte[] loadIcon(String name) throws ProcessingException {
    ByteArrayHolder iconHolder = new ByteArrayHolder();
    SQL.selectInto("SELECT icon INTO :icon FROM TABUSERS WHERE username = :name", new NVPair("icon", iconHolder), new NVPair("name", name));
    return iconHolder.getValue();
  }

  @Override
  public void saveIcon(byte[] icon) throws ProcessingException {
    // permission validation
    if (!ACCESS.check(new UpdateIconPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    // input validation
    byte[] resizedIcon = resize(icon);
    if (resizedIcon == null) {
      throw new VetoException();
    }

    //store in database
    SQL.update("UPDATE TABUSERS SET icon = :icon WHERE username = :userId", new NVPair("name", ServerSession.get().getUserId()), new NVPair("icon", resizedIcon));
  }
}
