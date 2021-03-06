package linearalgebra.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import linearalgebra.model.Dimension;
import linearalgebra.model.LinearEquation;
import linearalgebra.model.MathOperation;
import linearalgebra.model.ResultLinearEquation;

// Class responsible for the persistence of MathOperation...
// also responsible for the manipulation of LinearEquation and ResultLinearEquation, as these are heavily dependent on MathOperation



public class OperationRepository extends AbstractRepository<MathOperation> {
    
    // responsible for logging (system log) system information   
    // REFERENCES -> https://www.vogella.com/tutorials/Logging/article.html
    private static final Logger logger = Logger.getLogger(OperationRepository.class.getName());

    @Override
    public boolean insert(MathOperation operation) {        
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "INSERT INTO `linear_algebra`.`Operation` (`Created_Date`,`Dimension`,`Id_User`) VALUES (?,?,?)";
            
            // database column that is the primary key (PK)
            // objective is to get the id after making the insert in the database, because we need to set the id in LinearEquation and ResultLinearEquation
            String generatedColumns[] = { "Id_Operation" };
            // connection is set with the database connection via createConnection()
            PreparedStatement ps = connection.prepareStatement(query, generatedColumns);
            ps.setTimestamp(1, Timestamp.valueOf(operation.getCreated()));
            ps.setString(2, operation.getDimension().name());
            ps.setLong(3, operation.getUser().getId());   
            // execute the query
            int rowAffected = ps.executeUpdate();

            // validates if any rows were affected in the database
            if(rowAffected > 0) {
                // get the id generated by auto-increment
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    // get the generated id
                    long id = rs.getLong(1); 
                    // to set the id on the object
                    operation.setId(id);
                    
                    // run through the list of linear equations to enter one by one into the database
                    for(int i=0; i< operation.getLinearEquations().size(); i++) {
                        LinearEquation linearEquation = operation.getLinearEquations().get(i);       
                        // calls the method that inserts linear equation
                        boolean isSuccess = insertLinearEquation(linearEquation);
                        // if unsuccessful throws an exception
                        if(!isSuccess) {
                            throw new Exception("Failed to insert linear equation");
                        }
                    }

                    // run through the list of results to insert one by one into the database
                    for(int i=0; i< operation.getResults().size(); i++) {
                        ResultLinearEquation resultLinearEquation = operation.getResults().get(i);
                        // calls the method that input result of the linear equation system
                        boolean isSuccess = insertResultLinearEquation(resultLinearEquation);
                        // if unsuccessful throws an exception
                        if(!isSuccess) {
                            throw new Exception("Failed to insert result linear equation");
                        }
                    }
                }
                return true;
            }
            
