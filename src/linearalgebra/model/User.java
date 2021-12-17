package linearalgebra.model;

import java.time.LocalDateTime;
import java.util.List;

public class User extends AbstractEntity {
    
    private String name;
    private String surname;
    private Integer age;
    private Sex sex;
    private AccountType accountType;
    private String username;
    private String password;
    private List<MathOperation> operations;
    private LocalDateTime created;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<MathOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<MathOperation> operations) {
        this.operations = operations;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
}
