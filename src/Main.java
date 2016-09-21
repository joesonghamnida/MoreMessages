import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joe on 21/09/2016.
 */
public class Main {
    static User user;
    //static String pword;

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {

        //Spark.staticFileLocation("/public");
        Spark.init();

        //three parameters: path, route, response transformer
        Spark.get("/",
                ((request, response) -> {

                 Session session = request.session();
                    String name = session.attribute("userName");
                    String pword=session.attribute("password");


                    User user = users.get(name);

                    HashMap h = new HashMap();
                    if (user == null){
                        return new ModelAndView(h, "index.html");
                    }
                    else if(!user.checkPassword(name,pword)){
                        System.out.println("password error");
                        return new ModelAndView(h, "index.html");
                    }
                    else {
                        h.put("name", user.name);
                        h.put("password", user.password);
                        h.put("messages", user.messageList);
                        return new ModelAndView(h, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post("/create-user",
                ((request, response) -> {
                    String name = request.queryParams("name");
                    String pword = request.queryParams("password");

                    user = users.get(name);
                    if (user == null) {
                        user = new User(name, pword);
                        users.put(name,user);
                    }

                    //these are cookies
                    Session session = request.session();
                    session.attribute("userName", user.name);
                    session.attribute("password", pword);

                    response.redirect("/");
                    return "";
                }));

        Spark.post("/create-message",
                (request, response) -> {
                    String message = request.queryParams("message");
                    Message m = new Message(message);
                    user.messageList.add(m);
                    response.redirect("/");
                    return "";
                });//post

        Spark.post("/delete-message",((request, response) -> {

            //index of message
            int delete=Integer.parseInt(request.queryParams("delete"));

            //need to get message index from array list
            user.messageList.remove(delete-1);
            response.redirect("/");
        return "";
        }));

        Spark.post("/edit-message", ((request, response) -> {
            int editNumber=Integer.parseInt(request.queryParams("edit-message-num"));
            String editedText=request.queryParams("edit-message-text");
            Message m = new Message(editedText);
            user.messageList.remove(editNumber-1);
            user.messageList.add(editNumber-1,m);

            response.redirect("/");
            return "";
        }));

        Spark.post("/logout", ((request, response) -> {

                    Session session=request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
    }//main
}//Main
