package android.example.waterbulletin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);

        createNotificationChannel();
        updatePushNotifsVisibility();

        updateAlarm();

        // If user taps on 'Alerts', send them to 'Alerts' page
        TextView alerts = findViewById(R.id.alerts);
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AlertsActivity.class);
                startActivity(intent);
            }
        });

        // If user taps on 'Settings', send them to 'Settings' page
        TextView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // If user taps on 'Reset Intake', then it sends an intent to HomeActivity
        // that requests to reset the current intake
        Button reset_intake = findViewById(R.id.reset_intake);
        reset_intake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                intent.putExtra("resetIntake", 1);
                startActivity(intent);
            }
        });

        // Each time the user opens the Settings page, it will update the seekbar progress
        // to what its saved value is
        int min_intake = sharedPreferences.getInt("" + R.string.settings_min_intake_key, 2000);
        SeekBar min_intake_seekbar = findViewById(R.id.min_intake_seekbar);
        min_intake_seekbar.setProgress(min_intake);
        Log.v("setProgress", "Min intake: " + min_intake);
        TextView seekbar_progress = findViewById(R.id.min_intake_seekbar_progress);
        seekbar_progress.setText("" + min_intake);

        // Listener for when the seekbar is updated by the user
        min_intake_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor min_intake_editor = sharedPreferences.edit();

                progress = (int) Math.round((progress + 99) / 100) * 100;
                Log.v("setProgress", "Min Intake Seekbar progress: " + progress);

                min_intake_editor.putInt("" + R.string.settings_min_intake_key, progress);
                min_intake_editor.apply();

                TextView seekbar_progress = (TextView) findViewById(R.id.min_intake_seekbar_progress);
                seekbar_progress.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Updates push notification key if user updates the switch
        Switch push_notifications = (Switch) findViewById(R.id.push_notifications_switch);
        push_notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor enable_notifs = sharedPreferences.edit();
                if (isChecked) {
                    enable_notifs.putBoolean("" + R.string.settings_push_notification_key, true);
                }
                else {
                    enable_notifs.putBoolean("" + R.string.settings_push_notification_key, false);
                }
                enable_notifs.apply();
                updatePushNotifsVisibility();
                updateAlarm();
            }
        });

        // Each time the user opens the Settings page, it will update the seekbar progress
        // to what its saved value is
        int frequency_value = sharedPreferences.getInt("" + R.string.settings_notification_frequency_key, 1);
        SeekBar frequency_seekbar = findViewById(R.id.push_notifications_seekbar);
        frequency_seekbar.setProgress(frequency_value);
        Log.v("setProgress", "Frequency value: " + frequency_value);
        TextView frequency_progress = findViewById(R.id.push_notifications_seekbar_progress);
        frequency_progress.setText("" + frequency_value);

        // Listener for when the seekbar is updated by the user
        frequency_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor frequency_editor = sharedPreferences.edit();
                Log.v("setProgress", "Seekbar progress: " + progress);
                frequency_editor.putInt("" + R.string.settings_notification_frequency_key, progress);
                frequency_editor.apply();
                updateAlarm();

                TextView seekbar_progress = (TextView) findViewById(R.id.push_notifications_seekbar_progress);
                seekbar_progress.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void updatePushNotifsVisibility() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);

        RelativeLayout frequency_container = findViewById(
                R.id.push_notifications_frequency_container);
        Switch push_notification = findViewById(R.id.push_notifications_switch);

        if (sharedPreferences.getBoolean(
                "" + R.string.settings_push_notification_key, false)) {
            Log.v("Shared preferences", "True - Push notification key " +
                    R.string.settings_push_notification_key);
            frequency_container.setVisibility(View.VISIBLE);
            push_notification.setChecked(true);
        }
        else {
            Log.v("Shared preferences", "False - Push notification key " +
                    R.string.settings_push_notification_key);
            frequency_container.setVisibility(View.INVISIBLE);
            push_notification.setChecked(false);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder";
            String description = "Reminder to drink water";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Reminder", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void updateAlarm() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);

        // Uses AlarmManager to send out a push notification every X hour(s)
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent a_intent = new Intent(this, NotificationReceiver.class);
        a_intent.setAction("push_notification");

        int frequency_in_hours = sharedPreferences.getInt(
                "" + R.string.settings_notification_frequency_key, 1);
        alarmIntent = PendingIntent.getBroadcast(this, 0, a_intent, 0);
        // Milliseconds * seconds * minutes * hours
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                1000 * 60 * (60 * frequency_in_hours), alarmIntent);

        Log.v("Push notifications", "Interval in millis: " + 1000 * 60 * frequency_in_hours);

        // Will only send out push notification if user specified in Settings
        if (!sharedPreferences.getBoolean(
                "" + R.string.settings_push_notification_key, false)) {
            Log.v("Push notifications", "Cancelled - Push notification key returns false");
            if (alarmMgr != null) {
                Log.v("Push notifications", "Intent deleted");
                alarmMgr.cancel(alarmIntent);
            }
        }
        else {
            Log.v("Push notifications", "Success - Push notification key returns true");
        }
    }

}