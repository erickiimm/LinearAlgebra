package linearalgebra.model;

/* 
- Entity Abstract Class
- An abstract class cannot be instantiated
- All classes that extend this class will have access to the public attributes.
- It was created to reuse the id attribute in all classes that are persisted in the database.
- Also in order to separate the model from data persistence...

Visibility:
- public: everyone accesses this information
- protected: only the parent class and the child class access this attribute.
- private: only the class itself accesses this attribute.
*/
public abstract class AbstractEntity {
    
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
