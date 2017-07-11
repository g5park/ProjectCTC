package com.georgepark.projectctc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //temporary username and password for admin
    final private String adminUser = "1";
    final private String adminPass = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button admin = (Button) findViewById(R.id.adminButton);
        Button client = (Button) findViewById(R.id.clientButton);
        final Context context = getApplicationContext();
        final Button loginButton = (Button) findViewById(R.id.login);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(new AsteriskPasswordTransformation());

        // onClick for admin button
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);

            }
        });

        // onClick for login button to go to next activity
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().equals(adminUser) && password.getText().toString().equals(adminPass)) {
                    username.setText("");
                    password.setText("");
                    username.setVisibility(View.INVISIBLE);
                    password.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                    launchAdminActivity();
                }
                else {
                    Toast.makeText(context, "Wrong username and password!", Toast.LENGTH_LONG).show();
                }
            }
        });


        // onClick for client button
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchClientActivity();
            }
        });

    }

    // launch client activity
    public void launchClientActivity() {
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }

    // launch admin activity
    public void launchAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

}
