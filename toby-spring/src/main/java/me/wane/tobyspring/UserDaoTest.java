package me.wane.tobyspring;

import java.sql.SQLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao1 = context.getBean("userDao", UserDao.class);
    UserDao dao2 = context.getBean("userDao", UserDao.class);

    System.out.println("dao1 = " + dao1);
    System.out.println("dao2 = " + dao2);
    System.out.println(dao1 == dao2);


//    UserDao dao = context.getBean("userDao", UserDao.class);
//
//    dao.deleteAll();
//
//    User user = new User();
//    user.setId("wan2daaa");
//    user.setName("박재완");
//    user.setPassword("1234");
//
//    dao.add(user);
//
//    System.out.println(user.getId() + "등록 성공");
//
//    User user2 = dao.get(user.getId());
//    System.out.println(user2.getName());
//    System.out.println("user2.getPassword() = " + user2.getPassword());
//
//    System.out.println("user2.getId() = " + user2.getId() + " 조회 성공");
  }
}
