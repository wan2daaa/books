package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version7_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.User;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.version5.ConnectionMaker;

public class UserDao_7_1 {
  private ConnectionMaker connectionMaker;
  private Connection c;
  private User user;

  private UserDao_7_1(ConnectionMaker connectionMaker) {
    this.connectionMaker = connectionMaker;
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
    this.c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?"
    );
    ps.setString(1, id);

    ResultSet rs = ps.executeQuery();
    rs.next();
    this.user = new User();
    this.user.setId(rs.getString("id"));
    this.user.setName(rs.getString("name"));
    this.user.setPassword(rs.getString("password"));

    rs.close();
    ps.close();
    c.close();

    return this.user;
  }

  public void deleteAll() throws ClassNotFoundException, SQLException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "delete from users"
    );
    ps.executeUpdate();
  }


}
