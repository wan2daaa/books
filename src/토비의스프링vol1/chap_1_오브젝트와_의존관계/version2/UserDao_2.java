package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.User;

/**
 * DB 연결 기능이 필요하면 getConnection() 메소드를 이용하게 된다.
 * 중복된 코드를 독립적인 메소드로 만들어서 중복을 제거
 */
public class UserDao_2 {

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


  private Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = DriverManager.getConnection(
        "jdbc:mysql://localhost/test", "root", "qkrwodhks1!"
    );
    return c;
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    UserDao_2 dao = new UserDao_2();

    dao.deleteAll();

    User user = new User();
    user.setId("wan2daaa");
    user.setName("박재완");
    user.setPassword("1234");

    dao.add(user);

    System.out.println(user.getId() + "등록 성공");

    User user2 = dao.get(user.getId());
    System.out.println(user2.getName());
    System.out.println("user2.getPassword() = " + user2.getPassword());

    System.out.println("user2.getId() = " + user2.getId() + " 조회 성공");
  }

}
