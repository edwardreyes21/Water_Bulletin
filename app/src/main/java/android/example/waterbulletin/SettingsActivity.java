package android.example.waterbulletin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);

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

        Button reset_intake = (Button) findViewById(R.id.reset_intake);
        reset_intake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                intent.putExtra("resetIntake", 1);
                startActivity(intent);
            }
        });

        int min_intake = sharedPreferences.getInt("" + R.string.settings_min_intake_key, 2000);

        SeekBar min_intake_seekbar = (SeekBar) findViewById(R.id.min_intake_seekbar);
        min_intake_seekbar.setProgress(min_intake);
        Log.v("setProgress", "Min intake: " + min_intake);

        TextView seekbar_progress = (TextView) findViewById(R.id.min_intake_seekbar_progress);
        seekbar_progress.setText("" + min_intake);

        int stepSize = 4;
        min_intake_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor min_intake_editor = sharedPreferences.edit();

                progress = (int) Math.round((progress + 99) / 100) * 100;
                Log.v("setProgress", "Seekbar progress: " + progress);

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

    }

}