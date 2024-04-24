package com.example.project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_page);

        final Button buttonOne = (Button) findViewById(R.id.buttonStartCalculate);
        final Button buttonTwo = (Button) findViewById(R.id.buttonCredits);
        final Button buttonFour = (Button) findViewById(R.id.buttonExit);

        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this, Credits.class);
                startActivity(intent);
            }
        });

        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finishAffinity();
                System.exit(0);
                // finish();
            }
        });
    }
}
