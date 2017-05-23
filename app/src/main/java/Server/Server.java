package Server;

import android.os.AsyncTask;
import android.util.Log;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import Models.User;

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

    public User Login(String email, String Pass)
    {
        try {
            final String url = "http://restapp.tecandweb.net/api/users?email=" + email;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            User user = restTemplate.getForObject(url, User.class);
            this.user = user;
            return user;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }
}
