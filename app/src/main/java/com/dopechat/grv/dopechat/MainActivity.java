package com.dopechat.grv.dopechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    FirebaseUser current_user;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_postcard:
                    //Make fullscreen>>>>START
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    //Make fullscreen>>>>END

                    PostcardFragment postcardFragment = new PostcardFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment_container, postcardFragment, "Postcard");
                    fragmentTransaction2.commit();

                    return true;
                case R.id.navigation_chat:
                    //Exit fullscreen>>>>START
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    //Exit fullscreen>>>>END


                    ChatFragment chatFragment = new ChatFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.fragment_container,chatFragment, "Chat");
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_notifications:
                    //Exit fullscreen>>>>START
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    //Exit fullscreen>>>>END

                    NotificationFragment notificationFragment = new NotificationFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment_container,notificationFragment, "Notification");
                    fragmentTransaction3.commit();

                    return true;
                case R.id.navigation_profile:
                    //Exit fullscreen>>>>START
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    //Exit fullscreen>>>>END

                    ProfileFragment profileFragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.fragment_container,profileFragment, "Profile");
                    fragmentTransaction4.commit();

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);//This line removes the icon animation from bottom bar
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();

        //check if user is signed in or not>>>>START
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            //startActivity(new Intent(MainActivity.this,StartActivity.class));
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }else if(currentUser != null ){

            current_user = FirebaseAuth.getInstance().getCurrentUser();
            //uid = current_user.getUid();

            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user.getUid());
            mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild("name")){
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        Toast.makeText(MainActivity.this, "Complete profile first", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        //check if user is signed in or not>>>>END

        //startActivity(new Intent(MainActivity.this, RegisterActivity.class));

    }

}
