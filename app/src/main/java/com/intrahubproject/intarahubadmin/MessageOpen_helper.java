package com.intrahubproject.intarahubadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MessageOpen_helper extends BottomSheetDialogFragment {

    private EditText messageinput;
    private RelativeLayout messagebutton;
    private DatabaseReference MmessageDatabase;
    private String CurrentTime, CurrentDate;

    private ProgressBar Mprogress;
   private int negativeValShort = 0;
   private int ShortMessage;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View Mview = inflater.inflate(R.layout.message_templete, container, false);



        Mprogress = Mview.findViewById(R.id.MessageProgressbarID);
        Mprogress.setVisibility(View.INVISIBLE);
        MmessageDatabase = FirebaseDatabase.getInstance().getReference().child("AdminMessage");
        MmessageDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    ShortMessage = (int) dataSnapshot.getChildrenCount();
                    negativeValShort = (~(ShortMessage - 1));
                }
                else {
                    negativeValShort = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageinput = Mview.findViewById(R.id.MessagePanelID);
        messagebutton = Mview.findViewById(R.id.MessageButtonID);

        messagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Messageinputtext = messageinput.getText().toString();
                if(Messageinputtext.isEmpty()){
                    messageinput.setError("Message require");
                }
                else {

                    Mprogress.setVisibility(View.VISIBLE);
                    Calendar calendartime = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat_time = new SimpleDateFormat("hh:mm a");
                    CurrentTime = simpleDateFormat_time.format(calendartime.getTime());

                    Calendar calendar_date = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat_date = new SimpleDateFormat("dd-MM-yyyy");
                    CurrentDate = simpleDateFormat_date.format(calendar_date.getTime());



                    Map<String, Object> messagemap = new HashMap<String, Object>();
                    messagemap.put("AdminMessage", Messageinputtext);
                    messagemap.put("short_data", negativeValShort);
                    messagemap.put("current_time", CurrentTime);
                    messagemap.put("current_date", CurrentDate);




                    MmessageDatabase.push().updateChildren(messagemap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();
                                        Mprogress.setVisibility(View.INVISIBLE);
                                    }
                                    else {
                                        Mprogress.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Mprogress.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            });


                    messageinput.setText("");
                }
            }
        });

        return Mview;
    }
}
