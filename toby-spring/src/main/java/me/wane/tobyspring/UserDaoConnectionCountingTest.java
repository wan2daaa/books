package me.wane.tobyspring;

import java.sql.SQLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoConnectionCountingTest {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
    UserDao dao = context.getBean("userDao", UserDao.class);

    CountingConnectionMaker ccm = context.getBean("connectionMaker",
        CountingConnectionMaker.class);
    System.out.println("connectionCounter : "+  ccm.getCounter());

  }
}
