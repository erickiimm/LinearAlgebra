package linearalgebra.service;

import java.util.ArrayList;
import java.util.List;
import linearalgebra.model.Dimension;
import linearalgebra.model.LinearEquation;
import linearalgebra.model.MathOperation;
import linearalgebra.model.ResultLinearEquation;
import linearalgebra.repository.OperationRepository;

// responsible for the business rule of the application. in this case all the rules regarding the mathematical operation: calculation, persistence,...
public class OperationService {
    
    private final OperationRepository operationRepository;
    
    // class constructor
    public OperationService() {
        // instantiating the necessary repositories
        operationRepository = new OperationRepository();
    }
            
    // method that calculate the linear system through the matrix method
    public List<ResultLinearEquation> calculetedMatricesMethod(MathOperation operation) {
        // Calculation using the matrix method
        // X = inverseA * B  
        double[][] resolutionMatrix = null;
        // check what the dimension of the linear system is 2x2 ou 3x3
        if(Dimension.TWO == operation.getDimension()) {
            // if 2x2 get the linear equations
            List<LinearEquation> linearEquations = operation.getLinearEquations();
            // check if it really has 2 equations as expected otherwise it throws an exception
            if(linearEquations != null && linearEquations.size() == 2) {
                // get the first equation from the list
                LinearEquation equation1 = linearEquations.get(0);
                // get the second equation from the list
                LinearEquation equation2 = linearEquations.get(1);
                
                // create matrix A
                double[][] a = createMatrix2x2(equation1.getValueX(), equation1.getValueY(), equation2.getValueX(), equation2.getValueY());
                // calculates the determinant of matrix A
                double determinant = determinantCalculator(a);
                // creates adjunct matrix from matrix A
                double[][] adjunctA = createAdjunctMatrix(a); 
                // create matrix B
                double[][] b = createMatrix2x1(equation1.getResult(), equation2.getResult());
                
                // calculates the solution with the information obtained previously -> X = inverseA * B
                resolutionMatrix = resolutionCalculator(determinant, adjunctA, b);                
            } else {
                throw new IllegalArgumentException("Invalid operation!");
            }
        } else if(Dimension.THREE == operation.getDimension()) {
            // if it is 3x3 it gets the linear equations
            List<LinearEquation> linearEquations = operation.getLinearEquations();
            // check if it really has 3 equations as expected otherwise it throws an exception
            if(linearEquations != null && linearEquations.size() == 3) {
                // get the first equation from the list
                LinearEquation equation1 = linearEquations.get(0);
                // get the second equation from the list
                LinearEquation equation2 = linearEquations.get(1);
                // get the third equation from the list
                LinearEquation equation3 = linearEquations.get(2);
                
                // create matrix A
                double[][] a = createMatrix3x3(equation1.getValueX(), equation1.getValueY(), equation1.getValueZ(), equation2.getValueX(), equation2.getValueY(), equation2.getValueZ(), equation3.getValueX(), equation3.getValueY(), equation3.getValueZ());                
                // calculates the determinant of matrix A
                double determinant = determinantCalculator(a);
                // creates adjunct matrix from matrix A
                double[][] adjunctA = createAdjunctMatrix(a); 
                // create matrix B
                double[][] b = createMatrix3x1(equation1.getResult(), equation2.getResult(), equation3.getResult());
                
                // calculates the solution with the information obtained previously -> X = inverseA * B
                resolutionMatrix = resolutionCalculator(determinant, adjunctA, b);                
            } else {
                throw new IllegalArgumentException("Invalid operation!");
            }
        } else {
            throw new IllegalArgumentException("Invalid operation!");
        }
                
        // if there is the resolution
        if(resolutionMatrix != null) {
            List<ResultLinearEquation> results = new ArrayList<>();
            
            // run through the list of results
            for(int i=0; i< resolutionMatrix.length; i++) { 
                String variable;
                if(i == 0) {
                    // the first position in the list will be the value of x
                    variable = "x";
                } else if(i == 1) {
                    // the second position in the list will be the value of y
                    variable = "y";
                } else {
                    // the third position in the list will be the value of z
                    variable = "z";
                }
                
                // Instantiate and populate the result object with the result information
                ResultLinearEquation result = new ResultLinearEquation();
                result.setOperation(operation);
                result.setVariable(variable);
                result.setValue(resolutionMatrix[i][0]);
            
                // insert in the results list
                // if it is dimension 2 we will have two results x and y otherwise we will have 3 results x, y and z
                results.add(result);
            }
            // set the results in the operation object
            operation.setResults(results);
            
            // inserts into the database the operation that was populated in parts on the screen and another part calculated.
            boolean isSuccess = operationRepository.insert(operation);
            // if successfully recorded, return the results to display on the screen to the user
            if(isSuccess) {
                return results;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    // get all operations for a user
    public List<MathOperation> getMathOperations(long idUser) {
        return operationRepository.getMathOperationsByUser(idUser);
    }
    
    // get an operation based on his id
    public MathOperation getMathOperation(long idMathOperation) {
        return operationRepository.get(idMathOperation);
    }
    
    public double determinantCalculator(double[][] matrix) {
        // REFERENCES -> https://pt.symbolab.com/solver/matrix-determinant-calculator/%5Cdet%20%5Cbegin%7Bpmatrix%7D1%20%26%202%20%26%203%20%5C%5C4%20%26%205%20%26%206%20%5C%5C7%20%26%208%20%26%209%5Cend%7Bpmatrix%7D?or=ex
        if(matrix.length == 2) {    
            // calculating the determinant of the 2x2 matrix
            return ( matrix[0][0] * matrix[1][1] ) - ( matrix[0][1] * matrix[1][0] );            
        } else if(matrix.length == 3) {     
            // calculating the determinant of the 2x2 matrix
            double a = matrix[0][0];
            double[][] matrixA = createMatrix2x2(matrix[1][1], matrix[1][2], matrix[2][1], matrix[2][2]);
            
            double b = matrix[0][1];
            double[][] matrixB = createMatrix2x2(matrix[1][0], matrix[1][2], matrix[2][0], matrix[2][2]);
            
            double c = matrix[0][2];
            double[][] matrixC = createMatrix2x2(matrix[1][0], matrix[1][1], matrix[2][0], matrix[2][1]);
            
            return  (a * determinantCalculator(matrixA)) - (b * determinantCalculator(matrixB)) + (c * determinantCalculator(matrixC));            
        } else {
            throw new IllegalArgumentException("Invalid operation!");
        }        
    }
    
    private double[][] resolutionCalculator(double determinant, double[][] adjunctA, double[][] b) {
        if(adjunctA.length == 2) {
            // matrix multiplication adjunctA(2x2) * B(2x1)
            double x = (adjunctA[0][0] * b[0][0]) + (adjunctA[0][1] * b[1][0]);
            double y = (adjunctA[1][0] * b[0][0]) + (adjunctA[1][1] * b[1][0]);        
            
            // creates a result matrix 2x1 and multiply each value with 1/determinant found previously
            return createMatrix2x1(x * (1/determinant), y * (1/determinant));            
        } else if(adjunctA.length == 3) {
            // matrix multiplication adjunctA(3x3) * B(3x1)
            double x = (adjunctA[0][0] * b[0][0]) + (adjunctA[0][1] * b[1][0]) + (adjunctA[0][2] * b[2][0]);
            double y = (adjunctA[1][0] * b[0][0]) + (adjunctA[1][1] * b[1][0]) + (adjunctA[1][2] * b[2][0]); 
            double z = (adjunctA[2][0] * b[0][0]) + (adjunctA[2][1] * b[1][0]) + (adjunctA[2][2] * b[2][0]);
            
            // creates a result matrix 3x1 and multiply each value with 1/determinant found previously
            return createMatrix3x1(x * (1/determinant), y * (1/determinant), z * (1/determinant));
        } else {
            // Invalid array dimension
            throw new IllegalArgumentException("Invalid operation!");
        }
    }
    
    private double[][] createAdjunctMatrix(double[][] a) {
        // REFERENCES -> https://pt.wikipedia.org/wiki/Matriz_adjunta
        if(a.length == 2) {
            // create adjunct matrix 2x2
            double[][] matrix = new double[2][2];
            matrix[0][0] = a[1][1];
            matrix[0][1] = -a[0][1];
            matrix[1][0] = -a[1][0];
            matrix[1][1] = a[0][0];
            return matrix;
        } else if(a.length == 3) {
            // create adjunct matrix 3x3
            double x1 = determinantCalculator( createMatrix2x2(a[1][1], a[1][2], a[2][1], a[2][2]) );
            double y1 = determinantCalculator( createMatrix2x2(a[0][1], a[0][2], a[2][1], a[2][2]) );
            double z1 = determinantCalculator( createMatrix2x2(a[0][1], a[0][2], a[1][1], a[1][2]) );
            
            double x2 = determinantCalculator( createMatrix2x2(a[1][0], a[1][2], a[2][0], a[2][2]) );
            double y2 = determinantCalculator( createMatrix2x2(a[0][0], a[0][2], a[2][0], a[2][2]) );
            double z2 = determinantCalculator( createMatrix2x2(a[0][0], a[0][2], a[1][0], a[1][2]) );
            
            double x3 = determinantCalculator( createMatrix2x2(a[1][0], a[1][1], a[2][0], a[2][1]) );
            double y3 = determinantCalculator( createMatrix2x2(a[0][0], a[0][1], a[2][0], a[2][1]) );
            double z3 = determinantCalculator( createMatrix2x2(a[0][0], a[0][1], a[1][0], a[1][1]) );
            
            double[][] matrix = new double[3][3];
            matrix[0][0] = +x1;
            matrix[0][1] = -y1;
            matrix[0][2] = +z1;
            matrix[1][0] = -x2;
            matrix[1][1] = +y2;
            matrix[1][2] = -z2;
            matrix[2][0] = +x3;
            matrix[2][1] = -y3;
            matrix[2][2] = +z3;            
            return matrix;
        } else {
            throw new IllegalArgumentException("Invalid operation!");
        }
    }
    
    private double[][] createMatrix2x1(double x, double y) {
        double[][] matrix = new double[2][1];
        matrix[0][0] = x;        
        matrix[1][0] = y;       
        return matrix;
    }
    
    private double[][] createMatrix3x1(double x, double y, double z) {
        double[][] matrix = new double[3][1];
        matrix[0][0] = x;        
        matrix[1][0] = y;
        matrix[2][0] = z;
        return matrix;
    }
    
    private double[][] createMatrix2x2(double x1, double y1, double x2, double y2) {
        double[][] matrix = new double[2][2];
        matrix[0][0] = x1;
        matrix[0][1] = y1;
        matrix[1][0] = x2;
        matrix[1][1] = y2;
        return matrix;
    }
    
    private double[][] createMatrix3x3(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        double[][] matrix = new double[3][3];
        matrix[0][0] = x1;
        matrix[0][1] = y1;
        matrix[0][2] = z1;
        matrix[1][0] = x2;
        matrix[1][1] = y2;
        matrix[1][2] = z2;        
        matrix[2][0] = x3;
        matrix[2][1] = y3;
        matrix[2][2] = z3;
        return matrix;
    }
              
}
