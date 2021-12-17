
package linearalgebra;

import java.util.InputMismatchException;
import java.util.Scanner;
import linearalgebra.model.User;
import linearalgebra.service.OperationService;
import linearalgebra.service.UserService;

/* 
- Abstract classe view 
- An abstract class cannot be instantiated
- All classes that extend this class will have access to the protected and public attributes.
- Only the Abstract View class has access to the private attribute (scan)

Visibility:
- public: everyone accesses this information
- protected: only the mother class and the child class access this attribute
- private: only the class itself accesses this attribute 
*/
public abstract class AbstractView {
    
    // every class that extends Abstract View has access to the loggedUser, userService, and operationService attributes. 
    
    // Save session information (logged in user)
    protected final User loggedUser;    
    // Provides access to the user service 
    
    protected UserService userService = new UserService();      
    
    //Provides access to operations service 
    protected OperationService operationService = new OperationService();

    // Only this class has access to the (private) attribute 
    // will read from keyboard until "line break" -> \n 
    //Reference:
    // https://docs.oracle.com/javase/tutorial/java/data/characters.html
    private Scanner scan = new Scanner(System.in).useDelimiter("\n");
    
    // Class constructor when there is no user logged in. Example system Home screen. 
    public AbstractView() {
        this.loggedUser = null;
    }
            
    // Class constructor when a user is logged in
    public AbstractView(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    
    // Implementation of the method that customizes user interaction with string data
    // Pass the label field name as parameter
    protected String inputString(String label) {  
        // show the name 
        System.out.print(label);
        // capture string type information 
        // in this case it does not generate an exception with invalid information... string accepts all
        return scan.next();        
    }
    
    // Implementation of the method that customizes user interaction with integer type data
    // Pass the label field name as parameter
    protected int inputInt(String label) {
        boolean isSuccess;
        int value = 0;
            
        // loop repeat..
        // execute the code inside the loop and test the condition      
        do {
            try {
                // show the field name
                System.out.print(label);
                // captures integer type information 
                value = scan.nextInt(); 
                // if it got this far, there was no exception.. so it's true
                isSuccess = true;
            } catch (InputMismatchException ex) {
                // if there was an exception then you must "clear" the content of the scan with the command below
                scan.nextLine();
                // show the message 
                System.out.println("Invalid number!");
                // false to rerun the loop
                isSuccess = false;            
            }
            
            // keeps running as long as it doesn't have a valid value -> integer
        } while(!isSuccess);
        
        // returns the value captured from the keyboard
        return value;
    }
        
    // Implementation of the method that customizes user interaction with data of type double (floating point)
    // Pass the label Field name as parameter
    protected double inputDouble(String label) {
        boolean isSuccess;
        double value = 0;
                
        // loop repeat..
        // execute the code inside the loop and test the condition
        do {
            try {
                // show the field name
                System.out.print(label);
                // capture the information of type double
                value = scan.nextDouble();  
                // if it got this far, there was no exception.. so it's true
                isSuccess = true;
            } catch (InputMismatchException ex) {
                // if there was an exception then you must "clear" the content of the scan with the command below
                scan.nextLine();
                // show the message 
                System.out.println("Invalid number!");
                // false to rerun the loop
                isSuccess = false;            
            }
            
            // keeps running as long as it doesn't have a valid value -> double 
        } while(!isSuccess);
        
        // returns the value captured from the keyboard 
        return value;
    }
    
    // abstract method: all classes that extend to this one must have their specific implementation of this method
    // method that will be used to display and capture screen information
    public abstract void toView();
    
}
