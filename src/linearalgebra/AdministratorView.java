package linearalgebra;

import java.time.format.DateTimeFormatter;
import java.util.List;
import linearalgebra.model.AccountType;
import linearalgebra.model.LinearEquation;
import linearalgebra.model.MathOperation;
import linearalgebra.model.ResultLinearEquation;
import linearalgebra.model.User;

// class must be instantiated if the user logged in with administrator profile
public class AdministratorView extends AbstractView {
    
    // attribute that is used by various places in the class to present the date and time information in format: yyyy-MM-dd HH:mm
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    // class constructor 
    // parameter is the logged in user, obtained from the system's home screen 
    public AdministratorView(User loggedUser) {
        // setup the user logged into the mother class(AbstractView) ->  public AbstractView(User loggedUser)
        super(loggedUser);
    }

    @Override // Annotation used to subscribe to the parent class method
    public void toView() {
        // Displays system administrator screen       
        boolean isLeave = false;
        // displays the screen while not logging off
        while(!isLeave) {
            System.out.println("\n------------ Admintrator - Linear Algebra ------------");
             
            System.out.println("\nWhat would you like to do?");            
            System.out.println("1. List of all users");
            System.out.println("2. Create user");
            System.out.println("3. Edit user");
            System.out.println("4. Remove user");
            System.out.println("5. Review the operations user");
            System.out.println("6. Edit profile");            
            System.out.println("7. Logoff");
            int choice = inputInt("Choice : ");

            switch(choice) {
                case 1:
                    System.out.println("\n------------ List of all users ------------");
                    // call the método ...
                    listAllUsers(); 
                    break;
                case 2:
                    createUser();
                    break;
                case 3: 
                    editUser();
                    break;
                case 4: 
                    removeUser();
                    break;
                case 5: 
                    reviewOperationsUser();
                    break;
                case 6: 
                    editProfile();
                    break;
                case 7: 
                    isLeave = true;
                    break;
                default:
                    System.out.println("Invalid Choice!!");
            }
        }
    }
    
    private void listAllUsers() {
        //uses super class userService to get all users
        List<User> users = userService.findAllUsers();
        
        // Displays user information on the screen 
        // \t -> tab
        System.out.println("Id\tDate created\t\tName\t\tSurname\t\tAccount Type");   
        for(int i=0; i<users.size(); i++) {
            // get the user in the list position
            User user = users.get(i);
            // connect user information
            // format the date/time -> formatter.format(user.getCreated())
            // user.getAccountType().getDescription() -> information
            System.out.println(user.getId() + "\t" + formatter.format(user.getCreated()) + "\t" + user.getName() + "\t\t" + (user.getSurname() != null ? user.getSurname() : " ") + "\t\t" + user.getAccountType().getDescription());   
        }
    }
    
    private void createUser() {
        System.out.println("\n------------ Create user ------------");
        // Displays user creation screen with administrator permission
        // we are not passing the user -> we are creating the user
        // we are opening" the screen with admin permission -> allows to select user account type 
        ProfileView profileView = new ProfileView(AccountType.ADMIN);
        // prints the information to the console
        profileView.toView();
    }
    
    private void editUser() {
        System.out.println("\n------------ Edit user ------------");
        // calls the method that lists all users on the screen
        listAllUsers();
        
        // Get the userid that will be edited
        // Calls the inputInt method of the superclass
        int id = inputInt("Select an item by Id: ");
        
        // use the super class's userService to get the user through its id
        User user = userService.getUser(id);
        // if the user exists it will be different from null
        if(user != null) { 
            // Displays the user edit screen
            // Passing as a parameter with the user obtained from the database that will be edited
            // Administrator permission allowing you to select user profile 
            ProfileView profileView = new ProfileView(AccountType.ADMIN, user);
            // prints the screen to the console
            profileView.toView();            
        } else {
            System.out.println("\nUser not found!");
        }        
    }
    
    private void removeUser() {
        System.out.println("\n------------ Remove user ------------");
        // calls the method that lists all users on the screen
        listAllUsers();
        
        // Gets the user ID that will be deleted
        // calls the inputInt method of the super-class
        long id = inputInt("Select an item by Id: ");
        
        // uses the super class's userService to remove the user through its id
        boolean isSucess = userService.deleteUser(id);
        // Show message to user
        if(isSucess) {
            System.out.println("\nSuccess remove user!");            
        } else {
            System.out.println("\nFailed to remove user!");
        }
    }

    private void editProfile() {
        System.out.println("\n------------ Edit profile ------------");
        
        // Shows the profile editing screen (logged in user)
        // Passing as a parameter with the logged in user sitting in the super class
        // Administrator permission allowing you to select user profile
        ProfileView profileView = new ProfileView(AccountType.ADMIN, loggedUser);
        // prints the screen on the console
        profileView.toView();
    }
    
    private void reviewOperationsUser() {
        System.out.println("\n------------ Review the operations user ------------");
        
        // calls the method that lists all users on the screen
        listAllUsers();
        
        // Gets the ID of the user you want to view the operations
        // calls the inputInt method of the super class
        // id -> idUser
        long id = inputInt("Select an item by Id: ");
        
        // uses the super class's userService to get the user through its idUser
        User user = userService.getUser(id);
        // if the user exists it will be different from null 
        if(user != null) {
            // uses the operationService of the super class to get user operations through its idUser
            List<MathOperation> operations = operationService.getMathOperations(id);
            // if there are operations
            if(!operations.isEmpty()) {
                System.out.println("\n------------ List of all operations by user ------------");

                // Displays the list title with tabs
                System.out.println("Id\tDate created\tDimension");   
                // run through the list of operations and print your information
                for(int i=0; i<operations.size(); i++) {
                    // get the list operation at position i
                    MathOperation operation = operations.get(i);
                    // \t -> tab
                    // formatter.format(operation.getCreated()) -> formats the date for visual presentation
                    System.out.println(operation.getId() + "\t" + formatter.format(operation.getCreated()) + "\t" + operation.getDimension());   
                }

                // Gets the ID of the operation you want to view the detailed information
                // calls the inputInt method of the super class
                // id -> idOperation
                id = inputInt("Select an item by Id: ");

                // uses the operationService of the super class to get the operation through its idOperation
                MathOperation operationSelected = operationService.getMathOperation(id);
                // if the operation exists, it will be different from null
                if(operationSelected != null) {
                    System.out.println("\n------------ Operation selected ------------");                
                    System.out.println("Id: " + operationSelected.getId());
                    System.out.println("Create Date: " + formatter.format(operationSelected.getCreated()));
                    System.out.println("Dimension: " + operationSelected.getDimension().getDescription());

                    System.out.println("\nLinear Equation:");
                    for(int i=0; i< operationSelected.getLinearEquations().size(); i++) {
                        LinearEquation linearEquation = operationSelected.getLinearEquations().get(i);
                        System.out.println(linearEquation.toString());
                    }

                    System.out.println("\nResults:");
                    for(int i=0; i< operationSelected.getResults().size(); i++) {
                        ResultLinearEquation result = operationSelected.getResults().get(i);
                        System.out.println(result.getVariable() + ": " + result.getValue());
                    }
                } else {
                    System.out.println("\nOperation not found!");
                }
            } else {
                System.out.println("\nOperation not found!");
            }
        } else {
            System.out.println("\nUser not found!");
        }
    }
    
}
