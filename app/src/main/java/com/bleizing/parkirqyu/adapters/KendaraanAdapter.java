package com.bleizing.parkirqyu.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Kendaraan;

import java.util.ArrayList;

public class KendaraanAdapter extends RecyclerView.Adapter<KendaraanAdapter.ViewHolder> {

    private Context context;

    private ArrayList<Kendaraan> kendaraanArrayList;

    public KendaraanAdapter(Context context, ArrayList<Kendaraan> kendaraanArrayList) {
        this.context = context;
        this.kendaraanArrayList = kendaraanArrayList;
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

        viewHolder.btnLihatBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Lihat Barcode " + kendaraan.getNomorRegistrasi(), Toast.LENGTH_LONG).show();
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

        private Button btnLihatBarcode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomorRegistrasi = (TextView) itemView.findViewById(R.id.tv_nomor_registrasi);
            tvKendaraanType = (TextView) itemView.findViewById(R.id.tv_vehicle_type);
            tvKendaraanMerk = (TextView) itemView.findViewById(R.id.tv_merk);

            btnLihatBarcode = (Button) itemView.findViewById(R.id.btn_lihat_barcode);
        }
    }
}
