package com.bleizing.parkirqyu.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.LogTransaksi;

import java.util.ArrayList;

public class LogTransaksiAdapter extends RecyclerView.Adapter<LogTransaksiAdapter.ViewHolder> {

    private Context context;

    private ArrayList<LogTransaksi> logTransaksiArrayList;

    public LogTransaksiAdapter(Context context, ArrayList<LogTransaksi> logTransaksiArrayList) {
        this.context = context;
        this.logTransaksiArrayList = logTransaksiArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_log_transaksi, viewGroup, false);
        return new LogTransaksiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final LogTransaksi logTransaksi = logTransaksiArrayList.get(i);

        String nominal = logTransaksi.getNominal();

        if (logTransaksi.getInvoiceType().equals("Topup")) {
            nominal = "+" + nominal;
        } else {
            nominal = "-" + nominal;
        }

        if (logTransaksi.getNamaPetugas().equals("")) {
            viewHolder.tvNamaPetugas.setVisibility(View.GONE);
        }

        if (logTransaksi.getNomorRegistrasi().equals("")) {
            viewHolder.tvNomorRegistrasi.setVisibility(View.GONE);
        }

        viewHolder.tvNomorRegistrasi.setText(logTransaksi.getNomorRegistrasi());
        viewHolder.tvNominal.setText(nominal);
        viewHolder.tvNamaPetugas.setText(logTransaksi.getNamaPetugas());
        viewHolder.tvJenisPelanggan.setText(logTransaksi.getJenisPelanggan());
        viewHolder.tvTransactionType.setText(logTransaksi.getTransactionType());
        viewHolder.tvWaktu.setText(logTransaksi.getTime());
        viewHolder.tvInvoiceType.setText(logTransaksi.getInvoiceType());
        viewHolder.tvInvoiceCode.setText(logTransaksi.getInvoiceCode());
    }

    @Override
    public int getItemCount() {
        return logTransaksiArrayList.size();
    }

    public void setLogTransaksiArrayList(ArrayList<LogTransaksi> logTransaksiArrayList) {
        this.logTransaksiArrayList = logTransaksiArrayList;
        notifyDataSetChanged();
    }

    public ArrayList<LogTransaksi> getLogTransaksiArrayList() {
        return logTransaksiArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNomorRegistrasi;
        private TextView tvNominal;
        private TextView tvNamaPetugas;
        private TextView tvJenisPelanggan;
        private TextView tvWaktu;
        private TextView tvInvoiceType;
        private TextView tvInvoiceCode;
        private TextView tvTransactionType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomorRegistrasi = (TextView) itemView.findViewById(R.id.tv_nomor_registrasi);
            tvNominal = (TextView) itemView.findViewById(R.id.tv_nominal);
            tvNamaPetugas = (TextView) itemView.findViewById(R.id.tv_nama_petugas);
            tvJenisPelanggan = (TextView) itemView.findViewById(R.id.tv_jenis_pelanggan);
            tvWaktu = (TextView) itemView.findViewById(R.id.tv_time);
            tvInvoiceType = (TextView) itemView.findViewById(R.id.tv_invoice_type);
            tvInvoiceCode = (TextView) itemView.findViewById(R.id.tv_invoice_code);
            tvTransactionType = (TextView) itemView.findViewById(R.id.tv_transaction_type);
        }
    }
}
