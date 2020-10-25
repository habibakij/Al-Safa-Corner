package com.nayeemakij.bajitpuronlineshop.UserPanel.Registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nayeemakij.bajitpuronlineshop.R;
import com.nayeemakij.bajitpuronlineshop.AdminPanel.AdminDashboard;
import com.nayeemakij.bajitpuronlineshop.UserPanel.MainActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String verificationCodebySystem;
    String getPhone;
    ProgressBar progressBar;
    Button btnVerification;
    EditText get_edit_code;
    String adminPhone="01717722647";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mAuth= FirebaseAuth.getInstance();

        progressBar= findViewById(R.id.verify_progress_dialog);
        btnVerification= findViewById(R.id.btn_verification);
        get_edit_code= findViewById(R.id.edt_verification_code);

        SharedPreferences sharedPreferences = getSharedPreferences("STORE_REGISTRATION_DATA", Context.MODE_PRIVATE);
        getPhone= sharedPreferences.getString("phoneNumber","defalut");
        Log.i("VerifyphoneNumber",getPhone);

        sendVerificationCode(getPhone);
        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code= get_edit_code.getText().toString();
                if (code.isEmpty()){
                    get_edit_code.setError("Invalid OTP...");
                    get_edit_code.requestFocus();
                    return;
                } else {
                    Verify(code);
                }
            }
        });
    }

    private void sendVerificationCode(String getPhone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+880"+getPhone, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                callbacks); // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodebySystem= s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code= phoneAuthCredential.getSmsCode();
            if (code!= null){
                progressBar.setVisibility(View.VISIBLE);
                Verify(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this, "Verification_failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i( "Verification_failed: ", Objects.requireNonNull(e.getMessage()));
        }
    };

    private void Verify(String code){
        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(verificationCodebySystem, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (getPhone.equals(adminPhone)){
                                Tost("Admin Panel");
                                Intent intent= new Intent(VerificationActivity.this, AdminDashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Tost("User Panel");
                                Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Tost("verification code invalid");
                                Log.i("invalid_verification", task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    private void Tost(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

}
