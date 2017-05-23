package solutions.lhdev.app.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import Server.Server;
import Models.User;

public class LoginActivity extends AppCompatActivity {

    private View buttonLogin;
    private View textViewRestartPassword;
    private EditText eTuser;
    private EditText eTpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonLogin =  findViewById(R.id.buttonRegister);
        textViewRestartPassword = findViewById(R.id.textViewRestartPassword);
        eTuser = (EditText) findViewById(R.id.editTextUser);
        eTpass = (EditText) findViewById(R.id.editTextPassword1);

        buttonLogin.setOnClickListener(new LoginClick());
        textViewRestartPassword.setOnClickListener(new RestartPassClick());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.registro) {
            Toast.makeText(LoginActivity.this, "Le has dado a registro...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("message", "tonto");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoginClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(v.getContext(), InicioActivity.class);
            //startActivity(intent);
            //User user = Server.getInstance().Login(eTuser.getText().toString().trim(),eTpass.getText().toString().trim());
            new LoginTask().execute(eTuser.getText().toString().trim(),eTpass.getText().toString().trim());
        }
    }

    private class RestartPassClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v){
            Toast.makeText(LoginActivity.this, ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
        }
    }

    private class LoginTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... strings) {
            try {
                return Server.getInstance().Login(strings[0],strings[1]);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
