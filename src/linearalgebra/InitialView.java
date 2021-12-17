package linearalgebra;

import linearalgebra.model.AccountType;
import linearalgebra.model.User;

public class InitialView extends AbstractView {
    
    @Override
    public void toView() {
        // Displays system home menu
        
        // displays the screen while not choosing the option Leave
        while(true) {
            System.out.println("\n------------ Linear Algebra ------------");
            
            System.out.println("\nWelcome to Linear Algebra!!\nWhat would you like to do?");
            System.out.println("1. Login");
            System.out.println("2. Create user");
            System.out.println("3. Leave");         
            int choice = inputInt("Choice : ");

            switch(choice) {
                case 1:
                    // call the método ...
                    login(); 
                    break;
                case 2:
                    createUser();                   
                    break;
                case 3:
                    System.out.println("\nGoodbye!!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nInvalid Choice!!");
            }
        }
    }

    private void createUser() {
        System.out.println("\n------------ Create user ------------");
        // Displays the user creation screen with regular user permission
        // As we are not passing the user -> we are creating the user
        // we are "opening" the screen with permission to regular user -> Does not allow selecting user account type
        ProfileView profileView = new ProfileView(AccountType.REGULAR_USER);
        // prints the information to the console
        profileView.toView();
    }
    
    private void login() {    
        System.out.println("\n------------ Login ------------");
        
        // calls the inputStream method of the superclass
        String username = inputString("Username: ");            
        String password = inputString("Password: ");
           
        // uses the super class userService to get the user through his username
        // trim() -> removes white space at the beginning and end of the string
        User loggedUser = userService.findUserByUsername(username.trim());     
        // If the user exists and the password is valid (database password the same as the one entered on the screen)
        if(loggedUser != null && loggedUser.getPassword().equals(password)) {   
            // check what type of user account
            if(AccountType.ADMIN == loggedUser.getAccountType()) {
               // Displays the administrator screen
               // Passing the user logged into the system as a parameter
               AdministratorView administratorView = new AdministratorView(loggedUser);
               // prints the information to the console 
               administratorView.toView();
            } else { 
               // Displays the regular user screen              
               // Passing the user logged into the system as a parameter
               RegularUserView regularUserView = new RegularUserView(loggedUser);
               // prints the information to the console 
               regularUserView.toView();
            }
        } else { // User does not exist or wrong password
            System.out.println("\nUsername or password is invalid!");                
        }        
    }
    
}
