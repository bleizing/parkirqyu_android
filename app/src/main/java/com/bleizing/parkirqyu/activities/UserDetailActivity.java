package com.bleizing.parkirqyu.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;

public class UserDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvNama = (TextView) findViewById(R.id.tv_nama);
        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        TextView tvJenisKelamin = (TextView) findViewById(R.id.tv_jenis_kelamin);
        TextView tvAlamat = (TextView) findViewById(R.id.tv_alamat);
        TextView tvTempatLahir = (TextView) findViewById(R.id.tv_tempat_lahir);
        TextView tvTanggalLahir = (TextView) findViewById(R.id.tv_tanggal_lahir);
        TextView tvSebagai = (TextView) findViewById(R.id.tv_sebagai);

        Button btnUbahPassword = (Button) findViewById(R.id.btn_ubah_password);
        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailActivity.this, UbahPasswordActivity.class);
                startActivity(intent);
            }
        });

        String userType = "Admin";

        if (Model.getUser().getUserType() == 2) {
            userType = "Petugas Parkir";
        } else if (Model.getUser().getUserType() == 3) {
            userType = "Karyawan";
        }

        tvNama.setText(Model.getUser().getNama());
        tvEmail.setText(Model.getUser().getEmail());
        tvJenisKelamin.setText(Model.getUser().getJenisKelamin());
        tvAlamat.setText(Model.getUser().getAlamat());
        tvTempatLahir.setText(Model.getUser().getTempatLahir());
        tvTanggalLahir.setText(Model.getUser().getTanggalLahir());
        tvSebagai.setText(userType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
