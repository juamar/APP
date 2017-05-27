package Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import Models.Conversation;
import Models.Message;
import Models.User;
import Models.UserConversation;

/**
 * Created by JuanIgnacio on 23/05/2017.
 */

public class Server {

    private User user;
    private static Server server;
    private boolean finished = false;



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private Server(){}

    public static Server getInstance()
    {
        if (server == null)
        {
            server = new Server();
        }

        return server;
    }

    public int Login(String email, String pass)
    {
        try {
            final String url = "http://restapp.tecandweb.net/api/users?email=" + email;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            this.user = restTemplate.getForObject(url, User.class);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            return 1; //user is not correct or we cannot connect to internet.
        }

        if (!this.user.getPassword().equals(pass))
        {
            return 2; //password is not correct.
        }

        return 0;
    }

    public ArrayList<View> getConversations(Context context)
    {
        UserConversation[] userConversationsArray;

        try {
            final String url = "http://restapp.tecandweb.net/api/UserConversations?userid=" + user.getId();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            userConversationsArray = restTemplate.getForObject(url, UserConversation[].class);
        } catch (Exception e) {
            Log.e("InicioActivity", e.getMessage(), e);
            return null;
        }

        ArrayList<UserConversation> userConversations = new ArrayList<UserConversation>(Arrays.asList(userConversationsArray));
        ArrayList<View> views = new ArrayList<View>();

        /**
         * Before nothing, we 've got to check if user has conversations. If not, we got to return a Void Collection.
         * If we don't do this, our code will crash.
         */
        if (userConversations.size() < 1)
        {
            return views;
        }

        for ( UserConversation userConversation : userConversations)
        {
            /**Get Friend**/
            User user;
            int friendId = 0;

            try {
                final String url = "http://restapp.tecandweb.net/api/UserConversations?conversationId=" + userConversation.getConversationId();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                UserConversation[] uc = restTemplate.getForObject(url, UserConversation[].class);
                for ( UserConversation userconversation2 : uc)
                {
                    if (userconversation2.getUserId() != this.user.getId())
                    {
                        friendId = userconversation2.getUserId();
                    }
                }
            } catch (Exception e) {
                Log.e("InicioActivity", e.getMessage(), e);
                return null;
            }

            try {
                final String url = "http://restapp.tecandweb.net/api/users/" + friendId;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                user = restTemplate.getForObject(url, User.class);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                return null;
            }

            /**Friend gotten**/

            /**Get Messages**/

            int messagesNotReaded=0;
            Message lastMessage;

            try {
                final String url = "http://restapp.tecandweb.net/api/Messages?conversationid=" + userConversation.getConversationId();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Message[] messagesArray = restTemplate.getForObject(url, Message[].class);

                lastMessage = messagesArray[messagesArray.length - 1];
                for (Message message : messagesArray)
                {
                    if (!message.isReaded() && message.getUserId() != this.user.getId())
                    {
                        messagesNotReaded++;
                    }
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                return null;
            }

            /**Messages Gotten**/

            views.add(new Conversation(userConversation.getConversationId()).buildConversation(user,  lastMessage, messagesNotReaded, context));
        }

        return views;

        //Trae Conversaciones
        //Trae el amigo asociado a la conversacion
        //Contruye vista de conversacion
    }
}
