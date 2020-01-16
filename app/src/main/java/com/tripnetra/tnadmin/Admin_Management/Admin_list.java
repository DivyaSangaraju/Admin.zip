package com.tripnetra.tnadmin.Admin_Management;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.tripnetra.tnadmin.R;

import java.util.Objects;

public class Admin_list extends AppCompatActivity {

    Button admninfo,deptinfo;
    TextInputLayout fn,ln,email,pwd,cpwd,mno;
    String act;
    Boolean aa=false;
    Switch astatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        deptinfo = findViewById(R.id.deptinfoBtn);
        admninfo = findViewById(R.id.admininfoBtn);
        fn=findViewById(R.id.FnameTIL);
        ln=findViewById(R.id.LnameTIL);
        email=findViewById(R.id.emailTIL);
        pwd=findViewById(R.id.passwdTIL);
        cpwd=findViewById(R.id.cpwdTIL);
        mno=findViewById(R.id.MnoTIL);
        astatus=findViewById(R.id.astatusNS);
        astatus.setOnCheckedChangeListener((compoundButton, on) -> {
            if(on){
                 aa=true;
                act ="INACTIVE";
                astatus.setText(act);
            } else {
                aa=false;
                act="ACTIVE";
                astatus.setText(act);
            }

        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deptinfo(View view) {

        final String  Fname = ((EditText)findViewById(R.id.fnameET)).getText().toString().trim(),
                    Lname = ((EditText)findViewById(R.id.LnameET)).getText().toString().trim(),
                    Email = ((EditText)findViewById(R.id.emailET)).getText().toString().trim(),
                    Pass = ((EditText)findViewById(R.id.passET)).getText().toString().trim(),
                    cPass = ((EditText)findViewById(R.id.cpwdET)).getText().toString().trim(),
                    Phone = ((EditText)findViewById(R.id.MnoET)).getText().toString().trim();


        fn.setErrorEnabled(false);ln.setErrorEnabled(false);email.setErrorEnabled(false);
        mno.setErrorEnabled(false);pwd.setErrorEnabled(false);

        if(Objects.equals(Fname, "")){
            fn.setError("Enter First Name");
        }else if(Objects.equals(Lname, "")){
            ln.setError("Enter Last Name");
        }else if(Objects.equals(Phone, "") || !Phone.matches("[6-9][0-9]{9}")){
            mno.setError((Objects.equals(Phone, ""))?"Enter Mobile Number" : "Enter Valid Mobile Number");
        }else if(Objects.equals(Email, "") || !android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError((Objects.equals(Email, ""))?"Enter Email":"Enter Valid Email");
        }else if(Objects.equals(Pass, "") || Pass.length()<8){
            pwd.setError((Objects.equals(Pass, ""))?"Enter Password":"Enter Password more than 8 Characters");
        }else if(Objects.equals(cPass, "")) {
            cpwd.setError((Objects.equals(cPass, ""))?"Enter Password":"Enter Password more than 8 Characters");
        }else if(act == null){
            Toast.makeText(this, "Choose Status", Toast.LENGTH_SHORT).show();
        }
        else{

            Intent intent = new Intent(this,Dept_info_Activity.class);

            Bundle bundle = new Bundle();
            bundle.putString("firstname",Fname);
            bundle.putString("lastname",Lname);
            bundle.putString("email",Email);
            bundle.putString("password",Pass);
            bundle.putString("mobileno",Phone);
            bundle.putString("status",act);
            intent.putExtras(bundle);
            Admin_list.this.finish();
            startActivity(intent);
        }
    }
}
