import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joe on 21/09/2016.
 */
public class User {


    String name;
    String password;

    ArrayList<Message> messageList = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password=password;
    }

    public boolean checkPassword(String pwd){return password.equals(pwd);}

}
