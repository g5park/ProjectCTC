package com.georgepark.projectctc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        final EditText eFind = (EditText) findViewById(R.id.findOrderE);
        final EditText eOrder = (EditText) findViewById(R.id.editOrderE);
        final EditText eName = (EditText) findViewById(R.id.editNameE);
        final Context context = getApplicationContext();
        Button fButton = (Button) findViewById(R.id.findButtonE);
        Button sButton = (Button) findViewById(R.id.saveButton);


        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String myOrder = eFind.getText().toString();
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                if (!myOrder.isEmpty()) {

                    ref.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // Currently deletes user from the database when "SEARCH" is clicked on
                            // then re-creates a 'new' user after editing it
                            // NEED TO FIND BETTER SOLUTION

                            for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                                if (childSnapShot.getKey().equals(myOrder)) {
                                    String temp = childSnapShot.child("status").getValue().toString();
                                    eOrder.setText(myOrder);
                                    eName.setText(temp);

                                    ref.child("orders").child(temp).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                else {
                    Toast.makeText(context, "Invalid input.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Saves changes
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();

                String mOrder = eOrder.getText().toString();
                String mStatus = eName.getText().toString();

                if (!mOrder.isEmpty() && !mStatus.isEmpty()) {
                    ref2.child("orders").child(mOrder).child("status").setValue(mStatus);
                    eFind.setText("");
                    eOrder.setText("");
                    eName.setText("");
                    Toast.makeText(context, "Changes saved.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Invalid input.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
