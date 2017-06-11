package server;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import models.Conversation;
import models.Friendship;
import models.Message;
import models.User;
import models.UserConversation;

/**
 * Created by JuanIgnacio on 23/05/2017.
 */

public class Server {

    private User user;
    private static Server server;
    private boolean finished = false;
    private Message messageAttachment;

    public Message getMessageAttachment() {
        return messageAttachment;
    }

    public void setMessageAttachment(Message messageAttachment) {
        this.messageAttachment = messageAttachment;
    }

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
            Log.e("Server", e.getMessage(), e);
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
            Log.e("Server", e.getMessage(), e);
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

            try
            {
                for ( UserConversation userconversation2 : getUserConversationsByConversationID(userConversation.getConversationId()))
                {
                    if (userconversation2.getUserId() != this.user.getId())
                    {
                        friendId = userconversation2.getUserId();
                    }
                }
            } catch (Exception e) {
                Log.e("Server", e.getMessage(), e);
                return null;
            }

            try {
                final String url = "http://restapp.tecandweb.net/api/users/" + friendId;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                user = restTemplate.getForObject(url, User.class);
            } catch (Exception e) {
                Log.e("Server", e.getMessage(), e);
                return null;
            }

            /**Friend gotten**/

            /**Get Messages**/

            int messagesNotReaded=0;
            Message lastMessage = new Message();
            int messagesNumber = 0;

            try {
                final String url = "http://restapp.tecandweb.net/api/Messages?conversationid=" + userConversation.getConversationId();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Message[] messagesArray = restTemplate.getForObject(url, Message[].class);

                messagesNumber = messagesArray.length;

                if (messagesNumber > 0)
                {
                    lastMessage = messagesArray[messagesArray.length - 1];
                    for (Message message : messagesArray) {
                        if (!message.isReaded() && message.getUserId() != this.user.getId()) {
                            messagesNotReaded++;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Server", e.getMessage(), e);
                return null;
            }
            /**Messages Gotten**/
            if (messagesNumber > 0)
            {
                views.add(new Conversation(userConversation.getConversationId()).buildConversation(user, lastMessage, messagesNotReaded, context));
            }
        }

        return views;

    }

    public ArrayList<View> getMessages(Context context, int conversationId) {

        ArrayList<View> messages = new ArrayList<View>();

        try {
            String url = "http://restapp.tecandweb.net/api/Messages?conversationid=" + conversationId;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Message[] messagesArray = restTemplate.getForObject(url, Message[].class);


            for (Message message : messagesArray)
            {
                if (message.getUserId() == this.getUser().getId())
                {
                    messages.add(message.buildMessage(context, true));
                }
                else
                {
                    message.setReaded(true);
                    url = "http://restapp.tecandweb.net/api/Messages/" + message.getId();
                    restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    /**ResponseEntity<Message> sendResponse = **/restTemplate.put(url, message, Message.class);
                    messages.add(message.buildMessage(context, false));
                }

            }
        } catch (Exception e) {
            Log.e("Server", e.getMessage(), e);
            return null;
        }

        return messages;
    }

    public int sendMessage(Message message)
    {
        int retry = 0;
        try {
            final String url = "http://restapp.tecandweb.net/api/Messages";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            /**ResponseEntity<Message> sendResponse = **/restTemplate.postForEntity(url, message, Message.class);
        }
        catch (org.springframework.web.client.ResourceAccessException e)
        {
            Log.e("Server", e.getMessage(), e);
            return 1;
        }
        catch (Exception e)
        {
            Log.e("Server", e.getMessage(), e);
            return 2;
        }
        return 0;
    }

    public void updateUser(User user)
    {
        try {
            final String url = "http://restapp.tecandweb.net/api/users/" + this.user.getId();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.put(url, user, User.class);
        }
        catch (Exception e)
        {
            Log.e("Server", e.getMessage(), e);
        }
    }

    public Integer register(User user) {
        try {
            final String url = "http://restapp.tecandweb.net/api/users";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            /**ResponseEntity<Message> sendResponse = **/restTemplate.postForEntity(url, user, User.class);
            this.Login(user.getEmail(), user.getPassword());
            return 0;
        }
        catch (Exception e)
        {
            Log.e("Server", e.getMessage(), e);
            return 1;
        }
    }

    public ArrayList<View> getFriends(Context context) {
        ArrayList<View> friends = new ArrayList<>();

        try {
            final String url = "http://restapp.tecandweb.net/api/users";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            User[] u = restTemplate.getForObject(url, User[].class);
            for ( User user : u)
            {
                if (user.getId() != this.user.getId())
                {
                    friends.add(new Friendship().buildConversation(user, context));
                }
            }
        } catch (Exception e) {
            Log.e("Server", e.getMessage(), e);
        }
        return friends;
    }

    public Integer addConversation(User friend) {

        UserConversation[] myUserConversations;

        try {
            final String url = "http://restapp.tecandweb.net/api/UserConversations?userid=" + this.user.getId();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            myUserConversations = restTemplate.getForObject(url, UserConversation[].class);
        } catch (Exception e) {
            Log.e("Server", e.getMessage(), e);
            return null;
        }

        for (UserConversation ucs : myUserConversations)
        {
            try {
                for (UserConversation uc : getUserConversationsByConversationID(ucs.getConversationId()))
                {
                    if (uc.getUserId() == friend.getId())
                    {
                        return uc.getConversationId();
                    }
                }
            } catch (Exception e) {
                Log.e("Server", e.getMessage(), e);
                return null;
            }

        }
        try {
            String url = "http://restapp.tecandweb.net/api/Conversations";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Conversation> conversation = restTemplate.postForEntity(url, new Conversation(), Conversation.class);

            url = "http://restapp.tecandweb.net/api/UserConversations";
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            /**ResponseEntity<Message> sendResponse = **/restTemplate.postForEntity(url, new UserConversation(conversation.getBody().getId(),friend.getId()), UserConversation.class);

            url = "http://restapp.tecandweb.net/api/UserConversations";
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            /**ResponseEntity<Message> sendResponse = **/restTemplate.postForEntity(url, new UserConversation(conversation.getBody().getId(),this.user.getId()), UserConversation.class);

            return conversation.getBody().getId();
        }
        catch (Exception e)
        {
            Log.e("Server", e.getMessage(), e);
        }

        return null;

    }

    private UserConversation[] getUserConversationsByConversationID(int conversationId) throws Exception
    {
        final String url = "http://restapp.tecandweb.net/api/UserConversations?conversationId=" + conversationId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.getForObject(url, UserConversation[].class);
    }
}
