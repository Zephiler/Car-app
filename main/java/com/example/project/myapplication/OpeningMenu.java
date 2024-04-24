package com.example.project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OpeningMenu extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.opening_page);

            Thread thread = new Thread()
            {
                public void run()
                {
                    try
                    {
                        sleep(2000);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(OpeningMenu.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                    finally
                    {
                        Intent startApp = new Intent(OpeningMenu.this, StartPage.class);
                        startActivity(startApp);
                    }
                }

            };
            thread.start();
        }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