            // uses the super class closeConnection to close the database connection
            closeConnection();
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to insert operation", ex);
        } catch (Exception ex) { 
            // log system failure to system console
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }
    
    // In this method there is no opening and closing of the database connection, because whoever is calling is "managing" the connection
    private boolean insertLinearEquation(LinearEquation linearEquation) {        
        try {
            String query = "INSERT INTO `linear_algebra`.`Linear_Equation` (`X`,`Y`,`Z`,`Result`,`Id_Operation`) VALUES (?,?,?,?,?)";
            
            // connection already set with the database connection
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDouble(1, linearEquation.getValueX());
            ps.setDouble(2, linearEquation.getValueY());
            ps.setDouble(3, linearEquation.getValueZ());
            ps.setDouble(4, linearEquation.getResult());
            ps.setLong(5, linearEquation.getOperation().getId());  
            // execute the query
            int rowAffected = ps.executeUpdate();
            
            // validates if any rows were affected in the database
            if(rowAffected > 0) {                
                return true;
            }
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to insert linearEquation", ex);
        }
        return false;
    }
    
    // In this method there is no opening and closing of the database connection, because whoever is calling is "managing" the connection
    private boolean insertResultLinearEquation(ResultLinearEquation resultLinearEquation) {       
        try {
            String query = "INSERT INTO `linear_algebra`.`Result` (`Variable`,`Value`,`Id_Operation`) VALUES (?,?,?)";
           
            // connection already set with the database connection
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, resultLinearEquation.getVariable());
            ps.setDouble(2, resultLinearEquation.getValue());
            ps.setLong(3, resultLinearEquation.getOperation().getId()); 
            // execute the query
            int rowAffected = ps.executeUpdate();
            
            // validates if any rows were affected in the database
            if(rowAffected > 0) {                
                return true;
            }
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to insert operation", ex);
        }
        return false;
    }

    @Override
    public boolean update(MathOperation operation) {
        // In this system there is no functionality to update an existing operation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(long idOperation) {
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            // The order of delete is important, because of the referential integrity of the database...
            // first if you delete the table records that have no reference to another table...
            // Results and LinearEquations -> Id_Operation
                        
            // excludes the results of the operation
            deleteResults(idOperation);
            // excludes linear equations from the operation          
            deleteLinearEquations(idOperation);
            // delete the operation
            deleteOperation(idOperation);
            
            // uses the super class closeConnection to close the database connection
            closeConnection();
            
            // if you got here without generating an exception, everything worked out 
            return true;
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to delete operation", ex);
        }
        return false;
    }
    
    // In this method there is no opening and closing of the database connection, because whoever is calling is "managing" the connection
    private boolean deleteOperation(long idOperation) {
        try {
            String query = "DELETE FROM `Operation` WHERE `Id_Operation` = ?";
            
            // connection is already set with the database connection
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, idOperation);
            // execute the query
            int rowAffected = ps.executeUpdate();
                        
            // validates if any rows were affected in the database
            if(rowAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to delete operation", ex);
        }
        return false;
    }
    
    // In this method there is no opening and closing of the database connection, because whoever is calling is "managing" the connection
    private boolean deleteLinearEquations(long idOperation) {
        try {
            String query = "DELETE FROM `Linear_Equation` WHERE `Id_Operation` = ?";
            
            // connection is already set with the database connection
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, idOperation);
            // executa a query
            int rowAffected = ps.executeUpdate();
                        
            // validates if any rows were affected in the database
            if(rowAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to delete operation", ex);
        }
        return false;
    }
    
    // In this method there is no opening and closing of the database connection, because whoever is calling is "managing" the connection
    private boolean deleteResults(long idOperation) {
        try {                       
            String query = "DELETE FROM `Result` WHERE `Id_Operation` = ?";
            
            // connection is already set with the database connection
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, idOperation);
            // execute the query
            int rowAffected = ps.executeUpdate();
                        
            // validates if any rows were affected in the database
            if(rowAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to delete operation", ex);
        }
        return false;
    }

    @Override
    public MathOperation get(long idOperation) {
        MathOperation operation = null;
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "SELECT `Id_Operation`, `Created_Date`, `Dimension`, `Id_User` FROM `Operation` WHERE `Id_Operation` = ?";
            
            PreparedStatement ps = connection.prepareStatement(query); 
            ps.setLong(1, idOperation);
            // execute the query
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                long IdOperation = rs.getLong("Id_Operation");
                
                // Instantiate MathOperation and populate with query result
                operation = new MathOperation();
                operation.setId(IdOperation);
                operation.setDimension(Dimension.valueOf(rs.getString("Dimension")));
                operation.setCreated(rs.getTimestamp("Created_Date").toLocalDateTime()); 
                // calls the method to populate linear equations
                operation.setLinearEquations( getLinearEquations(operation) );
                // call the method to populate the results
                operation.setResults( getResults(operation) );
            }
            
            // uses the super class closeConnection to close the database connection
            closeConnection();            
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to get all mathOperations by idUser", ex);
        }
        return operation;
    }
    
    // In this method there is no opening and closing of the database connection, because whoever is calling is "managing" the connection
    private List<LinearEquation> getLinearEquations(MathOperation operation) {
        List<LinearEquation> linearEquations = new ArrayList<>();
        try {
            String query = "SELECT `Id_Linear_Equation`, `X`, `Y`, `Z`, `Result` FROM `Linear_Equation` WHERE `Id_Operation` = ?";
            
            // connection is already set with the database connection
            PreparedStatement ps = connection.prepareStatement(query); 
            ps.setLong(1, operation.getId());
            // executa a query
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                // Instantiate LinearEquation and popular with query result
                LinearEquation linearEquation = new LinearEquation();
                linearEquation.setId(rs.getLong("Id_Linear_Equation"));
                linearEquation.setValueX(rs.getDouble("X"));
                linearEquation.setValueY(rs.getDouble("Y"));
                linearEquation.setValueZ(rs.getDouble("Z"));
                linearEquation.setResult(rs.getDouble("Result"));
                linearEquation.setOperation(operation);
                
                // add to the list of linear equations
                linearEquations.add(linearEquation);
            }                     
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to get all linear equations by operation", ex);
        }
        return linearEquations;
    }

    // In this method there is no opening and closing of the database connection, because whoever is calling is "managing" the connection
    private List<ResultLinearEquation> getResults(MathOperation operation) {
        List<ResultLinearEquation> results = new ArrayList<>();
        try {
            String query = "SELECT `Id_Result`, `Variable`, `Value` FROM `Result` WHERE `Id_Operation` = ?";
            
            // connection is already set with the database connection
            PreparedStatement ps = connection.prepareStatement(query);    
            ps.setLong(1, operation.getId());
            // execute the query
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                // Instantiate ResultLinearEquation and populate with query result
                ResultLinearEquation result = new ResultLinearEquation();
                result.setId(rs.getLong("Id_Result"));
                result.setVariable(rs.getString("Variable"));
                result.setValue(rs.getDouble("Value"));
                result.setOperation(operation);
                
                // add to the list of results
                results.add(result);
            }                      
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to get all results by operation", ex);
        }
        return results;
    }
    
    public List<MathOperation> getMathOperationsByUser(long idUser) {
        List<MathOperation> operations = new ArrayList<>();
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "SELECT `Id_Operation`, `Created_Date`, `Dimension` FROM `Operation` WHERE `Id_User` = ?";
            
            // connection is set with the database connection via createConnection()
            PreparedStatement ps = connection.prepareStatement(query);  
            ps.setLong(1, idUser);
            // execute the query       
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                // Instantiate MathOperation and populate with query result
                MathOperation operation = new MathOperation();
                operation.setId(rs.getLong("Id_Operation"));
                operation.setDimension(Dimension.valueOf(rs.getString("Dimension")));
                operation.setCreated(rs.getTimestamp("Created_Date").toLocalDateTime()); 
                
                // add to the list of operations
                operations.add(operation);
            }
            
            // uses the super class closeConnection to close the database connection
            closeConnection();            
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to get all mathOperations by idUser", ex);
        }
        return operations;
    }

}
