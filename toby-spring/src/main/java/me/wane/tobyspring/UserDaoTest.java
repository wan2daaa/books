package me.wane.tobyspring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@DirtiesContext
@ContextConfiguration(classes = DaoFactory.class, loader = SpringBootContextLoader.class)
public class UserDaoTest {


/*  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

//    UserDao dao1 = context.getBean("userDao", UserDao.class);
//    UserDao dao2 = context.getBean("userDao", UserDao.class);
//
//    System.out.println("dao1 = " + dao1);
//    System.out.println("dao2 = " + dao2);
//    System.out.println(dao1 == dao2);

//    UserDao dao = context.getBean("userDao", UserDao.class);
//
//    dao.deleteAll();
//
//    User user1 = new User();
//    user1.setId("wan2daaa");
//    user1.setName("박재완");
//    user1.setPassword("1234");
//
//    dao.add(user1);
//
//    System.out.println(user1.getId() + "등록 성공");
//
//    User user2 = dao.get(user1.getId());
//    System.out.println(user2.getName());
//    System.out.println("user2.getPassword() = " + user2.getPassword());
//
//    System.out.println("user2.getId() = " + user2.getId() + " 조회 성공");

*//*    UserDao dao = context.getBean("userDao", UserDao.class);

    dao.deleteAll();

    User user1 = new User();
    user1.setId("wan2daaa");
    user1.setName("박재완");
    user1.setPassword("1234");

    dao.add(user1);

    System.out.println(user1.getId() + "등록 성공");

    User user2 = dao.get(user1.getId());

    if (!user1.getName().equals(user2.getName())) {
      System.out.println("테스트 실패 (name)");
    } else if (!user1.getPassword().equals(user2.getPassword())) {
      System.out.println("테스트 실패 (password)");
    } else {
      System.out.println("조회 테스트 성공");
    }*//*

  }
*/

  private ApplicationContext context;

  private UserDao dao;
  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {
    this.context = new AnnotationConfigApplicationContext(DaoFactory.class);

    System.out.println("context = " + context);
    System.out.println("this = " + this);

    this.dao = context.getBean("userDao", UserDao.class);
    this.user1 = new User("wane", "와닝", "pass1");
    this.user2 = new User("gyungju", "갱주", "pass2");
    this.user3 = new User("anno", "익명", "pass3");
  }

  @Test
  public void addAndGet() throws SQLException, ClassNotFoundException {
//    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//
//    UserDao dao = context.getBean("userDao", UserDao.class);

    dao.deleteAll();
    assertThat(dao.getCount()).isZero();

//    User user1 = new User();
//    user1.setId("wan2daaa");
//    user1.setName("박재완");
//    user1.setPassword("1234");
//
//    User user2 = new User("gyungju", "갱주", "pass2");

    dao.add(user1);
    dao.add(user2);
    assertThat(dao.getCount()).isEqualTo(2);

    User userGet1 = dao.get(user1.getId());
    assertThat(user1.getName()).isEqualTo(userGet1.getName());
    assertThat(user1.getPassword()).isEqualTo(userGet1.getPassword());

    User userGet2 = dao.get(user2.getId());
    assertThat(user2.getName()).isEqualTo(userGet2.getName());
    assertThat(user2.getPassword()).isEqualTo(userGet2.getPassword());
  }

  @Test
  void count() throws Exception {
//    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//
//    UserDao dao = context.getBean("userDao", UserDao.class);
//    User user1 = new User("wane", "와닝", "pass1");
//    User user2 = new User("gyungju", "갱주", "pass2");
//    User user3 = new User("anno", "익명", "pass3");

    dao.deleteAll();
    assertThat(dao.getCount()).isEqualTo(0);

    dao.add(user1);
    assertThat(dao.getCount()).isEqualTo(1);

    dao.add(user2);
    assertThat(dao.getCount()).isEqualTo(2);

    dao.add(user3);
    assertThat(dao.getCount()).isEqualTo(3);
  }

  @Test
  void getUserFailure() throws Exception {
//    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//
//    UserDao dao = context.getBean("userDao", UserDao.class);

    dao.deleteAll();
    assertThat(dao.getCount()).isZero();

    assertThatThrownBy(() -> {
      dao.get("unknown_id");
    }).isInstanceOf(NoSuchElementException.class);
  }
}
