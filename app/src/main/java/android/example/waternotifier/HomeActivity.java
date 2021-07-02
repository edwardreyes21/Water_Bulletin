package android.example.waternotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    // How much water (in milliliters) the user has drank today
    public int current_intake = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

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

                current_intake += 500;
                current.setText("" + current_intake);

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
                    Toast.makeText(HomeActivity.this, "Congratulations! You've hit " +
                            "your minimum limit for the day..", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}