package com.example.zello_sdk_sample_signin;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zello.sdk.Tab;

public class SigninActivity extends Activity implements com.zello.sdk.Events {

    //region Lifecycle Methods

    private EditText networkEdit;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private TextView textView;
    private CheckBox perishableCheckBox;
    private RelativeLayout signingInView;
    private RelativeLayout signInView;
    private Button cancelButton;
    private TextView errorTextView;
    private boolean signInAttempted = false;

    private com.zello.sdk.Sdk zelloSDK = new com.zello.sdk.Sdk();
    private com.zello.sdk.AppState appState = new com.zello.sdk.AppState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        signInView = (RelativeLayout)findViewById(R.id.signInView);
        networkEdit = (EditText)findViewById(R.id.networkEdit);
        usernameEdit = (EditText)findViewById(R.id.usernameEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        loginButton = (Button)findViewById(R.id.loginButton);
        textView = (TextView)findViewById(R.id.statusTextView);
        perishableCheckBox = (CheckBox)findViewById(R.id.checkBox);
        signingInView = (RelativeLayout)findViewById(R.id.signingInView);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        errorTextView = (TextView)findViewById(R.id.incorrectPasswordTextView);

        zelloSDK.onCreate("com.pttsdk", this, this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String network = networkEdit.getText().toString();
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                boolean perishable = perishableCheckBox.isChecked();

                signInAttempted = true;
                zelloSDK.signIn(network, username, password, perishable);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zelloSDK.cancel();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        zelloSDK.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        zelloSDK.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        zelloSDK.onPause();
    }

    //endregion

    //region Zello SDK Events

    @Override
    public void onMessageStateChanged() {

    }

    @Override
    public void onSelectedContactChanged() {

    }

    @Override
    public void onAudioStateChanged() {

    }

    @Override
    public void onContactsChanged() {

    }

    @Override
    public void onLastContactsTabChanged(Tab tab) {

    }

    @Override
    public void onAppStateChanged() {
        zelloSDK.getAppState(appState);

        updateUI();
    }

    //endregion

    private void updateUI() {
        if (appState.isSignedIn()) {
            textView.setText(R.string.signed_in);

            cancelButton.setVisibility(View.INVISIBLE);
        } else if (appState.isSigningIn()) {
            textView.setText(R.string.signing_in);
            cancelButton.setVisibility(View.VISIBLE);

            signInView.setVisibility(View.INVISIBLE);
            signingInView.setVisibility(View.VISIBLE);
        } else {
            signInView.setVisibility(View.VISIBLE);
            signingInView.setVisibility(View.INVISIBLE);
        }

        if (signInAttempted) {
            String error = getErrorText(appState.getLastError());
            if (error == null) {
                errorTextView.setText("");
            } else {
                errorTextView.setText(error);
            }
        }
    }

    private String getErrorText(com.zello.sdk.Error error) {
        switch (error) {
            case UNKNOWN:
                return getResources().getString(R.string.error_unknown);
            case INVALID_CREDENTIALS:
                return getResources().getString(R.string.error_invalid_credentials);
            case INVALID_NETWORK_NAME:
                return getResources().getString(R.string.error_invalid_network_name);
            case NETWORK_SUSPENDED:
                return getResources().getString(R.string.error_network_suspended);
            case SERVER_SECURE_CONNECT_FAILED:
                return getResources().getString(R.string.error_secure_connect_failed);
            case SERVER_SIGNIN_FAILED:
                return getResources().getString(R.string.error_server_signin_failed);
            case NETWORK_SIGNIN_FAILED:
                return getResources().getString(R.string.error_network_signin_failed);
            case KICKED:
                return getResources().getString(R.string.error_kicked);
            case APP_UPDATE_REQUIRED:
                return getResources().getString(R.string.error_update_required);
            case NO_INTERNET_CONNECTION:
                return getResources().getString(R.string.error_no_internet);
            case INTERNET_CONNECTION_RESTRICTED:
                return getResources().getString(R.string.error_internet_restricted);
            case SERVER_LICENSE_PROBLEM:
                return getResources().getString(R.string.error_server_license);
            case TOO_MANY_SIGNIN_ATTEMPTS:
                return getResources().getString(R.string.error_brute_force_protection);
            default:
                return null;
        }
    }

}
