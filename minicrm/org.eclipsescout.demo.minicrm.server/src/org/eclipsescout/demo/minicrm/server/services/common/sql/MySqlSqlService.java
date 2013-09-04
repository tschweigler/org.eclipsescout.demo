package org.eclipsescout.demo.minicrm.server.services.common.sql;

import org.eclipse.scout.service.IService2;

import com.bsiag.scout.rt.server.jdbc.AbstractMySqlSqlService;

public class MySqlSqlService extends AbstractMySqlSqlService implements IService2 {
  @Override
  protected String getConfiguredJdbcMappingName() {
    return "jdbc:mysql://ec2-23-21-211-172.compute-1.amazonaws.com:3306/minicrm";
  }

  @Override
  protected String getConfiguredPassword() {
    return "minicrm";
  }

  @Override
  protected String getConfiguredUsername() {
    return "minicrm";
  }
}
