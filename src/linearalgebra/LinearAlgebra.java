package linearalgebra;

import java.time.LocalDateTime;
import linearalgebra.model.AccountType;
import linearalgebra.model.User;
import linearalgebra.service.UserService;

// initial class system 
public class LinearAlgebra {  
        
    // The system is started by executing the main method of the class...
    public static void main(String[] args) {
        // calls the method that checks if the default CCT user is created in the database and if it does not exist then create.
        insertUserAdminDefault();
        
        // Instance the class that presents the splash screen
        InitialView initialView = new InitialView();
        // show information on the user's screen
        initialView.toView();
    }
    
    private static void insertUserAdminDefault() {      
        // Instance the user service
        UserService userService = new UserService();
        
        // Searches if the CCT user exists in the database
        User user = userService.findUserByUsername("CCT");
        // Otherwise the user will be null
        if(user == null) {
            //Instantiate the user class and populate its attributes with the system default user values
            user = new User();
            user.setName("CCT");                        
            user.setUsername("CCT");
            user.setPassword("Dublin");
            user.setAccountType(AccountType.ADMIN);
            user.setCreated(LocalDateTime.now());
                      
            // save to database
            userService.salveUser(user);
        }        
    }
        
}
