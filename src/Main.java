import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

/**
 * Created by joe on 21/09/2016.
 */
public class Main {
    static User user;

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {

        Spark.staticFileLocation("/public");
        Spark.init();

        Spark.get("/",
                ((request, response) -> {

                    Session session = request.session();
                    String name = session.attribute("userName");

                    User user = users.get(name);

                    HashMap h = new HashMap();
                    if (user == null) {
                        return new ModelAndView(h, "index.html");
                    } else {
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
                    String password = request.queryParams("password");

                    user = users.get(name);
                    if (user == null) {
                        user = new User(name, password);
                        users.put(name, user);
                    }

                    if(!user.checkPassword(password)){
                        response.redirect("/");
                        return "";
                    }

                    Session session = request.session();
                    session.attribute("userName", user.name);

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

        Spark.post("/delete-message", ((request, response) -> {

            int messageIndex = Integer.parseInt(request.queryParams("delete"));

            user.messageList.remove(messageIndex - 1);
            response.redirect("/");
            return "";
        }));

        Spark.post("/edit-message", ((request, response) -> {
            int editNumber = Integer.parseInt(request.queryParams("edit-message-num"));
            String editedText = request.queryParams("edit-message-text");
            Message m = new Message(editedText);
            user.messageList.set(editNumber-1,m);

            response.redirect("/");
            return "";
        }));

        Spark.post("/logout", ((request, response) -> {

                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
    }//main
}//Main
