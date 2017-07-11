package com.georgepark.projectctc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AdminActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    private ZXingScannerView mScannerView;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button deleteButton = (Button) findViewById(R.id.df);
        Button editButton = (Button) findViewById(R.id.edit);

        // onClick for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDeleteOrder();
            }
        });

        // onClick for edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEditOrder();
            }
        });

    }

    // onClick for scan button
    public void scanOnClick(View view) {
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    // turns off camera and returns to previous activity
    @Override
    protected void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        }
    }

    // Result result is the variable that holds the value from the scan
    // Here, we update the status of each order scanned
    @Override
    public void handleResult(Result result) {

        Handler handler = new Handler();

        updateProgress(result);

        Toast.makeText(this, "Order " + result.getText() + " updated!", Toast.LENGTH_SHORT).show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                helperResume();
            }
        }, 2000);
    }

    // resumes camera for scanning
    public void helperResume() {
        mScannerView.resumeCameraPreview(this);
    }

    // called by handleResult, which holds the logic behind updating the order's id in the database
    public void updateProgress(Result result) {

        final Result res = result;
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean temp = false;
                String status = "";

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.getKey().equals(res.getText())) {
                        temp = true;
                        status = childSnapshot.child("status").getValue().toString();
                        break;
                    }
                }
                if (!temp) {
                    ref.child("orders").child(res.getText()).child("status").setValue("one");
                }

                else {

                    Context context = getApplicationContext();

                    switch(status) {
                        case "one":
                            // update it to the next step
                            ref.child("orders").child(res.getText()).child("status").setValue("two");
                            Toast.makeText(context, "Updated to step two!", Toast.LENGTH_SHORT).show();
                            break;
                        case "two":
                            ref.child("orders").child(res.getText()).child("status").setValue("three");
                            Toast.makeText(context, "Updated to step three!", Toast.LENGTH_SHORT).show();
                            break;
                        case "three":
                            ref.child("orders").child(res.getText()).child("status").setValue("final");
                            Toast.makeText(context, "Updated to final step!", Toast.LENGTH_SHORT).show();
                            break;
                        case "final":
                            ref.child("orders").child(res.getText()).child("status").setValue("ready");
                            Toast.makeText(context, "Updated to Ready for pick up!", Toast.LENGTH_SHORT).show();
                            break;
                        case "ready":
                            Toast.makeText(context, "This order is ready for pick up!", Toast.LENGTH_SHORT).show();
                        default:
                            Toast.makeText(context, "Unable to check the status", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void launchDeleteOrder() {
        Intent intent = new Intent(this, DeleteFindOrder.class);
        startActivity(intent);
    }

    public void launchEditOrder() {
        Intent intent = new Intent(this, EditOrder.class);
        startActivity(intent);
    }
}
