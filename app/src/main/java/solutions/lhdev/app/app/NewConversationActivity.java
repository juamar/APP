package solutions.lhdev.app.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import server.Server;

public class NewConversationActivity extends AppCompatActivity {

    private LinearLayout friendsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        friendsContainer = (LinearLayout) findViewById(R.id.friendsContainer);

        new GetFriends().execute(friendsContainer.getContext());
    }

    private class GetFriends extends AsyncTask<Context, Void, ArrayList<View>> {
        @Override
        protected ArrayList<View> doInBackground(Context... contexts) {
            try {
                return Server.getInstance().getFriends(contexts[0]);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<View> result)
        {
            for (View friend : result)
            {
                friendsContainer.addView(friend);
            }
        }

    }


}
