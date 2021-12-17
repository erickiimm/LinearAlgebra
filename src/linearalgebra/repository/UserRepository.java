package linearalgebra.repository;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import linearalgebra.model.AccountType;
import linearalgebra.model.Sex;
import linearalgebra.model.User;

// Class responsible for User persistence...
public class UserRepository extends AbstractRepository<User> {
    
    // responsible for logging (system log) system information    
    // REFERENCES -> https://www.vogella.com/tutorials/Logging/article.html
    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());

    @Override
    public boolean insert(User user) {         
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "INSERT INTO `User` (`Name`,`Surname`,`Age`,`Sex`,`Username`,`Password`,`Account_Type`,`Created_Date`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            // connection is set with the database connection via createConnection()
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getName());
            
            // when the attribute allows it to be null, it is necessary to treat here to execute the query -> ps.setNull(<Indice>, <Type Data in Database>)
            if(user.getSurname() != null) {
                ps.setString(2, user.getSurname());
            } else {
                ps.setNull(2, Types.VARCHAR);
            }
            
            if(user.getAge() != null) {
                ps.setInt(3, user.getAge());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            if(user.getSex() != null) {
                ps.setString(4, user.getSex().name());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getAccountType().name());
            ps.setTimestamp(8, Timestamp.valueOf(user.getCreated()));
            // executa a query
            int rowAffected = ps.executeUpdate();
            
            // validates if any rows were affected in the database
            if(rowAffected > 0) {
                return true;
            }

            // uses the super class closeConnection to close the database connection
            closeConnection();      
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to insert user", ex);
        } 
        return false;
    }

    @Override
    public boolean update(User user) {
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "UPDATE `User` SET `Name` = ?, `Surname` = ?, `Age` = ?, `Sex` = ?, `Username` = ?,`Password` = ?, `Account_Type` = ? WHERE `Id_User` = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getSex().name());
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getAccountType().name());
            ps.setLong(8, user.getId());
            // execute the query
            int rowAffected = ps.executeUpdate();
            
            // uses the super class closeConnection to close the database connection
            closeConnection();
            
            // validates if any rows were affected in the database
            if(rowAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to update user", ex);            
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "DELETE FROM `User` WHERE `Id_User` = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            // execute the query
            int rowAffected = ps.executeUpdate();
            
            // uses the super class closeConnection to close the database connection
            closeConnection();
            
           // validates if any rows were affected in the database
            if(rowAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to delete user", ex);
        }
        return false;
    }

    @Override
    public User get(long id) {
        User user = null;
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "SELECT `Id_User`,`Name`,`Surname`,`Age`,`Sex`,`Username`,`Password`,`Account_Type`,`Created_Date` FROM `User` WHERE `Id_User` = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            // execute the query
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                // call the method to populate the user
                user = populateUser(rs);
            }
            
            // uses the super class closeConnection to close the database connection
            closeConnection();            
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to get user", ex);
        }
        return user;
    }
        
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "SELECT `Id_User`,`Name`,`Surname`,`Age`,`Sex`,`Username`,`Password`,`Account_Type`,`Created_Date` FROM `User`";
            PreparedStatement ps = connection.prepareStatement(query); 
            // execute the query
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                // call the method to populate the user
                User user = populateUser(rs);
                
                // add to user list
                users.add(user);
            }
            
            // uses the super class closeConnection to close the database connection
            closeConnection();            
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to get all users", ex);
        }
        return users;
    }
    
    public User findByUsername(String username) {
        User user = null;
        try {
            // uses the super class's createConnection to open the database connection
            createConnection();
            
            String query = "SELECT `Id_User`,`Name`,`Surname`,`Age`,`Sex`,`Username`,`Password`,`Account_Type`,`Created_Date` FROM `User` WHERE `Username` = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            // execute the query
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                // call the method to populate the user
                user = populateUser(rs);
            }
            
            // uses the super class closeConnection to close the database connection
            closeConnection();            
        } catch (SQLException ex) {
            // log system failure to system console
            logger.log(Level.SEVERE, "Failed to get user", ex);
        }
        return user;
    }
        
    private User populateUser(ResultSet rs) throws SQLException {
        // in the database records the Sex with the enum MALE ou FEMALE
        // Sex.valueOf() -> convert string to enum via value MALE ou FEMALE
        Sex sex = (rs.getString("Sex") != null ? Sex.valueOf(rs.getString("Sex")) : null);
        
        // Instantiate the user with the information obtained from the database
        User user = new User();
        user.setId(rs.getLong("Id_User"));
        user.setName(rs.getString("Name"));
        user.setSurname(rs.getString("Surname"));
        user.setAge(rs.getInt("Age"));
        user.setSex(sex);
        user.setUsername(rs.getString("Username"));
        user.setPassword(rs.getString("Password"));
        user.setAccountType(AccountType.valueOf(rs.getString("Account_Type")));
        user.setCreated(rs.getTimestamp("Created_Date").toLocalDateTime());        
        return user;
    }
    
}
