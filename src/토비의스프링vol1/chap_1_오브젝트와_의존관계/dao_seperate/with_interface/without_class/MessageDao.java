package 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.without_class;

import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.ConnectionMaker;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.dao_seperate.with_interface.CustomConnectionMaker;

public class MessageDao {

  public MessageDao(ConnectionMaker connectionMaker) {
  }
}
