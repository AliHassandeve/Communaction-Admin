package com.intrahubproject.intarahubadmin;

import android.content.Intent;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterOpen_helper extends BottomSheetDialogFragment {

    private FirebaseAuth Mauth;
    private TextInputLayout email, password;
    private RelativeLayout registerbutton;
    private ProgressBar Mprogress;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View Mview = inflater.inflate(R.layout.register_template, container, false);

        Mprogress = Mview.findViewById(R.id.ProgressbarID);
        Mprogress.setVisibility(View.INVISIBLE);
        Mauth = FirebaseAuth.getInstance();
        email = Mview.findViewById(R.id.EmailID);
        password = Mview.findViewById(R.id.PasswordEID);
        registerbutton = Mview.findViewById(R.id.registerButtonIDs);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailget = email.getEditText().getText().toString();
                final String passwordget = password.getEditText().getText().toString();

                if (emailget.isEmpty()) {
                    email.setError("Email require");
                } else if (passwordget.isEmpty()) {
                    password.setError("Password require");
                } else {

                    Mprogress.setVisibility(View.VISIBLE);

                    Mauth.createUserWithEmailAndPassword(emailget, passwordget)

                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Mprogress.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getContext(), "Account is creating success", Toast.LENGTH_LONG).show();


                                        String subject = R.string.subject + "\n\n";
                                        String message = R.string.message + "\n" + emailget + "\n" + "Your password is: " + passwordget;

                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_EMAIL, "");
                                        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                                        intent.putExtra(Intent.EXTRA_TEXT, message);

                                        intent.setType("message/rfc822");
                                        startActivity(Intent.createChooser(intent, "open your email clint"));

                                    } else {
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

                }

            }
        });

        return Mview;
    }


}
