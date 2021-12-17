package linearalgebra.model;

public enum Sex {
    MALE(1, "Male"), 
    FEMALE(2, "Female");
    
    // attribute that matches the first option of the enum: 1 or 2
    private final int code;
    // attribute that corresponds to the second option of the enum: Male ou Female
    private final String description;
    
    // when we have attributes in the enum it is mandatory to have a constructor with its attributes and setting their corresponding
    Sex(int code, String description) {
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
