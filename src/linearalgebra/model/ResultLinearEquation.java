package linearalgebra.model;

//where the results of mathematical operations will be created.

public class ResultLinearEquation extends AbstractEntity {
    
    private MathOperation operation;
    
    private String variable;
    
    private double value;
    
    public MathOperation getOperation() {
        return operation;
    }

    public void setOperation(MathOperation operation) {
        this.operation = operation;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }     
    
}
