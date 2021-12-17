package linearalgebra.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public abstract class AbstractRepository<T> {
    
    // Database connection
    protected Connection connection;
    
    // centralization of database settings
    // before any query it is necessary to open the database connection
    protected void createConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/linear_algebra";
        String user = "root"; 
        String password = "Pass@123";
        connection = DriverManager.getConnection(url, user, password);
    }
    
    // after running it is always necessary to close the connection
    protected void closeConnection() throws SQLException {
        if(connection != null) {
            connection.close();
        }        
    }
        
    // all classes that extend to this one have to do their implementations of the methods below.
    
    // insert the entity(object) passed as parameter
    public abstract boolean insert(T entity);
    
    // updates the entity(object) passed as parameter
    public abstract boolean update(T entity);
    
    // delete the entity with the id passed in the parameter
    public abstract boolean delete(long id);
    
    // get the entity with the id passed in the parameter
    public abstract T get(long id);
        
}
