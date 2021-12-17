package linearalgebra.model;

public enum AccountType {
    ADMIN(1, "Admin"), 
    REGULAR_USER(2, "Regular User");  
    
    
    private final int code;
    // attribute that corresponds to the second option of the enum: Admin or Regular User
    private final String description;
    
    // when I have attributes in the enum it is mandatory to have a constructor with its attributes and setting their corresponding
    AccountType(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    // get of your attributes
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}
