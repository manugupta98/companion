package com.sdpd.companion.viewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpd.companion.data.model.Report;

public class ReportGroupViewModel extends ViewModel {

    private DatabaseReference mDatabase;
    private GoogleSignInAccount account;
    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public void report(String reportGroupId, String reportGroupDescription) {
        account = getAccount();
        Log.i("ACCOUNT", account.getEmail());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        writeNewReport(account.getId(), account.getEmail(), reportGroupId, reportGroupDescription);
    }

    private GoogleSignInAccount getAccount() {
        account = GoogleSignIn.getLastSignedInAccount(context);
        return account;
    }

    private void writeNewReport(String userId, String email, String reportGroupId, String reportGroupDescription) {
        Report report = new Report(email, reportGroupId, reportGroupDescription);

        mDatabase.child("Reports").child(userId).push().setValue(report);
    }
}
