package com.bleizing.parkirqyu.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.activities.KaryawanActivity;
import com.bleizing.parkirqyu.activities.KaryawanDetailActivity;
import com.bleizing.parkirqyu.activities.KaryawanFormActivity;
import com.bleizing.parkirqyu.activities.KendaraanActivity;
import com.bleizing.parkirqyu.models.Karyawan;

import java.util.ArrayList;

public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.ViewHolder> {

    private Context context;

    private ArrayList<Karyawan> karyawanArrayList;

    public KaryawanAdapter(Context context, ArrayList<Karyawan> karyawanArrayList) {
        this.context = context;
        this.karyawanArrayList = karyawanArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_karyawan, viewGroup, false);
        return new KaryawanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Karyawan karyawan = karyawanArrayList.get(i);

        viewHolder.tvNama.setText(karyawan.getNama());
        viewHolder.tvEmail.setText(karyawan.getEmail());

        viewHolder.llItemKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KaryawanDetailActivity.class);
                intent.putExtra("karyawan", karyawan);
                context.startActivity(intent);
                ((KaryawanActivity)context).finish();
            }
        });

        viewHolder.btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KaryawanFormActivity.class);
                intent.putExtra("karyawan", karyawan);
                context.startActivity(intent);
                ((KaryawanActivity)context).finish();
            }
        });

        viewHolder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.konfirmasi))
                    .setMessage(context.getString(R.string.konfirmasi_hapus))
                    .setPositiveButton(context.getString(R.string.konfirmasi_yakin), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((KaryawanActivity) context).hapusEmployee(karyawan.getUserId());
                        }
                    })
                    .setNegativeButton(context.getString(R.string.konfirmasi_batal), null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            }
        });

        viewHolder.btnLihatKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KendaraanActivity.class);
                intent.putExtra("karyawan", karyawan);
                context.startActivity(intent);
            }
        });
    }

    public void setKaryawanArrayList(ArrayList<Karyawan> karyawanArrayList) {
        this.karyawanArrayList = karyawanArrayList;
        notifyDataSetChanged();
    }

    public ArrayList<Karyawan> getKaryawanArrayList() {
        return karyawanArrayList;
    }

    @Override
    public int getItemCount() {
        return karyawanArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llItemKaryawan;

        private TextView tvNama;
        private TextView tvEmail;

        private Button btnLihatKendaraan;
        private Button btnUbah;
        private Button btnHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llItemKaryawan = (LinearLayout) itemView.findViewById(R.id.ll_item_karyawan);

            tvNama = (TextView) itemView.findViewById(R.id.tv_nama);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_email);

            btnLihatKendaraan = (Button) itemView.findViewById(R.id.btn_lihat_kendaraan);
            btnUbah = (Button) itemView.findViewById(R.id.btn_ubah);
            btnHapus = (Button) itemView.findViewById(R.id.btn_hapus);
        }
    }
}
