package cs48.project.com.parl.models;

/**
 * Created by yaoyuan on 4/22/17.
 */

public class User {
    public String uid;
    public String email;
    public String firebaseToken;
    public String userName;
    public String language;
    public boolean loggedIn;
    public String photoURL;
    public User() {

    }

    public User(String uid, String email, String firebaseToken, String userName, String language, boolean loggedIn) {
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
        this.userName = userName;
        this.language = language;
        this.loggedIn = loggedIn;
        this.photoURL=null;
    }

    public User(String uid, String email, String firebaseToken, String userName, String language, boolean loggedIn, String photoURL) {
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
        this.userName = userName;
        this.language = language;
        this.loggedIn = loggedIn;
        this.photoURL = photoURL;
    }
}
