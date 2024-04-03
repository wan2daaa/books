package me.wane.tobyspring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserDao {
  private final ConnectionMaker connectionMaker;

  @Autowired
  public UserDao(ConnectionMaker connectionMaker) {
    this.connectionMaker = connectionMaker;
  }

  public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "insert into users(id, name, password) values(?,?,?)"
    );
    ps.setString(1, user.getId());
    ps.setString(2, user.getName());
    ps.setString(3, user.getPassword());

    ps.executeUpdate();

    ps.close();
    c.close();
  }

  public User get(String id) throws ClassNotFoundException, SQLException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?"
    );
    ps.setString(1, id);

    ResultSet rs = ps.executeQuery();

    User user = null;

    if (rs.next()) {
      user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
    }

    rs.close();
    ps.close();
    c.close();

    if (user == null) {
      throw new NoSuchElementException();
    }

    return user;
  }

  public void deleteAll() throws ClassNotFoundException, SQLException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement(
        "delete from users"
    );
    ps.executeUpdate();

    ps.close();
    c.close();
  }

  public int getCount() throws SQLException, ClassNotFoundException {
    Connection c = connectionMaker.makeConnection();

    PreparedStatement ps = c.prepareStatement("select count(*) from users");

    ResultSet rs = ps.executeQuery();
    rs.next();
    int count = rs.getInt(1);

    rs.close();
    ps.close();
    c.close();

    return count;
  }


}
