package ae.okhaz.boss.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ae.okhaz.boss.Activitys.LoginActivity;
import ae.okhaz.admin.R;


public class ForgotPasswordActivity extends AppCompatActivity {

    Button btn_send;
    TextView tv_login_forgot_password;
    ImageView iv_back_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btn_send=findViewById(R.id.btn_send);
        tv_login_forgot_password=findViewById(R.id.tv_login_forgot_password);
        iv_back_forgot_password=findViewById(R.id.iv_back_forgot_password);

        iv_back_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class));
            }
        });

        tv_login_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });


    }
}