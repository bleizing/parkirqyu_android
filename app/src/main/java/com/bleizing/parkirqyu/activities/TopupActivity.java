package com.bleizing.parkirqyu.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bleizing.parkirqyu.Constants;
import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.network.APIService;
import com.bleizing.parkirqyu.network.HTTPClient;
import com.bleizing.parkirqyu.network.ProcessTopupRequest;
import com.bleizing.parkirqyu.network.ProcessTopupResponse;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BILLING;
import static com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH;
import static com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_SHIPPING;

public class TopupActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private EditText editNominal;

    private int nominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editNominal = (EditText) findViewById(R.id.edit_nominal);

        progressDialog = new ProgressDialog(this);

        Button btnTopup = (Button) findViewById(R.id.btn_topup);
        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal = Integer.parseInt(editNominal.getText().toString());
                processTopup();
            }
        });
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
        Intent intent = new Intent(TopupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void processTopup() {
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ProcessTopupRequest request = new ProcessTopupRequest(Model.getUser().getUserId(), nominal);
        APIService apiService = HTTPClient.getClient().create(APIService.class);
        Call<ProcessTopupResponse> call = apiService.processTopup(request);
        call.enqueue(new Callback<ProcessTopupResponse>() {
            @Override
            public void onResponse(Call<ProcessTopupResponse> call, Response<ProcessTopupResponse> response) {
                if (response.isSuccessful()) {
                    switch (response.body().getStatusCode()) {
                        case Constants.STATUS_CODE_CREATED :
                            processTopupResponse(response.body().getData());
                            break;
                        case Constants.STATUS_CODE_BAD_REQUEST :
                            Toast.makeText(TopupActivity.this, getString(R.string.data_empty), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProcessTopupResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(TopupActivity.this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init()
                .setClientKey("SB-Mid-client-J72doCXrYCYxVjXD") // client_key is mandatory
                .setContext(TopupActivity.this) // context is mandatory
                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                    @Override
                    public void onTransactionFinished(TransactionResult result) {
                        String message = "Pembayaran gagal. Harap coba lagi";
                        int type = 0;
                        if (result != null && !result.getStatus().equals("")) {
                            message = "Transaksi telah berhasil";
                            if (result.getStatus().equals("pending")) {
                                message = "Harap segera melakukan pembayaran sesuai instruksi sebelum 24 jam";
                            }
                            type = 1;
                        }
                        final int finalType = type;
                        new AlertDialog.Builder(TopupActivity.this)
                                .setTitle(TopupActivity.this.getString(R.string.konfirmasi))
                                .setMessage(message)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if (finalType == 1) {
                                            onBackPressed();
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl("https://parkirqyu.bleizing.com/api/topup/") //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .buildSDK();

        disableUser();
    }

    private void disableUser() {
        UserDetail userDetail = LocalDataHandler.readObject("user_details", UserDetail.class);
        if (userDetail == null) {
            // Set user details
            userDetail = new UserDetail();
            userDetail.setUserFullName(Model.getUser().getNama());
            userDetail.setEmail(Model.getUser().getEmail());
            userDetail.setPhoneNumber("0000000000");

// Initiate address list
            ArrayList<UserAddress> userAddresses = new ArrayList<>();

// Initiate and add shipping address
            UserAddress shippingUserAddress = new UserAddress();
            shippingUserAddress.setAddress("Jakarta");
            shippingUserAddress.setCity("Jakarta");
            shippingUserAddress.setCountry("IDN");
            shippingUserAddress.setZipcode("14410");
            shippingUserAddress.setAddressType(ADDRESS_TYPE_SHIPPING);
            userAddresses.add(shippingUserAddress);

// Initiate and add billing address
            UserAddress billingUserAddress = new UserAddress();
            billingUserAddress.setAddress("Jakarta");
            billingUserAddress.setCity("Jakarta");
            billingUserAddress.setCountry("IDN");
            billingUserAddress.setZipcode("14410");
            billingUserAddress.setAddressType(ADDRESS_TYPE_BILLING);
            userAddresses.add(billingUserAddress);

// if shipping address is same billing address
// you can user type Constants.ADDRESS_TYPE_BOTH
// NOTE: if you use this, skip initiate shipping and billing address above
            UserAddress userAddress = new UserAddress();
            userAddress.setAddress("Jakarta");
            userAddress.setCity("Jakarta");
            userAddress.setCountry("IDN");
            userAddress.setZipcode("14410");
            userAddress.setAddressType(ADDRESS_TYPE_BOTH);
            userAddresses.add(userAddress);

// Set user address to user detail object
            userDetail.setUserAddresses(userAddresses);

// Save the user detail. It will skip the user detail screen
            LocalDataHandler.saveObject("user_details", userDetail);
        }

        UIKitCustomSetting uiKitCustomSetting = new UIKitCustomSetting();
        uiKitCustomSetting.setShowPaymentStatus(false);
        MidtransSDK.getInstance().setUIKitCustomSetting(uiKitCustomSetting);
    }

    private void processTopupResponse(ProcessTopupResponse.Data data) {
        initMidtransSDK();

        progressDialog.dismiss();

        CustomerDetails customer = new CustomerDetails(Model.getUser().getNama(), "", Model.getUser().getEmail(), "0000000000");

        TransactionRequest transactionRequest = new TransactionRequest(data.getInvoiceCode(), nominal);

        transactionRequest.setCustomerDetails(customer);
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);

        MidtransSDK.getInstance().startPaymentUiFlow(TopupActivity.this);
    }
}
