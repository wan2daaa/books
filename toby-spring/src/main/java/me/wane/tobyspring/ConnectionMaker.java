package me.wane.tobyspring;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

public interface ConnectionMaker {
  public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
