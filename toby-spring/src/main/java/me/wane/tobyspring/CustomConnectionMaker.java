package me.wane.tobyspring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class CustomConnectionMaker implements ConnectionMaker{
  @Override
  public Connection makeConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = DriverManager.getConnection(
        "jdbc:mysql://localhost/test", "root", "qkrwodhks1!"
    );
    return c;
  }
}
