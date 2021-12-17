package linearalgebra;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import linearalgebra.model.AccountType;
import linearalgebra.model.Dimension;
import linearalgebra.model.LinearEquation;
import linearalgebra.model.MathOperation;
import linearalgebra.model.ResultLinearEquation;
import linearalgebra.model.User;

// class must be instantiated if the logged in user has regular user profile
public class RegularUserView extends AbstractView {
    
    // class constructor
    // parameter is the logged in user, obtained from the system's home screen
    public RegularUserView(User loggedUser) {
        // to set the user logged into the mother class(AbstractView) ->  public AbstractView(User loggedUser)
        super(loggedUser);
    }

    @Override
    public void toView() {
        // Displays regular system user screen  
        boolean isLeave = false;
        // displays the screen while not logging off
        while(!isLeave) {
            System.out.println("\n------------ Regular User - Linear Algebra ------------");
            
            System.out.println("\nWhat would you like to do?");            
            System.out.println("1. Calculate linear equations");            
            System.out.println("2. Edit profile");           
            System.out.println("3. Logoff");
            int choice = inputInt("Choice : ");

            switch(choice) {
                case 1:
                    // call the method ...
                    calculateLinearEquations(); 
                    break;
                case 2:
                    editProfile();
                    break;
                case 3:                        
                    isLeave = true;
                    break;
                default:
                    System.out.println("Invalid Choice!!");
            }
        }
    }

    private void editProfile() {
        System.out.println("\n------------ Edit profile ------------");
        
        //Displays the edit screen of the user with regular user permission
        // How we are passing the user -> we are editing the user
        // We are "opening" the screen with permission to regular user -> Does not allow selecting user account type
        ProfileView profileView = new ProfileView(AccountType.REGULAR_USER, loggedUser);
        // prints the information to the console
        profileView.toView();
    }
    
    private void calculateLinearEquations() {
        System.out.println("\n------------ Regular User - Calculate linear equations ------------");
        
        int choiceDimension;
        do {
            // Selects the dimension of the system linear equation : 2x2 ou 3x3
            System.out.println("Select an item dimension:");
            // run the enum values
            // Dimension.values() is an array -> Dimension[]
            for(int i=0; i< Dimension.values().length; i++) {
                // get the value of the array at position i
                Dimension dimension = Dimension.values()[i];
                // show message with code and description defined in enum
                System.out.println(dimension.getCode() + ". " + dimension.getDescription());
            }            
            // calls the inputInt method of the superclass
            choiceDimension = inputInt("Choice : ");
            
            // repeats the repeat loop if the selection is invalid it needs to be between 1 and 2 (two elements in the enum)
        } while (choiceDimension < 1 || choiceDimension > Dimension.values().length);
        
        // if choiceDimension is equal to code = 1 then set dimension 2 or 3
        // java ternary operation(Conditional Operator) 
        // REFERENCE: https://www.alura.com.br/artigos/o-que-e-o-operador-ternario?gclid=CjwKCAiAksyNBhAPEiwAlDBeLG6vdwE_t5-htapVNyjODQKuDW--2ptvltV2I27OGBVzUG51OEbgrRoCDZoQAvD_BwE
        Dimension dimension = (choiceDimension == Dimension.TWO.getCode() ? Dimension.TWO : Dimension.THREE);
        
        
        // instantiates MathOperation and populates attributes
        MathOperation operation = new MathOperation();
        operation.setUser(loggedUser);
        operation.setCreated(LocalDateTime.now());
        operation.setDimension(dimension);
        
        // calls the method to populate linear equations
        List<LinearEquation> linearEquations = populatedLinearEquations(operation);
        operation.setLinearEquations(linearEquations);
        
        // runs the list of equations to display on the screen 
        System.out.println("\nLinear Equations: ");
        for(int i=0; i< linearEquations.size(); i++) {
            LinearEquation linearEquation = linearEquations.get(i);
            // prints the linear equation that was customized in the toString method of the LinearEquation object
            System.out.println(linearEquation.toString());
        }
                
        // calls the method of the service that will calculate and record all the information of the operation in the database
        List<ResultLinearEquation> results = operationService.calculetedMatricesMethod(operation);
        // if you return the results, display the information on the user's screen
        if(!results.isEmpty()) {
            System.out.println("\nResults: ");
            
            for(int i=0; i< results.size(); i++) {
                ResultLinearEquation result = results.get(i);
                System.out.println(result.getVariable() + "= " + result.getValue());
            }
            
            System.out.println("\nSuccess saving user!");            
        } else {
            System.out.println("\nFailed to save user!");
        }
    }

    private List<LinearEquation> populatedLinearEquations(MathOperation operation) {
        int sizeDimension = (operation.getDimension() == Dimension.TWO ? 2 : 3);
        
        ArrayList linearEquations = new ArrayList();
        for(int line=0; line<sizeDimension; line++) {
            System.out.println("\nLinear Equation:");
            
            // instantiates the LinearEquation object and populates it with the information entered by the user
            LinearEquation linearEquation = new LinearEquation();
            linearEquation.setOperation(operation);
            
            // calls the inputDouble method of the superclass
            double x = inputDouble("x= ");
            linearEquation.setValueX(x);
           
            // calls the inputDouble method of the superclass
            double y = inputDouble("y= ");
            linearEquation.setValueY(y);
            
            if(Dimension.THREE == operation.getDimension()) {
                // calls the inputDouble method of the super class
                double z = inputDouble("z= ");
                linearEquation.setValueZ(z);
            }
           
            // calls the inputDouble method of the superclass
            double result = inputDouble("Result = ");
            linearEquation.setResult(result);
            
            // add to the list of linear equations
            linearEquations.add(linearEquation);
        }
        return linearEquations;
    }
    
}
