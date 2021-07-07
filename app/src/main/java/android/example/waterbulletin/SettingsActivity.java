package android.example.waterbulletin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new UserPreferenceFragment())
                .commit();

    }

    public static class UserPreferenceFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_preference, rootKey);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }
}