package 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_extends;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.User;

/**
 * 구현 코드는 제거되고 추상 메소드로 바뀌었다.
 * 메소드의 구현은 서브클래스가 담당한다.
 */
public abstract class UserDao_3 {

  public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c = getConnection();

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
    Connection c = getConnection();

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
    Connection c = getConnection();

    PreparedStatement ps = c.prepareStatement(
        "delete from users"
    );
    ps.executeUpdate();
  }


  /**
   * 구현 코드는 제거되고 추상 메소드로 바뀌었다.
   * 메소드의 구현은 서브클래스가 담당한다.
   */
  public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

  public class NUserDao extends UserDao_3 {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
      return null;
    }
  }

  public class DUserDao extends UserDao_3 {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
      return null;
    }
  }
}
