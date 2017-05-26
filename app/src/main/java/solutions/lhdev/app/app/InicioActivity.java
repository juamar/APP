package solutions.lhdev.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import Models.Message;
import Server.Server;
import Models.Conversation;
import Models.User;
import Models.Message;

public class InicioActivity extends AppCompatActivity {

    private TextView tVPerfil;
    private View IBConversationButton;
    private LinearLayout contenedor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tVPerfil = (TextView) findViewById(R.id.tVPerfil);
        SpannableString content = new SpannableString(Server.getInstance().getUser().getSurname());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tVPerfil.setText(content);
        contenedor1 = (LinearLayout) findViewById(R.id.contenedor1);
        User user = new User(99, "pepe", "Viñas", "pepe@vinas.com", "666777888");
        contenedor1.addView(new Conversation().buildConversation(user,new Message(1, "Hola Buen día!", user, new Conversation(1), false, 1, 99),1,contenedor1.getContext()));

        IBConversationButton = findViewById(R.id.IBConversationButton);

        IBConversationButton.setOnClickListener(new ConversationClick());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    private class ConversationClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ConversationActivity.class);
            startActivity(intent);
        }
    }

}
