package ae.okhaz.boss.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ae.okhaz.boss.Activitys.LoginActivity;
import ae.okhaz.admin.R;


public class ResetPasswordActivity extends AppCompatActivity {

    Button btn_login_reset_password;
    ImageView iv_back_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_activtiy);

        btn_login_reset_password=findViewById(R.id.btn_login_reset_password);
        iv_back_forgot_password=findViewById(R.id.iv_back_reset_password);

        iv_back_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_login_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
            }
        });
    }
}