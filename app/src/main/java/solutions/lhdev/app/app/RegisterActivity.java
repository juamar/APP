package solutions.lhdev.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        Toast.makeText(this, "De lo otra actividad hemos traido: " + intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "¿Has visto que bonito?", Toast.LENGTH_SHORT).show();
    }

}
