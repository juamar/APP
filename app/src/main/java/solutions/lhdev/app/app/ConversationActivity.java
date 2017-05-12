package solutions.lhdev.app.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class ConversationActivity extends AppCompatActivity {

    private TextView tVFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tVFriend = (TextView) findViewById(R.id.tVFriend);
        SpannableString content = new SpannableString("Paula Valero");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tVFriend.setText(content);
    }

}
