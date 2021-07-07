package android.example.waternotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    // How much water (in milliliters) the user has drank today
    public int current_intake = 0;

    public void updateCurrentIntake() {
        // Accesses the SharedPreferences key of current intake and resets it to 0
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        // Resets the progress text to 0ml
        TextView current = findViewById(R.id.current_intake);
        current.setText("" + sharedPreferences.getInt("" + R.string.saved_current_intake, 0));
    }

    public void resetIntake() {
        // Accesses the SharedPreferences key of current intake and resets it to 0
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor saved_current_intake_editor = sharedPreferences.edit();
        saved_current_intake_editor.putInt("" + R.string.saved_current_intake, 0);
        saved_current_intake_editor.apply();

       // Resets the progress text to 0ml
        TextView current = findViewById(R.id.current_intake);
        current.setText("" + sharedPreferences.getInt("" + R.string.saved_current_intake, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        // Every time the activity is created, update the current intake so that it shows
        // the proper value
        updateCurrentIntake();

        // Catches an intent specifically designed for resetting the intake
        Intent intent = getIntent();
        int data = intent.getIntExtra("resetIntake", 0);
        if (data == 1) {
            resetIntake();
        }

        // If user taps on 'Alerts', send them to 'Alerts' page
        TextView alerts = findViewById(R.id.alerts);
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AlertsActivity.class);
                startActivity(intent);
            }
        });

        // If user taps on 'Settings', send them to 'Settings' page
        TextView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Handles the user pressing the 'Increase' button
        Button increase_intake = (Button) findViewById(R.id.increase_intake);
        increase_intake.setOnClickListener(new View.OnClickListener() {
            @Override
            // The cup of water represents the minimum intake per day
            // Assume that for now, the minimum intake is 2000 ml
            // We'll change the minimum later, as well as add an algorithm to calculate it
            public void onClick(View v) {
                ImageView cup_of_water = findViewById(R.id.bottle_fullness);
                TextView current = findViewById(R.id.current_intake);

                SharedPreferences max_intake_preference = getSharedPreferences(
                        getString(R.string.settings_max_intake_key), MODE_PRIVATE);

                current_intake = sharedPreferences.getInt(
                        "" + R.string.saved_current_intake, 0);
                current_intake += 500;
                current.setText("" + current_intake);

                // Reads the max intake key using Shared Preferences

                int default_max_intake = getResources().getInteger(
                        R.integer.settings_max_intake_default);
                int max_intake = max_intake_preference.getInt(getString(
                        R.string.settings_max_intake_key), default_max_intake);

                // Writes the newly updated current intake to saved current intake key

                SharedPreferences.Editor current_intake_editor = sharedPreferences.edit();
                current_intake_editor.putInt("" + R.string.saved_current_intake, current_intake);
                current_intake_editor.apply();

                // Shows a warning when the user has drank more than their maximum intake
                // The toast is temporary, we'll show a more threatening alert later
                if (current_intake >= max_intake) {
                    Toast.makeText(HomeActivity.this,
                            "WARNING: You have consumed more than your daily maximum intake!",
                            Toast.LENGTH_LONG).show();
                }

                // Updates the image based on current intake
                // Later on, we'll update it so that it updates based on current and maximum intakes
                if (current_intake == 500) {
                    cup_of_water.setImageResource(R.drawable.cupofwater_100percent);
                }
                else if (current_intake == 1000) {
                    cup_of_water.setImageResource(R.drawable.cupofwater_66percent);
                }
                else if (current_intake == 1500) {
                    cup_of_water.setImageResource(R.drawable.cupofwater_33percent);
                }
                else if (current_intake == 2000) {
                    cup_of_water.setImageResource(R.drawable.cupofwater_0percent);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}