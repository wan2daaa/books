package me.wane.tobyspring;

import java.sql.Connection;
import java.sql.SQLException;

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
