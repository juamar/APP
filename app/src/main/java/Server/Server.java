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
    private Server server:

    private class HttpRequestTask extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            try {
                final String url = "http://rest-service.guides.spring.io/greeting"; //cambiar URL
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                User user = restTemplate.getForObject(url, User.class);
                return user;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            setUser(user);
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private Server(){}

    public Server getInstance()
    {
        if (server == null)
        {
            server = new Server();
        }

        return server;
    }
}
