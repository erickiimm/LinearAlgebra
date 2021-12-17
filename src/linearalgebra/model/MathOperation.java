package linearalgebra.model;

// creation of mathematical operations

import java.time.LocalDateTime;
import java.util.List;

public class MathOperation extends AbstractEntity {
    
    private User user;
    
    private List<LinearEquation> linearEquations;
    
    private Dimension dimension;
    
    private LocalDateTime created;
    
    private List<ResultLinearEquation> results;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<LinearEquation> getLinearEquations() {
        return linearEquations;
    }

    public void setLinearEquations(List<LinearEquation> linearEquations) {
        this.linearEquations = linearEquations;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<ResultLinearEquation> getResults() {
        return results;
    }

    public void setResults(List<ResultLinearEquation> results) {
        this.results = results;
    }
      
}
