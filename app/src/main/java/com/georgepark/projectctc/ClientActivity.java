package com.georgepark.projectctc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ClientActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
    }

    //scan button onClick
    public void scanOnClick(View view) {
        TextView tv = (TextView) findViewById(R.id.step);
        tv.setText("");
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mScannerView != null){
            mScannerView.stopCamera();
            Intent intent = new Intent(this, ClientActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void handleResult(Result result) {

        final Result res = result;
        final Context context = getApplicationContext();

        ref.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean boolTemp = false;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.getKey().equals(res.getText())) {
                        boolTemp = true;
                        String temp = childSnapshot.child("status").getValue().toString();

                        // I no longer want to display it in the 'Step: ' text or a toast
                        Toast.makeText(context, "Step: " + temp, Toast.LENGTH_SHORT).show();

                        // I want to use an alert, but not sure if this works yet
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Progress");
                        builder.setMessage("Your order is in step: " + temp);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                }

                if(!boolTemp) {
                    Toast.makeText(context, "Unable to find order!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
