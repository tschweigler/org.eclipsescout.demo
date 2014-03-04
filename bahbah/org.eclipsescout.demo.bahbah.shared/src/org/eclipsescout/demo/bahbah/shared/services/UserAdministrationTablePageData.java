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
package org.eclipsescout.demo.bahbah.shared.services;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated, no manual modifications recommended.
 * 
 * @generated
 */
public class UserAdministrationTablePageData extends AbstractTablePageData {

  private static final long serialVersionUID = 1L;

  public UserAdministrationTablePageData() {
  }

  @Override
  public UserAdministrationTableRowData addRow() {
    return (UserAdministrationTableRowData) super.addRow();
  }

  @Override
  public UserAdministrationTableRowData addRow(int rowState) {
    return (UserAdministrationTableRowData) super.addRow(rowState);
  }

  @Override
  public UserAdministrationTableRowData createRow() {
    return new UserAdministrationTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return UserAdministrationTableRowData.class;
  }

  @Override
  public UserAdministrationTableRowData[] getRows() {
    return (UserAdministrationTableRowData[]) super.getRows();
  }

  @Override
  public UserAdministrationTableRowData rowAt(int index) {
    return (UserAdministrationTableRowData) super.rowAt(index);
  }

  public void setRows(UserAdministrationTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class UserAdministrationTableRowData extends AbstractTableRowData {

    private static final long serialVersionUID = 1L;
    public static final String userId = "userId";
    public static final String username = "username";
    public static final String role = "role";
    private Long m_userId;
    private String m_username;
    private Integer m_role;

    public UserAdministrationTableRowData() {
    }

    public Long getUserId() {
      return m_userId;
    }

    public void setUserId(Long userId) {
      m_userId = userId;
    }

    public String getUsername() {
      return m_username;
    }

    public void setUsername(String username) {
      m_username = username;
    }

    public Integer getRole() {
      return m_role;
    }

    public void setRole(Integer role) {
      m_role = role;
    }
  }
}
