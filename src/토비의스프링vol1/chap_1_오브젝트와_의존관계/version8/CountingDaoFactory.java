package 토비의스프링vol1.chap_1_오브젝트와_의존관계.version8;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CountingDaoFactory {
//
//
//  @Bean
//  public UserDao userDao() {
//    return new UserDao(connectionMaker());
//  }
//
//
//  @Bean
//  public ConnectionMaker connectionMaker() {
//    return new CountingConnectionMaker(realConnectionMaker());
//  }
//
//  @Bean
//  public ConnectionMaker realConnectionMaker() {
//    return new CustomConnectionMaker();
//  }
//
//}