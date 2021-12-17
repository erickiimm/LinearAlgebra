package linearalgebra.model;

public class LinearEquation extends AbstractEntity {
    
    private MathOperation operation;
    
    private double valueX;
    
    private double valueY;
    
    private double valueZ;
    
    private double result;
    
    public MathOperation getOperation() {
        return operation;
    }

    public void setOperation(MathOperation operation) {
        this.operation = operation;
    }

    public double getValueX() {
        return valueX;
    }

    public void setValueX(double valueX) {
        this.valueX = valueX;
    }

    public double getValueY() {
        return valueY;
    }

    public void setValueY(double valueY) {
        this.valueY = valueY;
    }

    public double getValueZ() {
        return valueZ;
    }

    public void setValueZ(double valueZ) {
        this.valueZ = valueZ;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
    
    @Override
    public String toString() {        
        String signalY = valueY >= 0 ? " +" : " ";
        String signalZ = valueZ >= 0 ? " +" : " ";
            
        if(valueZ != 0) {
            return valueX + "x" + signalY + valueY + "y" + signalZ + valueZ + "z = " + result;
        } else {
            return valueX + "x" + signalY + valueY + "y = " + result;
        }
    }
    
}
