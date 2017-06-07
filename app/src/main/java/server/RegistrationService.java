package server;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;
import solutions.lhdev.app.app.R;

/**
 * Created by JuanIgnacio on 03/06/2017.
 */

public class RegistrationService extends IntentService
{
    public RegistrationService()
    {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            InstanceID myID = InstanceID.getInstance(this);
            String registrationToken = myID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("Registration Token", registrationToken);
            Server.getInstance().getUser().setToken(registrationToken);
            Server.getInstance().updateUser(Server.getInstance().getUser());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
