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
package org.eclipsescout.demo.minicrm.client.ui.desktop.outlines.pages;

import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.extension.client.ui.basic.table.AbstractExtensibleTable;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.node.INodeSynchronizationProcessService;
import org.eclipse.scout.rt.shared.services.common.node.NodeStatusTableHolder;
import org.eclipse.scout.rt.shared.ui.UserAgentUtility;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.shared.Icons;

public class ClusterSynchronizationStatusTablePage extends AbstractPageWithTable<ClusterSynchronizationStatusTablePage.Table> {

//  /**
//   * @param pageParam
//   */
//  public ClusterSynchronizationStatusTablePage(AbstractPageParam pageParam) {
//    this(pageParam, true);
//  }
//
//  /**
//   * @param pageParam
//   */
//  protected ClusterSynchronizationStatusTablePage(AbstractPageParam pageParam, boolean callInitializer) {
//    super(pageParam, callInitializer);
//  }

  private static final String NODE_COLUMN_ID_PREFIX = "NODE_";

  private NodeStatusTableHolder m_data;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Cluster");
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.TreeNode;
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

  /**
   * return null-safe the number of columns (number of nodes) inside the TableHolder.
   */
  private int columnCount(NodeStatusTableHolder tableData) {
    if (tableData == null || tableData.getTableHeaders() == null) {
      return 0;
    }
    return tableData.getTableHeaders().length;
  }

  @Override
  protected Object[][] execLoadTableData(SearchFilter filter) throws ProcessingException {
    m_data = SERVICES.getService(INodeSynchronizationProcessService.class).getClusterstatusTableData();
    getTable().resetColumnConfiguration();
    return m_data.getTableData();
  }

  @Order(10.0)
  public class Table extends AbstractExtensibleTable {

    public ResourceColumn getResourceColumn() {
      return getColumnSet().getColumnByClass(ResourceColumn.class);
    }

    public DescriptionColumn getDescriptionColumn() {
      return getColumnSet().getColumnByClass(DescriptionColumn.class);
    }

    public ReloadClusterCacheMenuTextColumn getReloadClusterCacheMenuTextColumn() {
      return getColumnSet().getColumnByClass(ReloadClusterCacheMenuTextColumn.class);
    }

    @Override
    protected boolean getConfiguredMultilineText() {
      return true;
    }

    @Override
    protected int getConfiguredRowHeightHint() {
      if (UserAgentUtility.isTouchDevice()) {
        return super.getConfiguredRowHeightHint();
      }

      // for ten lines 150 is a good value
      return 150;
    }

    @Order(-100.0)
    public class ResourceColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Resource");
      }

      @Override
      protected String getConfiguredFont() {
        return "BOLD";
      }

      @Override
      protected int getConfiguredWidth() {
        return 235;
      }

      @Override
      protected boolean getConfiguredSortAscending() {
        return true;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 0;
      }
    }

    @Order(-90.0)
    public class DescriptionColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Description");
      }

      @Override
      protected boolean getConfiguredTextWrap() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 177;
      }
    }

    @Order(-80.0)
    public class ReloadClusterCacheMenuTextColumn extends AbstractStringColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Override
    protected void injectColumnsInternal(List<IColumn<?>> columnList) {
      String thisClusterNodeId = SERVICES.getService(INodeSynchronizationProcessService.class).getClusterNodeId();

      // Dynamically add node columns
      for (int i = 0; i < columnCount(m_data); i++) {
        AbstractStringColumn column = getNodeColumn(m_data.getTableHeaders()[i]);
        if (CompareUtility.equals(thisClusterNodeId, m_data.getTableHeaders()[i])) {
          column.setBackgroundColor("F1F1F1");
        }
        columnList.add(column);
      }
      super.injectColumnsInternal(columnList);
    }

    private AbstractStringColumn getNodeColumn(final String headerText) {
      return new AbstractStringColumn() {
        /**
         * The anonymous columns have to be identified by some
         * (unique) string other than their class name
         * (for as anonymous method-local classes, they have none).
         */
        @Override
        public String getColumnId() {
          return NODE_COLUMN_ID_PREFIX + headerText;
        }

        @Override
        protected int getConfiguredWidth() {
          return 300;
        }

        @Override
        protected String getConfiguredHeaderText() {
          return headerText;
        }
      };
    }

    @Order(10)
    public class ReloadClusterCacheMenu extends AbstractMenu {
      @Override
      protected void execPrepareAction() throws ProcessingException {
        String text = getReloadClusterCacheMenuTextColumn().getValue(getSelectedRow());
        if (!StringUtility.isNullOrEmpty(text)) {
          setText(text);
          setVisible(true);
        }
        else {
          setText("");
          setVisible(false);
        }
      }

      @Override
      protected void execAction() throws ProcessingException {
        String resourceName = getResourceColumn().getValue(getSelectedRow());
        SERVICES.getService(INodeSynchronizationProcessService.class).reloadClusterCache(resourceName);
        reloadPage();
      }
    }
  }
}
