package cz.simek.projectName.config;

import cz.simek.projectName.dao.UserDao;
import cz.simek.projectName.dao.UserDaoImpl;
import cz.simek.projectName.service.UserService;
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