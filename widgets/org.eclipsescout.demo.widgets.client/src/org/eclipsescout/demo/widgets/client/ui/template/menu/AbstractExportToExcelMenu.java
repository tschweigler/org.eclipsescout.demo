package org.eclipsescout.demo.widgets.client.ui.template.menu;

import java.io.File;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.docx4j.client.ScoutXlsxSpreadsheetAdapter;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.shell.IShellService;
import org.eclipse.scout.service.SERVICES;

public abstract class AbstractExportToExcelMenu extends AbstractMenu {

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("ExportToExcelMenu");
  }

  @Override
  protected void execAction() throws ProcessingException {
    IPage page = providePage();
    if (page != null) {
      ScoutXlsxSpreadsheetAdapter s = new ScoutXlsxSpreadsheetAdapter();
      File xlsx = s.exportPage(null, 0, 0, page);
      SERVICES.getService(IShellService.class).shellOpen(xlsx.getAbsolutePath());
    }
  }

  abstract protected IPage providePage();
}
