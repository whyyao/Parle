package cs48.project.com.parl.models;

import java.util.ArrayList;

/**
 * Created by yaoyuan on 4/22/17.
 */

public class User {
    public String uid;
    public String email;
    public String firebaseToken;
    public String userName;
    public String language;

    public User(){

    }

    public User(String uid, String email, String firebaseToken, String userName, String language){
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
        this.userName = userName;
        this.language = language;
    }
}
