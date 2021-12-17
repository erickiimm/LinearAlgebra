package linearalgebra.service;

import java.util.List;
import linearalgebra.model.MathOperation;
import linearalgebra.model.User;
import linearalgebra.repository.OperationRepository;
import linearalgebra.repository.UserRepository;

// responsible for the business rule of the application. in this case all the rules regarding the user: persistence, validation,...

public class UserService {
    
    private final UserRepository userRepository;
    private final OperationRepository operationRepository;
    
    //class constructor
    public UserService() {
        // instantiating the necessary repositories
        userRepository = new UserRepository();
        operationRepository = new OperationRepository();
    }
    
    // method that allows you to add or update a user record
    // if the user has an id, it is a sign that it was already persisted at another time, so it is an update, otherwise it is inclusion
    public boolean salveUser(User user) {
        if(user.getId() == null) {
            return userRepository.insert(user);
        } else {
            return userRepository.update(user);
        }
    }
    
    // delete users and their operations
    // the order of deletion is important. database referential integrity   
    public boolean deleteUser(long idUser) {       
        // get user operations that will be deleted
        List<MathOperation> operations = operationRepository.getMathOperationsByUser(idUser);
        // run through the list of operations
        for(int i=0; i< operations.size(); i++) {
            MathOperation operation = operations.get(i);
            // delete operation one by one
            operationRepository.delete(operation.getId());
        }        
        // in the end deletes the user
        return userRepository.delete(idUser);
    }
    
    public User getUser(long idUser) {
        return userRepository.get(idUser);
    }
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
