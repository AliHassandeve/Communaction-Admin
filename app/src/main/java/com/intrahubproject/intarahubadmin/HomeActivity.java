package com.intrahubproject.intarahubadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference MuserDatabase;
    private FirebaseAuth Mauth;
    private RecyclerView recyclerView;
    private RelativeLayout message, register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        message  =findViewById(R.id.MessageID);
        register = findViewById(R.id.RegisterButtonID);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageOpen_helper messageOpen_helper = new MessageOpen_helper();
                messageOpen_helper.show(getSupportFragmentManager(), "open");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    RegisterOpen_helper registerOpen_helper = new RegisterOpen_helper();
                    registerOpen_helper.show(getSupportFragmentManager(), "open");
            }
        });

        Mauth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.HomeToolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = findViewById(R.id.MainViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        cheackuserloginornot();
    }

    @Override
    protected void onStart() {

        FirebaseRecyclerAdapter<homeholder, HomeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<homeholder, HomeViewHolder>(
                homeholder.class,
                R.layout.userlayout,
                HomeViewHolder.class,
                MuserDatabase
        ) {
            @Override
            protected void populateViewHolder(final HomeViewHolder homeViewHolder, final homeholder homeholder, int i) {

                String UID = getRef(i).getKey();
                MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("username")){
                                String usernameget = dataSnapshot.child("username").getValue().toString();
                                homeViewHolder.setUsernameset(usernameget);
                            }
                            if(dataSnapshot.hasChild("imageuri")){
                                String imageuriget = dataSnapshot.child("imageuri").getValue().toString();
                                homeViewHolder.setProfileimageset(imageuriget);
                            }
                            if(dataSnapshot.hasChild("adminstatas")){
                                String isonlineoroffline = dataSnapshot.child("adminstatas").getValue().toString();
                                if(isonlineoroffline.equals("online")){
                                    homeViewHolder.setonlnedata("online");
                                    homeViewHolder.onlineoroffline.setTextColor(getResources().getColor(R.color.red));
                                }
                                else if(isonlineoroffline.equals("offline")){
                                    homeViewHolder.setonlnedata("offline");
                                    homeViewHolder.onlineoroffline.setTextColor(getResources().getColor(R.color.transparent));

                                }
                            }





                        }
                        else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileimage;
        private MaterialTextView username, onlineoroffline;
        private Context context;
        private View Mview;

        private MaterialTextView delatetext;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();
            delatetext = Mview.findViewById(R.id.DelateButtonID);
            username = Mview.findViewById(R.id.SusernameID);
            onlineoroffline = Mview.findViewById(R.id.OnlineStatsID);
            profileimage = Mview.findViewById(R.id.SprofileImageID);
        }

        public void setProfileimageset(String uri){
            Picasso.with(context).load(uri).placeholder(R.drawable.defaultimage).into(profileimage);
        }

        public void setUsernameset(String nam){
            username.setText(nam);
        }

        public void setonlnedata(String isonline){
            onlineoroffline.setText(isonline);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.loginmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.LogoutID){
            Mauth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void cheackuserloginornot(){
        FirebaseUser Muser = Mauth.getCurrentUser();
        if(Muser == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
