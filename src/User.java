import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joe on 21/09/2016.
 */
public class User {
    //store user information

    String name;
    String password;

    ArrayList<Message> messageList = new ArrayList<>();

    public User(String name, String pword) {
        this.name = name;
        this.password=pword;
    }

    public boolean checkPassword(String name, String pword){return pword.equals(password);}

}
