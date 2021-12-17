package linearalgebra.model;

public enum Dimension {  
    TWO(1, "Two"), 
    THREE(2, "Three");
    
    // attribute that matches the first option of the enum: 1 or 2
    
    private final int code;
    // attribute that corresponds to the second option of the enum: Two or Three
    private final String description;
    
    // when we have attributes in the enum it is mandatory to have a constructor with its attributes and setting their corresponding
    Dimension(int code, String description) {
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
