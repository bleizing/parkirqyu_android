package com.bleizing.parkirqyu.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.activities.MainActivity;
import com.bleizing.parkirqyu.models.Kendaraan;
import com.bleizing.parkirqyu.models.KendaraanPakir;

import java.util.ArrayList;

public class KendaraanParkirAdapter extends RecyclerView.Adapter<KendaraanParkirAdapter.ViewHolder> {

    private Context context;

    private ArrayList<KendaraanPakir> kendaraanPakirArrayList;

    private boolean isAllData;

    public KendaraanParkirAdapter(Context context, ArrayList<KendaraanPakir> kendaraanPakirArrayList, boolean isAllData) {
        this.context = context;
        this.kendaraanPakirArrayList = kendaraanPakirArrayList;
        this.isAllData = isAllData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kendaraan_parkir, viewGroup, false);
        return new KendaraanParkirAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final KendaraanPakir kendaraanPakir = kendaraanPakirArrayList.get(i);

        viewHolder.tvNomorRegistrasi.setText(kendaraanPakir.getNomorRegistrasi());
        viewHolder.tvInfoJam.setText(kendaraanPakir.getInfoParkir());
        viewHolder.tvNominal.setText(kendaraanPakir.getNominal());

        if (!isAllData) {
            viewHolder.btnSelesai.setVisibility(View.VISIBLE);
            viewHolder.btnSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).showBarcode(kendaraanPakir.getNomorRegistrasi());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return kendaraanPakirArrayList.size();
    }

    public void updateKendaraanParkirArrayList(ArrayList<KendaraanPakir> kendaraanPakirArrayList) {
        this.kendaraanPakirArrayList = kendaraanPakirArrayList;
        notifyDataSetChanged();
    }

    public ArrayList<KendaraanPakir> getKendaraanPakirArrayList() {
        return kendaraanPakirArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNomorRegistrasi;
        private TextView tvInfoJam;
        private TextView tvNominal;

        private Button btnSelesai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomorRegistrasi = itemView.findViewById(R.id.tv_nomor_registrasi);
            tvInfoJam = itemView.findViewById(R.id.tv_info_jam);
            tvNominal = itemView.findViewById(R.id.tv_nominal);

            btnSelesai = itemView.findViewById(R.id.btn_selesai);
        }
    }
}
