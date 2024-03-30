package 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.without_class;

import java.sql.SQLException;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.User;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.ConnectionMaker;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.CustomConnectionMaker;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.DConnectionMaker;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.UserDao_5;

public class UserDaoTest {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {

//    //UserDao 가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
//    ConnectionMaker connectionMaker = new CustomConnectionMaker();
//    ConnectionMaker dconnectionMaker = new DConnectionMaker();
//
//    UserDao_6 dao = new UserDao_6(connectionMaker);
//    UserDao_6 dao2 = new UserDao_6(dconnectionMaker);

    UserDao_6 dao = new DaoFactory().userDao6();

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
