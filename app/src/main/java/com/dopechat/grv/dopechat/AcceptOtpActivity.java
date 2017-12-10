package com.dopechat.grv.dopechat;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lamudi.phonefield.PhoneInputLayout;
import java.util.concurrent.TimeUnit;
import es.dmoral.toasty.Toasty;


public class AcceptOtpActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mDesc;
    private Button btnSendotp;
    private PhoneInputLayout phoneInputLayout;
    private Button btnSubmitotp;
    private EditText otpInputLayout;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptotp);

        Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/gotham_bold.ttf");
        Typeface font_medium = Typeface.createFromAsset(getAssets(),"fonts/gotham_medium.ttf");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        /*String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameCheck = dataSnapshot.child("name").getValue().toString();
                Toast.makeText(AcceptOtpActivity.this, "Hulalalal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setTypeface(font_bold);
        mDesc = (TextView) findViewById(R.id.subdesc);
        mDesc.setTypeface(font_medium);
        phoneInputLayout = (PhoneInputLayout) findViewById(R.id.phone_input_layout);
        otpInputLayout = (EditText) findViewById(R.id.otp_input_layout);
        phoneInputLayout.setHint(R.string.phone_number_input_hint);
        phoneInputLayout.setDefaultCountry("IN");
        btnSendotp = (Button) findViewById(R.id.button_sendotp);
        btnSendotp.setTypeface(font_medium);
        btnSubmitotp = (Button) findViewById(R.id.button_submitotp);
        btnSubmitotp.setTypeface(font_medium);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:" + credential);

                Toast toast2 = Toasty.success(AcceptOtpActivity.this, "Auto Verified", Toast.LENGTH_LONG, true);
                toast2.setGravity(Gravity.TOP,0,100);
                toast2.show();
                signInWithPhoneAuthCredential(credential);
                //startActivity(new Intent(AcceptOtpActivity.this, MainActivity.class));
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast toast = Toasty.error(AcceptOtpActivity.this, "Verification failed", Toast.LENGTH_LONG, true);
                toast.setGravity(Gravity.TOP,0,100);
                toast.show();
                //Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast toast1 = Toasty.error(AcceptOtpActivity.this, "Invalid mobile number", Toast.LENGTH_LONG, true);
                    toast1.setGravity(Gravity.TOP,0,100);
                    toast1.show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast toast3 = Toasty.error(AcceptOtpActivity.this, "OTP quota exceeded", Toast.LENGTH_LONG, true);
                    toast3.setGravity(Gravity.TOP,0,100);
                    toast3.show();
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);
                Toast toast6 = Toasty.info(AcceptOtpActivity.this, "OTP sent", Toast.LENGTH_LONG, true);
                toast6.setGravity(Gravity.TOP,0,100);
                toast6.show();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                phoneInputLayout.setVisibility(View.GONE);
                btnSendotp.setVisibility(View.GONE);
                mTitle.setText("OTP sent!");
                mDesc.setText("Enter the OTP received on your mobile number");
                otpInputLayout.setVisibility(View.VISIBLE);
                btnSubmitotp.setVisibility(View.VISIBLE);


                // ...
            }
        };

        btnSendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(phoneInputLayout.getPhoneNumber().toString().trim().length() < 6){
                    Toast toast = Toasty.error(AcceptOtpActivity.this, "Enter a valid mobile number", Toast.LENGTH_LONG, true);
                    toast.setGravity(Gravity.TOP,0,100);
                    toast.show();
                }else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneInputLayout.getPhoneNumber().toString(),        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            AcceptOtpActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });

        btnSubmitotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpInputLayout.getText().toString().trim().length() < 1){
                    Toast toast7 = Toasty.error(AcceptOtpActivity.this, "Enter OTP", Toast.LENGTH_LONG, true);
                    toast7.setGravity(Gravity.TOP,0,100);
                    toast7.show();
                }else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpInputLayout.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            Toast toast4 = Toasty.success(AcceptOtpActivity.this, "Successfully verified", Toast.LENGTH_LONG, true);
                            toast4.setGravity(Gravity.TOP,0,100);
                            toast4.show();

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user.getUid());
                            mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("name")){
                                        startActivity(new Intent(AcceptOtpActivity.this, MainActivity.class));
                                        finish();
                                    }else{
                                        startActivity(new Intent(AcceptOtpActivity.this, RegisterActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            //finish();
                            /*FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(AcceptOtpActivity.this, MainActivity.class));
                            finish();*/

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast toast5 = Toasty.error(AcceptOtpActivity.this, "Code doesn't match", Toast.LENGTH_LONG, true);
                            toast5.setGravity(Gravity.TOP,0,100);
                            toast5.show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}