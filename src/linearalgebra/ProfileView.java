package linearalgebra;

import java.time.LocalDateTime;
import linearalgebra.model.AccountType;
import linearalgebra.model.Sex;
import linearalgebra.model.User;

// class must be instantiated to create or edit user/profile
public class ProfileView extends AbstractView {
    
    // to set with what type of permission the user who "called the screen" has...
    private AccountType accountType;
    
    private User editUser ;
    
    // Used to CREATE new user
    // passes the type of permission of who is calling the screen...
    public ProfileView(AccountType accountType) {
        // to set a attribute to the account 
        this.accountType = accountType;
    }
    
    // Used when EDIT profile
    // passes the type of permission of who is calling the screen...
    // passes the user that will be edited... can be logged in user or selected user (admin case)
    public ProfileView(AccountType accountType, User editUser) {
        this.editUser = editUser;
        this.accountType = accountType;
    }
    
    // Nome: Angelina Jolie

    @Override
    public void toView() {
        // Displays the user create/edit screen
        
        // if editUser != null then the user is being edited and the user's previous information will be displayed on the screen.
        if(editUser != null) {
            System.out.println("\nCurrent Name: " + editUser.getName());
        }
        // calls the inputString method of the super class
        String name = inputString("Name: ");

        if(editUser != null) {
            System.out.println("\nCurrent Surname: " + editUser.getSurname());
        }
        // calls the inputString method of the super class
        String surname = inputString("Surname: ");    

        if(editUser != null) {
            System.out.println("\nCurrent Age: " + editUser.getAge());
        }
        Integer age;
        do {           
            // chama o metodo inputInt da super classe
            age = inputInt("Age: ");  
            
            //repeat the loop if age is not between 1 and 99 years old
        } while (age < 1 || age > 99);

        if(editUser != null) {
            System.out.println("\nCurrent Sex: " + editUser.getSex());
        }
        int choiceSex;
        do {
            System.out.println("Select an item sex:");
            // run through the array Sex -> Sex[]
            for(int i=0; i< Sex.values().length; i++) {
                // get the value of the array at position i
                Sex sex = Sex.values()[i];
                // shows options concatenating code and description
                System.out.println(sex.getCode() + ". " + sex.getDescription());
            }     
            
            // calls the inputInt method of the superclass
            choiceSex = inputInt("Choice : ");
            
            // repeats the loop if the selected option is not between 1 and 2 (size of the enum values)
        } while (choiceSex < 1 || choiceSex > Sex.values().length);

        System.out.println("------------");

        if(editUser != null) {
            System.out.println("\nCurrent Username: " + editUser.getUsername());
        }
        // calls the inputString method of the super class
        String username = inputString("Username: ");
        String password = inputString("Password: ");

        int choiceAccountType;
        // If it presents the screen with the admin profile then it allows to select the profile of the new user 
        if(AccountType.ADMIN == accountType) {  
            
            // if editUser != null then the user is being edited and the user's previous information will be displayed on the screen
            if(editUser != null) {
                System.out.println("\nCurrent Account Type: " + editUser.getAccountType().getDescription());
            }
            
            do {
                System.out.println("Select an item account Type:");  
                // traverse the AccountType array -> AccountType[]
                for(int i=0; i< AccountType.values().length; i++) {
                     // get the value of the array at position i
                    AccountType type = AccountType.values()[i];
                    // shows options concatenating code and description
                    System.out.println(type.getCode() + ". " + type.getDescription());
                }
                
                // calls the inputInt method of the superclass
                choiceAccountType = inputInt("Choice : ");
                
                // repeats the loop if the selected option is not between 1 and 2 (size of the enum values)
            } while (choiceAccountType < 1 || choiceAccountType > AccountType.values().length);
        } else {
            // repeats the loop if the selected option is not between 1 and 2 (size of the enum values)
            choiceAccountType = AccountType.REGULAR_USER.getCode();
        }
            
        // home - populates user information
        User user = new User();
        // editing the profile then you should set the current id
        if(editUser != null) {
            user.setId(editUser.getId());
        }
        user.setName(name);        
        user.setSurname(surname);
        user.setAge(age);
        user.setSex(choiceSex == Sex.MALE.getCode() ? Sex.MALE : Sex.FEMALE);
        user.setUsername(username);
        user.setPassword(password);
        user.setAccountType(choiceAccountType ==  AccountType.ADMIN.getCode() ? AccountType.ADMIN : AccountType.REGULAR_USER);
        user.setCreated(LocalDateTime.now());
        // end - populates user information
        
        // save the user
        boolean isSucess = userService.salveUser(user);        
        if(isSucess) {
            System.out.println("\nSuccess saving user!");            
        } else {
            System.out.println("\nFailed to save user!");
        }
    }
    
}
