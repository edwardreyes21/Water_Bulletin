package android.example.waterbulletin;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ResetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Reset intake", "Intent received, resetting intake");

        // Will reset the current intake to 0, but will not update the images/text
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor saved_current_intake_editor = sharedPreferences.edit();
        saved_current_intake_editor.putInt("" + R.string.saved_current_intake, 0);
        saved_current_intake_editor.apply();

        SharedPreferences.Editor image_progress_editor = sharedPreferences.edit();
        image_progress_editor.putInt("" + R.string.saved_image_progress, 0);
        image_progress_editor.apply();
    }
}
