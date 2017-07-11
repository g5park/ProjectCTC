package com.georgepark.projectctc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteFindOrder extends AppCompatActivity {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_find_order);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        Button searchButton = (Button) findViewById(R.id.searchButton);

        // delete button onclick
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText orderid = (EditText) findViewById(R.id.order);
                final String myOrder = orderid.getText().toString();
                final Context context = getApplicationContext();
                final TextView textView = (TextView) findViewById(R.id.client);

                if (!myOrder.isEmpty()) {

                    ref.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                                if (childSnapShot.getKey().equals(myOrder)) {
                                    ref.child("orders").child(childSnapShot.getKey()).removeValue();
                                    orderid.setText("");
                                    textView.setText("");

                                    Toast.makeText(context, "Order has been deleted.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                else {
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //search button onclick
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText orderid = (EditText) findViewById(R.id.order);
                final String myOrder = orderid.getText().toString();
                final Context context = getApplicationContext();

                if (!myOrder.isEmpty()) {
                    ref.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean boolTemp = false;
                            for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                                if (childSnapShot.getKey().equals(myOrder)) {
                                    boolTemp = true;
                                    String temp = childSnapShot.child("status").getValue().toString();
                                    TextView textView = (TextView) findViewById(R.id.client);
                                    textView.setText(temp);
                                }
                            }

                            if (!boolTemp) {
                                Toast.makeText(context, "Order could not be found!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
