package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version6;

import java.sql.SQLException;

public class UserDaoTest {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    DaoFactory factory = new DaoFactory();
    UserDao_6 dao1 = factory.userDao6();
    UserDao_6 dao2 = factory.userDao6();

    System.out.println("dao1 = " + dao1);
    System.out.println("dao2 = " + dao2);
  }
}
