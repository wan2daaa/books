package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

  public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = DriverManager.getConnection(
        "jdbc:mysql://localhost/test", "root", "qkrwodhks1!"
    );
    return c;
  }
}
