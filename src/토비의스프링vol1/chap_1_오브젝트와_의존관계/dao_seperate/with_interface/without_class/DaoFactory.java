package 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.without_class;

import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.ConnectionMaker;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.CustomConnectionMaker;

public class DaoFactory {
  /**
   * 같은 코드가 계속 중복됨
   */
//  public UserDao_6 userDao() {
//    ConnectionMaker connectionMaker = new CustomConnectionMaker();
//    UserDao_6 userDao = new UserDao_6(connectionMaker);
//    return userDao;
//  }
//
//  public AccountDao accountDao() {
//    return new AccountDao(new CustomConnectionMaker());
//  }
//
//  public MessageDao messageDao() {
//    return new MessageDao(new CustomConnectionMaker());
//  }

  public UserDao_6 userDao6 () {
    return new UserDao_6(connectionMaker());
  }

  public AccountDao accountDao() {
    return new AccountDao(connectionMaker());
  }

  public MessageDao messageDao() {
    return new MessageDao(connectionMaker());
  }

  public ConnectionMaker connectionMaker() {
    return new CustomConnectionMaker(); // 분리해서 중복을 제거한 ConnectionMaker 타입 오브젝트 생성코드
  }
}
