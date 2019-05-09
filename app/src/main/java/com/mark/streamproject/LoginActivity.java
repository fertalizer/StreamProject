package com.mark.streamproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.mark.streamproject.util.Constants;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        TextView topTitle = findViewById(R.id.text_login_title_top);
        TextView bottomTitle = findViewById(R.id.text_login_title_bottom);
        TextView startButton = findViewById(R.id.button_login);

        topTitle.setTypeface(Typeface.createFromAsset(StreamProject.getAppContext().getAssets(), "fonts/Minecraftia-Regular.ttf"));
        bottomTitle.setTypeface(Typeface.createFromAsset(StreamProject.getAppContext().getAssets(), "fonts/Minecraftia-Regular.ttf"));
        startButton.setTypeface(Typeface.createFromAsset(StreamProject.getAppContext().getAssets(), "fonts/Minecraftia-Regular.ttf"));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        startButton.setOnClickListener(this);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("ACCOUNT",account);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        signIn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("ACCOUNT",account);
            startActivity(intent);
            finish();

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(Constants.TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


}
