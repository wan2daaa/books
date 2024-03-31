package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version8;

import java.sql.Connection;
import java.sql.SQLException;
import 토비의스프링vol1.chap_1_오브젝트와_의존관계.version5.ConnectionMaker;

public class CountingConnectionMaker implements ConnectionMaker {

  private int counter;
  private ConnectionMaker realConnectionMaker;

  public CountingConnectionMaker(ConnectionMaker connectionMaker) {
    this.realConnectionMaker = connectionMaker;
  }

  @Override
  public Connection makeConnection() throws ClassNotFoundException, SQLException {
    this.counter ++;
    return realConnectionMaker.makeConnection();
  }

  public int getCounter() {
    return counter;
  }

}
