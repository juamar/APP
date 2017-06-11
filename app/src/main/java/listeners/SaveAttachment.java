package listeners;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import models.Message;
import server.Server;
import solutions.lhdev.app.app.ConversationActivity;
import solutions.lhdev.app.app.Manifest;
import solutions.lhdev.app.app.R;

/**
 * Created by juan on 6/06/17.
 */

public class SaveAttachment implements View.OnClickListener{

    private Message message;

    public SaveAttachment(Message message) throws IOException {
        this.message = message;
    }

    @Override
    public void onClick(View v)
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            Intent intent = new Intent("save");
            Server.getInstance().setMessageAttachment(message);
            v.getContext().sendBroadcast(intent);
        }
        else
        {
            //This code is usefull for Android prior to 6.0
            File file = new File(Environment.getExternalStorageDirectory(), message.getAttachmentName());

            if (file.exists()) {
                file.delete();
            }

            try {
                FileOutputStream fos = new FileOutputStream(file.getPath());
                System.out.println(file.getPath());
                fos.write(message.getAttachmentBynary());
                fos.close();
                Toast.makeText(v.getContext(), v.getResources().getString(R.string.fileLocation) + file.getPath(), Toast.LENGTH_LONG).show();
            } catch (java.io.IOException e) {
                Log.e("SavingFile", "Exception in photoCallback", e);
            }
        }
    }
}
