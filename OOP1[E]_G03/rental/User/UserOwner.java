package User;

import java.lang.*;

public class UserOwner {
   
    private String name;
    private String email;
    private String password;


    public UserOwner(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
    

