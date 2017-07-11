package com.georgepark.projectctc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// NOT USED ANYMORE, REPLACED BY SCAN BUTTON

public class CreateOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        Button createButton = (Button) findViewById(R.id.createOrder);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText clientName = (EditText) findViewById(R.id.clientName);
                EditText orderID = (EditText) findViewById(R.id.id);
                Context context = getApplicationContext();
                String myOrder = orderID.getText().toString();
                String myClient = clientName.getText().toString();

                if (!myClient.isEmpty() && !myOrder.isEmpty()) {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("orders").child(myOrder).child("status").setValue(myClient);
                    Toast.makeText(context, "Order created!", Toast.LENGTH_SHORT).show();
                    orderID.setText("");
                    clientName.setText("");
                }
                else {
                    Toast.makeText(context, "Invalid name or id", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
