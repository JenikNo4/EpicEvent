package cz.simek.epicevent;

import cz.simek.epicevent.config.ApplicationConfig;
import cz.simek.epicevent.model.User;
import cz.simek.epicevent.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by jenik on 1.2.17.
 */
public class RunIt {


    public static void main(String[] args) {


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);


        UserService service = (UserService) context.getBean("userService");
        User user = service.printFirstUser();


        System.out.println(user.getLogin() + " " + user.getEmail());


    }

}
