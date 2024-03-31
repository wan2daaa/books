package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.User;

public class UserDao_4 {

  private SimpleConnectionMaker simpleConnectionMaker;

  public UserDao_4() {
    // 상태를 관리하는 것도 아니니 한 번만 만들어 인스턴스 변수에 저장해두고 메소드에서 사용하게 한다.
    simpleConnectionMaker = new SimpleConnectionMaker();
  }


  public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c = simpleConnectionMaker.makeNewConnection();

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
    Connection c = simpleConnectionMaker.makeNewConnection();

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
    Connection c = simpleConnectionMaker.makeNewConnection();

    PreparedStatement ps = c.prepareStatement(
        "delete from users"
    );
    ps.executeUpdate();
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    UserDao_4 dao = new UserDao_4();

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
