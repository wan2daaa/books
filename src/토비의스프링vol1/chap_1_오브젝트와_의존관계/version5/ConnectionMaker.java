package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version5;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
  public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
