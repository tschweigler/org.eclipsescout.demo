package org.eclipsescout.demo.minicrm.server.services.common.sql;

import org.eclipse.scout.service.IService;

import com.bsiag.scout.rt.server.jdbc.AbstractMySqlSqlService;

public class MySqlSqlService extends AbstractMySqlSqlService implements IService {
  @Override
  protected String getConfiguredJdbcMappingName() {
    //return "jdbc:mysql://ec2-23-21-211-172.compute-1.amazonaws.com:3306/minicrm";
    return "jdbc:mysql://localhost:3306/minicrm";
  }

  @Override
  protected String getConfiguredPassword() {
    //return "minicrm";
    return "";
  }

  @Override
  protected String getConfiguredUsername() {
    //return "minicrm";
    return "root";
  }
}
