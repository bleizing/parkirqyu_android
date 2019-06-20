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
import com.bleizing.parkirqyu.activities.KaryawanFormActivity;
import com.bleizing.parkirqyu.activities.KendaraanActivity;
import com.bleizing.parkirqyu.activities.KendaraanDetailActivity;
import com.bleizing.parkirqyu.activities.KendaraanFormActivity;
import com.bleizing.parkirqyu.activities.MainActivity;
import com.bleizing.parkirqyu.models.Kendaraan;
import com.bleizing.parkirqyu.models.Model;

import java.util.ArrayList;

public class KendaraanAdapter extends RecyclerView.Adapter<KendaraanAdapter.ViewHolder> {

    private Context context;

    private ArrayList<Kendaraan> kendaraanArrayList;

    private int type;       // 1 = Own Kendaraan, 2 = Karyawan Kendaraan

    public KendaraanAdapter(Context context, ArrayList<Kendaraan> kendaraanArrayList, int type) {
        this.context = context;
        this.kendaraanArrayList = kendaraanArrayList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kendaraan, viewGroup, false);
        return new KendaraanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Kendaraan kendaraan = kendaraanArrayList.get(i);

        viewHolder.tvNomorRegistrasi.setText(kendaraan.getNomorRegistrasi());
        viewHolder.tvKendaraanType.setText(kendaraan.getVehicleType());
        viewHolder.tvKendaraanMerk.setText(kendaraan.getMerk());

        if (type == 1) {
            viewHolder.btnLihatBarcode.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            viewHolder.llItemKendaraanAdmin.setVisibility(View.VISIBLE);
        }

        viewHolder.llItemKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KendaraanDetailActivity.class);
                intent.putExtra("kendaraan", kendaraan);
                if (type == 2) {
                    intent.putExtra("karyawan", ((KendaraanActivity) context).karyawan);
                }
                context.startActivity(intent);
                ((KendaraanActivity)context).finish();
            }
        });

        viewHolder.btnLihatBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showBarcode(kendaraan.getNomorRegistrasi());
            }
        });

        viewHolder.btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KendaraanFormActivity.class);
                intent.putExtra("kendaraan", kendaraan);
                intent.putExtra("karyawan", ((KendaraanActivity) context).karyawan);
                context.startActivity(intent);
                ((KendaraanActivity)context).finish();
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
                                ((KendaraanActivity) context).hapusKendaraan(kendaraan.getKendaraanId());
                            }
                        })
                        .setNegativeButton(context.getString(R.string.konfirmasi_batal), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kendaraanArrayList.size();
    }

    public void updateKendaraanArrayList(ArrayList<Kendaraan> kendaraanArrayList) {
        this.kendaraanArrayList = kendaraanArrayList;
        notifyDataSetChanged();
    }

    public ArrayList<Kendaraan> getKendaraanArrayList() {
        return kendaraanArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNomorRegistrasi;
        private TextView tvKendaraanType;
        private TextView tvKendaraanMerk;

        private LinearLayout llItemKendaraan;
        private LinearLayout llItemKendaraanAdmin;

        private Button btnUbah;
        private Button btnHapus;

        private Button btnLihatBarcode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomorRegistrasi = (TextView) itemView.findViewById(R.id.tv_nomor_registrasi);
            tvKendaraanType = (TextView) itemView.findViewById(R.id.tv_vehicle_type);
            tvKendaraanMerk = (TextView) itemView.findViewById(R.id.tv_merk);

            btnLihatBarcode = (Button) itemView.findViewById(R.id.btn_lihat_barcode);

            llItemKendaraan = (LinearLayout) itemView.findViewById(R.id.ll_item_kendaraan);
            llItemKendaraanAdmin = (LinearLayout) itemView.findViewById(R.id.ll_item_kendaraan_admin);

            btnUbah = (Button) itemView.findViewById(R.id.btn_ubah);
            btnHapus = (Button) itemView.findViewById(R.id.btn_hapus);
        }
    }
}
