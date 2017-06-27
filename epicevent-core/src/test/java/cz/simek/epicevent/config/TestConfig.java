package cz.simek.epicevent.config;

import cz.simek.epicevent.dao.UserDao;
import cz.simek.epicevent.dao.UserDaoImpl;
import cz.simek.epicevent.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jenik on 31.1.17.
 */
@Configuration
public class TestConfig {

    @Bean
    public UserService getUserService() {
        return new UserService();
    }

    @Bean
    public UserDao getUserDao() {
        return new UserDaoImpl();
    }


}