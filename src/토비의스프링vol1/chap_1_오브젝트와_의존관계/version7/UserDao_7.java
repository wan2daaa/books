package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version7;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.User;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.version5.ConnectionMaker;

public class UserDao_7 {
  private static UserDao_7 INSTANCE;
  private ConnectionMaker connectionMaker;

  private UserDao_7(ConnectionMaker connectionMaker) {
    this.connectionMaker = connectionMaker;
  }

  public static synchronized UserDao_7 getInstance() {
    if (INSTANCE == null) INSTANCE = new UserDao_7(getInstance().connectionMaker);
    return INSTANCE;
  }
  public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "insert into users(id, name, password) values(?,?,?)"
    );
    ps.setString(1, user.getId());
    ps.setString(2, user.getName());
    ps.setString(3, user.getPassword());

    ps.executeUpdate();

    ps.close();
    c.close();
  }

  public User get(String id) throws ClassNotFoundException, SQLException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?"
    );
    ps.setString(1, id);

    ResultSet rs = ps.executeQuery();
    rs.next();
    User user = new User();
    user.setId(rs.getString("id"));
    user.setName(rs.getString("name"));
    user.setPassword(rs.getString("password"));

    rs.close();
    ps.close();
    c.close();

    return user;
  }

  public void deleteAll() throws ClassNotFoundException, SQLException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "delete from users"
    );
    ps.executeUpdate();
  }


}
