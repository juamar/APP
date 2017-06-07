package solutions.lhdev.app.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import models.Message;
import models.User;
import server.RegistrationService;
import server.Server;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextEmail =  (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRepeatPassword = (EditText) findViewById(R.id.editTextRepeatPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new ClickRegister());
        editTextPassword.setOnFocusChangeListener(new PassTempListener());
    }

    private class ClickRegister implements View.OnClickListener {
        @Override
        public void onClick(View v)
        {
            User user = new User(1, editTextName.getText().toString(), editTextLastName.getText().toString(), editTextEmail.getText().toString(), editTextPhone.getText().toString());
            if (editTextPassword.getText().toString().equals(editTextRepeatPassword.getText().toString()))
            {
                if (editTextPassword.getText().toString().length() > 3)
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.loading), Toast.LENGTH_SHORT).show();
                    user.setPassword(editTextPassword.getText().toString());
                    new RegisterActivity.RegisterTask().execute(user);
                }
                else
                {
                    Toast.makeText(v.getContext(), getResources().getString(R.string.passwordTooShort), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(v.getContext(), getResources().getString(R.string.passwordMatchingIncorrect), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class PassTempListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Toast.makeText(v.getContext(), "POR FAVOR, NO INGRESES UNA CLAVE DE VERDAD. ESTA APP TODAVÍA ES MUY INSEGURA.", Toast.LENGTH_LONG).show();
        }
    }

    private class RegisterTask extends AsyncTask<User, Void, Integer> {
        @Override
        protected Integer doInBackground(User... users) {
            try {
                return Server.getInstance().register(users[0]);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                return 1;
            }
        }

        protected void onPostExecute(Integer result)
        {
            if (result.equals(0))
            {
                Intent i = new Intent(getApplicationContext(), RegistrationService.class);
                startService(i);
                Intent intent = new Intent(RegisterActivity.this, InicioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.registerProblem), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
