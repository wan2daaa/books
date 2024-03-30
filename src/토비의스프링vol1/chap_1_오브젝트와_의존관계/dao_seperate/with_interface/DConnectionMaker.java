package 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface;

import java.sql.Connection;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker{

  @Override
  public Connection makeConnection() throws ClassNotFoundException, SQLException {
    //...
    return null;
  }
}
