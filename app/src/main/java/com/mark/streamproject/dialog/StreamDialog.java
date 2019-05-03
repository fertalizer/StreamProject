package com.mark.streamproject.dialog;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.CdnSettings;
import com.google.api.services.youtube.model.IngestionInfo;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastContentDetails;
import com.google.api.services.youtube.model.LiveBroadcastListResponse;
import com.google.api.services.youtube.model.LiveBroadcastSnippet;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import com.google.api.services.youtube.model.LiveStream;
import com.google.api.services.youtube.model.LiveStreamListResponse;
import com.google.api.services.youtube.model.LiveStreamSnippet;
import com.ksyun.media.diversity.screenstreamer.kit.KSYScreenStreamer;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.mark.streamproject.MainContract;
import com.mark.streamproject.R;
import com.mark.streamproject.StreamProject;
import com.mark.streamproject.util.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class StreamDialog extends AppCompatDialogFragment implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks, ActivityCompat.OnRequestPermissionsResultCallback,
        RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "StreamDialog";

    MainContract.Presenter mMainPresenter;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton480;
    private RadioButton mRadioButton720;
    private EditText mEditTextTitle;
    private AlertDialog mAlertDialog;
    private Button mButtonStart;
    private Button mButtonCancel;
    private ImageView mButtonDismiss;

    private final static int PERMISSION_REQUEST_RECORD_AUDIO = 2;
    private final static int OVERLAY_PERMISSION_RESULT_CODE = 10;

    private KSYScreenStreamer mScreenStreamer;
    private Handler mMainHandler;

    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;


    GoogleAccountCredential mCredential;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE };

    private boolean isStreaming;
    private boolean isLoading;

    private YouTube mYouTube;
    private LiveBroadcast mLiveBroadcast;

    private String mTitle;
    private String mTag;
    private String mImage;
    private String mWatchId;
    private long mPublishTime;

    public StreamDialog() {
    }

    public void setMainPresenter(MainContract.Presenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NORMAL, R.style.StreamDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_stream, container, false);
        mRadioButton480 = view.findViewById(R.id.radiobutton_480);
        mRadioButton720 = view.findViewById(R.id.radiobutton_720);
        mRadioGroup = view.findViewById(R.id.resolution_group);
        mEditTextTitle = view.findViewById(R.id.edit_broadcast_title);
        mButtonStart = view.findViewById(R.id.button_start);
        mButtonCancel = view.findViewById(R.id.button_cancel);
        mButtonDismiss = view.findViewById(R.id.image_dismiss);
        mButtonStart.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
        mButtonDismiss.setOnClickListener(this);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isStreaming = false;
        isLoading = false;

        mMainHandler = new Handler();
        mScreenStreamer = new KSYScreenStreamer(StreamProject.getAppContext());

        mRadioGroup.check(R.id.radiobutton_720);
        setUpStreamer(StreamerConstants.VIDEO_RESOLUTION_720P);
        mRadioGroup.setOnCheckedChangeListener(this);


        mCredential = GoogleAccountCredential.usingOAuth2(
                StreamProject.getAppContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        mCredential.setSelectedAccount(mMainPresenter.getAccount().getAccount());
    }

    private void setUpStreamer(int resolution) {
        mScreenStreamer.setTargetFps(15);
        int videoBitrate = 800;
        mScreenStreamer.setVideoKBitrate(videoBitrate * 3 / 4, videoBitrate, videoBitrate / 4);
        mScreenStreamer.setAudioKBitrate(48);
        mScreenStreamer.setTargetResolution(resolution);
        mScreenStreamer.setVideoCodecId(AVConst.CODEC_ID_AVC);
        mScreenStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
        mScreenStreamer.setVideoEncodeScene(VideoEncodeFormat.ENCODE_SCENE_SHOWSELF);
        mScreenStreamer.setVideoEncodeProfile(VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER);
        mScreenStreamer.setIsLandspace(true);
        mScreenStreamer.setEnableAutoRestart(true, 3000);
        mScreenStreamer.setOnInfoListener(mOnInfoListener);
        mScreenStreamer.setOnErrorListener(mOnErrorListener);
        mScreenStreamer.setOnLogEventListener(mOnLogEventListener);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radiobutton_480:
                Log.d(Constants.TAG, "480P");
                setUpStreamer(StreamerConstants.VIDEO_RESOLUTION_480P);
                break;
            case R.id.radiobutton_720:
                Log.d(Constants.TAG, "720P");
                setUpStreamer(StreamerConstants.VIDEO_RESOLUTION_720P);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                if (!isStreaming && !isLoading) {
                    getResultsFromApi();
                    isStreaming = true;
                    isLoading = true;
                    mButtonCancel.setText("停止直播");
                } else {
                    Toast.makeText(StreamProject.getAppContext(), "直播進行中", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_cancel:
                if (isStreaming) {
                    mButtonCancel.setText("取消");
                    new EndLiveBroadcast().execute();
                } else {
                    dismiss();
                }
                break;
            case R.id.image_dismiss:
                if (isStreaming) {
                    new EndLiveBroadcast().execute();
                }
                dismiss();
                break;
            default:
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        requestPermission();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }

        if (mScreenStreamer != null) {
            mScreenStreamer.setOnLogEventListener(null);
            mScreenStreamer.release();
        }

        if (isStreaming) {
            new EndLiveBroadcast().execute();
        }
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) StreamProject.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(StreamProject.getAppContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(StreamProject.getAppContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Log.d("Mark", "No network connection available.");
        } else {
            Log.d("Mark", "Coming Soon");
            new CreateEventTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                StreamProject.getAppContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    private void requestPermission() {
        //audio
        int audioperm = ActivityCompat.checkSelfPermission(StreamProject.getAppContext(), Manifest.permission.RECORD_AUDIO);
        if (audioperm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e("Mark", "No RECORD_AUDIO permission, please check");
                Toast.makeText(StreamProject.getAppContext(), "No RECORD_AUDIO permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.RECORD_AUDIO};
                ActivityCompat.requestPermissions(getActivity(), permissions,
                        PERMISSION_REQUEST_RECORD_AUDIO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
        switch (requestCode) {
            case PERMISSION_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Log.e("Mark", "No RECORD_AUDIO permission");
                    Toast.makeText(StreamProject.getAppContext(), "No RECORD_AUDIO permission",
                            Toast.LENGTH_LONG).show();
                }
                break;

            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case OVERLAY_PERMISSION_RESULT_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(StreamProject.getAppContext())) {
                        // SYSTEM_ALERT_WINDOW permission not granted...
                        Toast.makeText(StreamProject.getAppContext(), "SYSTEM_ALERT_WINDOW not granted",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != Activity.RESULT_OK) {
                    Log.d("Mark",
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    private void startStream() {
        mScreenStreamer.startStream();
    }


    private void stopStream() {
        mScreenStreamer.stopStream();
        isStreaming = false;
    }

    private KSYScreenStreamer.OnInfoListener mOnInfoListener =
            new KSYScreenStreamer.OnInfoListener() {
                @Override
                public void onInfo(int what, int msg1, int msg2) {
                    switch (what) {
                        case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                            Log.d(TAG, "KSY_STREAMER_OPEN_STREAM_SUCCESS");
                            break;
                        case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                            Log.d(TAG, "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
                            Toast.makeText(StreamProject.getAppContext(), "Network not good!",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                            Log.d(TAG, "BW raise to " + msg1 / 1000 + "kbps");
                            break;
                        case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                            Log.d(TAG, "BW drop to " + msg1 / 1000 + "kpbs");
                            break;
                        default:
                            Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                            break;
                    }
                }
            };

    private void handleEncodeError() {
        int encodeMethod = mScreenStreamer.getVideoEncodeMethod();
        if (encodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
            mHWEncoderUnsupported = true;
            if (mSWEncoderUnsupported) {
                mScreenStreamer.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mScreenStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE mode");
            }
        } else if (encodeMethod == StreamerConstants.ENCODE_METHOD_SOFTWARE) {
            mSWEncoderUnsupported = true;
            if (mHWEncoderUnsupported) {
                Log.e(TAG, "Got SW encoder error, can not streamer");
            } else {
                mScreenStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_HARDWARE);
                Log.e(TAG, "Got SW encoder error, switch to HARDWARE mode");
            }
        }
    }

    private KSYScreenStreamer.OnErrorListener mOnErrorListener =
            new KSYScreenStreamer.OnErrorListener() {
                @Override
                public void onError(int what, int msg1, int msg2) {
                    switch (what) {
                        case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
                            Log.d(TAG, "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
                            break;
                        case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
                            Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_FAILED");
                            break;
                        case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
                            Log.d(TAG, "KSY_STREAMER_ERROR_PUBLISH_FAILED");
                            break;
                        case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
                            Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_BREAKED");
                            break;
                        case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                            Log.d(TAG, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                            break;
                        case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                            Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                            break;
                        case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                            Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                            break;
                        case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                            Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                            break;
                        case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                            Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                            break;
                        case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                            Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                            break;
                        case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                            Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                            break;
                        case KSYScreenStreamer.KSY_STREAMER_SCREEN_RECORD_UNSUPPORTED:
                            Log.d(TAG, "KSY_STREAMER_SCREEN_RECORD_UNSUPPORTED");
                            Toast.makeText(StreamProject.getAppContext(), "you android system is below 21, " +
                                            "can not use screenRecord",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case KSYScreenStreamer.KSY_STREAMER_SCREEN_RECORD_PERMISSION_DENIED:
                            Log.d(TAG, "KSY_STREAMER_SCREEN_RECORD_PERMISSION_DENIED");
                            Toast.makeText(StreamProject.getAppContext(), "No ScreenRecord permission, please check",
                                    Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.d(TAG, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                            break;
                    }
                    switch (what) {
                        case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                        case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                            break;
                        case KSYScreenStreamer.KSY_STREAMER_SCREEN_RECORD_UNSUPPORTED:
                            Log.d(TAG, "KSY_STREAMER_SCREEN_RECORD_UNSUPPORTED");
                            break;
                        case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_CLOSE_FAILED:
                        case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_ERROR_UNKNOWN:
                        case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_OPEN_FAILED:
                        case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_WRITE_FAILED:
                            break;
                        case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                        case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN: {
                            handleEncodeError();
                            stopStream();
                            mMainHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startStream();
                                }
                            }, 3000);
                        }
                        break;
                        default:
                            if (mScreenStreamer.getEnableAutoRestart()) {
                                Log.d(TAG, "AutoRestart");
                            } else {
                                stopStream();
                                mMainHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startStream();
                                    }
                                }, 3000);
                            }
                            break;
                    }
                }
            };


    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                }
            };




    /**
     * An asynchronous task that handles the YouTube Data API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class CreateEventTask extends AsyncTask<Void, Void, Void> {
        private YouTube youTube = null;
        private Exception mLastError = null;
        private String push_addr;
        private String share_addr;
        private LiveBroadcast returnedBroadcast;


        CreateEventTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            youTube = new YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("YouTube Data API Android Quickstart")
                    .build();
            mTitle = mEditTextTitle.getText().toString().trim();
        }

        /**
         * Background task to call YouTube Data API.
         */
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                System.out.println("You chose " + mTitle + " for broadcast title.");

                LiveBroadcastSnippet broadcastSnippet = new LiveBroadcastSnippet();
                broadcastSnippet.setTitle(mTitle);
                long time = System.currentTimeMillis();
                broadcastSnippet.setPublishedAt(new DateTime(time));
                broadcastSnippet.setScheduledStartTime(new DateTime(time));
//                broadcastSnippet.setScheduledEndTime(new DateTime(time + 60 * 60 * 1000));

                LiveBroadcastStatus status = new LiveBroadcastStatus();
                status.setPrivacyStatus("public");

                LiveBroadcast broadcast = new LiveBroadcast();
                broadcast.setKind("youtube#liveBroadcast");
                broadcast.setSnippet(broadcastSnippet);
                broadcast.setStatus(status);

//                LiveBroadcastContentDetails contentDetails = new LiveBroadcastContentDetails();
//                MonitorStreamInfo monitorStream = new MonitorStreamInfo();
//                monitorStream.setEnableMonitorStream(false);
//                contentDetails.setMonitorStream(monitorStream);

                LiveBroadcastContentDetails contentDetails = new LiveBroadcastContentDetails();
                contentDetails.setProjection("rectangular");
                broadcast.setContentDetails(contentDetails);

                YouTube.LiveBroadcasts.Insert liveBroadcastInsert =
                        youTube.liveBroadcasts().insert("snippet,status,contentDetails", broadcast);
                returnedBroadcast = liveBroadcastInsert.execute();

                // Print information from the API response.
                System.out.println("\n================== Returned Broadcast ==================\n");
                System.out.println("  - Id: " + returnedBroadcast.getId());
                System.out.println("  - Title: " + returnedBroadcast.getSnippet().getTitle());
                System.out.println("  - Description: " + returnedBroadcast.getSnippet().getDescription());
                System.out.println("  - Published At: " + returnedBroadcast.getSnippet().getPublishedAt());
                System.out.println(
                        "  - Scheduled Start Time: " + returnedBroadcast.getSnippet().getScheduledStartTime());
                System.out.println(
                        "  - Scheduled End Time: " + returnedBroadcast.getSnippet().getScheduledEndTime());


                System.out.println("You chose " + mTitle + " for stream title.");


                LiveStreamSnippet streamSnippet = new LiveStreamSnippet();
                streamSnippet.setTitle(mTitle);

                IngestionInfo ingestionInfo = new IngestionInfo();
                ingestionInfo.setStreamName("Education");
                ingestionInfo.setIngestionAddress("rtmp://a.rtmp.youtube.com/live2");

                CdnSettings cdnSettings = new CdnSettings();
//                cdnSettings.setFormat("480p");
                cdnSettings.setResolution("variable");
                cdnSettings.setFrameRate("variable");
                cdnSettings.setIngestionType("rtmp");

                LiveStream stream = new LiveStream();
                stream.setKind("youtube#liveStream");
                stream.setSnippet(streamSnippet);
                stream.setCdn(cdnSettings);


                YouTube.LiveStreams.Insert liveStreamInsert =
                        youTube.liveStreams().insert("snippet,cdn", stream);
                LiveStream returnedStream = liveStreamInsert.execute();

                // Print information from the API response.
                System.out.println("\n================== Returned Stream ==================\n");
                System.out.println("  - Id: " + returnedStream.getId());
                System.out.println("  - Title: " + returnedStream.getSnippet().getTitle());
                System.out.println("  - Description: " + returnedStream.getSnippet().getDescription());
                System.out.println("  - Published At: " + returnedStream.getSnippet().getPublishedAt());
                System.out.println("  - Ingestion Address: " + returnedStream.getCdn().getIngestionInfo().getIngestionAddress());


                YouTube.LiveBroadcasts.Bind liveBroadcastBind =
                        youTube.liveBroadcasts().bind(returnedBroadcast.getId(), "id,contentDetails");
                liveBroadcastBind.setStreamId(returnedStream.getId());
                Log.d("Mark", "setStreamId = " + returnedStream.getId());
                returnedBroadcast = liveBroadcastBind.execute();


                // Print information from the API response.
                System.out.println("\n================== Returned Bound Broadcast ==================\n");
                System.out.println("  - Broadcast Id: " + returnedBroadcast.getId());
                System.out.println(
                        "  - Bound Stream Id: " + returnedBroadcast.getContentDetails().getBoundStreamId());


                share_addr = "https://www.youtube.com/watch?v=" + returnedBroadcast.getId();
                push_addr = returnedStream.getCdn().getIngestionInfo().getIngestionAddress() + "/" + returnedStream.getCdn()
                        .getIngestionInfo().getStreamName();


                Log.d("Mark", "image_url_1 = " + "http://img.youtube.com/vi/" + returnedBroadcast.getId() + "/0.jpg");
                Log.d("Mark", "image_url_2 = " + "http://img.youtube.com/vi/" + returnedBroadcast.getId() + "/1.jpg");
                Log.d("Mark", "image_url_3 = " + "http://img.youtube.com/vi/" + returnedBroadcast.getId() + "/2.jpg");
                Log.d("Mark", "image_url_4 = " + "http://img.youtube.com/vi/" + returnedBroadcast.getId() + "/3.jpg");
                Log.d("Mark", "push_addr = " + push_addr);
                Log.d("Mark", "share_addr = " + share_addr);

                mTag = "Entertainment";
                mImage = "http://img.youtube.com/vi/" + returnedBroadcast.getId() + "/hqdefault.jpg";
                mWatchId = returnedBroadcast.getId();
                mPublishTime = time;
                mMainPresenter.createRoom(mTitle, mTag, mImage, mWatchId, mPublishTime);

//            } catch (GoogleJsonResponseException e) {
//                System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
//                        + e.getDetails().getMessage());
//                e.printStackTrace();

            } catch (UserRecoverableAuthIOException e) {
                startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Throwable: " + t.getMessage());
                t.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAlertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog)
                    .setView(R.layout.dialog_loading)
                    .setCancelable(false)
                    .create();
            mAlertDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Mark", "OK");
            mAlertDialog.dismiss();
            if (push_addr != null) {
                mScreenStreamer.setUrl(push_addr);
                startStream();
                isLoading = false;
                new GetStreamStatusTask(youTube, returnedBroadcast).execute();
            } else {
                isStreaming = false;
                isLoading = false;
                Toast.makeText(getContext(), "請先到 Youtube 帳號開啟直播相關功能", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    Log.d("Mark", "The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.d("Mark", "Request cancelled.");
            }
        }
    }

    public class GetStreamStatusTask extends AsyncTask<Void, Void, Void> {
        YouTube youTube;
        LiveBroadcast returnedBroadcast;


        public GetStreamStatusTask(YouTube youTube, LiveBroadcast liveBroadcast) {
            this.youTube = youTube;
            returnedBroadcast = liveBroadcast;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            getStreamStatus();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Mark", "Stream status is active");
            // change broadcast status
            new changeBroadcastTestingStatusTask(youTube, returnedBroadcast).execute();
        }

        private void  getStreamStatus() {
            try {

                YouTube.LiveStreams.List liveStreamRequest = youTube.liveStreams()
                        .list("id,status")
                        .setId(returnedBroadcast.getContentDetails().getBoundStreamId());
                LiveStreamListResponse returnedList = liveStreamRequest.execute();
                List<LiveStream> liveStreams = returnedList.getItems();
                if (liveStreams != null && liveStreams.size() > 0) {
                    LiveStream liveStream = liveStreams.get(0);
                    if (liveStream != null) {
                        Log.d("Mark", "StreamStatus = " + liveStream.getStatus().getStreamStatus());
                        while (!liveStream.getStatus().getStreamStatus()
                                .equals("active")) {
                            Thread.sleep(5000);
                            returnedList = liveStreamRequest.execute();
                            liveStreams = returnedList.getItems();
                            liveStream = liveStreams.get(0);
                            Log.d("Mark", "StreamStatus = " + liveStream.getStatus().getStreamStatus());
                        }
                    }
                }
            } catch (GoogleJsonResponseException e) {
                System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Throwable: " + t.getMessage());
                t.printStackTrace();
            }
        }
    }

    public class changeBroadcastTestingStatusTask extends AsyncTask<Void, Void, Void> {
        YouTube youTube;
        LiveBroadcast returnedBroadcast;

        public changeBroadcastTestingStatusTask(YouTube youTube, LiveBroadcast liveBroadcast) {
            this.youTube = youTube;
            returnedBroadcast = liveBroadcast;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                YouTube.LiveBroadcasts.Transition broadCastRequest = youTube
                        .liveBroadcasts().transition("testing",
                                returnedBroadcast.getId(), "id,contentDetails,status");

                returnedBroadcast = broadCastRequest.execute();
                String youTubeLink = "https://www.youtube.com/watch?v="+returnedBroadcast.getId();
                Log.d("Mark", "youTubeLink:"+youTubeLink);
                Log.d("Mark", "Broad EmbedHtml:"+returnedBroadcast.getContentDetails().getMonitorStream().getEmbedHtml());
                Log.d("Mark", "Broad Cast Status:"+returnedBroadcast.getStatus().getLifeCycleStatus());






            } catch (GoogleJsonResponseException e) {
                System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Throwable: " + t.getMessage());
                t.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Mark", "Change broadcast status to testStarting");
            new testStartingTask(youTube, returnedBroadcast).execute();
        }
    }

    public class testStartingTask extends AsyncTask<Void, Void, Void> {
        YouTube youTube;
        LiveBroadcast returnedBroadcast;


        public testStartingTask(YouTube youTube, LiveBroadcast liveBroadcast) {
            this.youTube = youTube;
            returnedBroadcast = liveBroadcast;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                YouTube.LiveBroadcasts.List liveBroadRequest = youTube.liveBroadcasts().list("id,status");
                liveBroadRequest.setBroadcastStatus("all");

                LiveBroadcastListResponse liveBroadcastResponse = liveBroadRequest.execute();
                List<LiveBroadcast> returnedList1 = liveBroadcastResponse.getItems();
                if (returnedList1 != null && returnedList1.size() > 0) {
                    LiveBroadcast liveBroadcastReq = returnedList1.get(0);
                    if (liveBroadcastReq != null) {
                        while (!liveBroadcastReq.getStatus().getLifeCycleStatus().equals("testing")) {
                            Thread.sleep(5000);
                            Log.d("Mark", "publish broadcast - getLifeCycleStatus: " + liveBroadcastReq.getStatus().getLifeCycleStatus());
                            liveBroadcastResponse = liveBroadRequest.execute();
                            returnedList1 = liveBroadcastResponse.getItems();
                            liveBroadcastReq = returnedList1.get(0);
                        }
                        Log.d("Mark","publish broadcast - getLifeCycleStatus: " + liveBroadcastReq.getStatus().getLifeCycleStatus());
                    }
                }

            } catch (GoogleJsonResponseException e) {
                System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Throwable: " + t.getMessage());
                t.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Change broadcast status to live
            Log.d("Mark", "Stream Prepare");
            new changeBroadcastLiveStatusTask(youTube, returnedBroadcast).execute();
        }
    }

    public class changeBroadcastLiveStatusTask extends AsyncTask<Void, Void, Void> {
        YouTube youTube;
        LiveBroadcast returnedBroadcast;

        public changeBroadcastLiveStatusTask(YouTube youTube, LiveBroadcast liveBroadcast) {
            this.youTube = youTube;
            returnedBroadcast = liveBroadcast;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d("Mark", "Hello");
                YouTube.LiveBroadcasts.Transition transitionRequest = youTube.liveBroadcasts().transition("live",
                        returnedBroadcast.getId(), "status");
                returnedBroadcast = transitionRequest.execute();

                YouTube.LiveBroadcasts.List liveBroadRequest = youTube.liveBroadcasts().list("id,status");
                liveBroadRequest.setBroadcastStatus("all");

                LiveBroadcastListResponse liveBroadcastResponse = liveBroadRequest.execute();
                List<LiveBroadcast> returnedList2 = liveBroadcastResponse.getItems();
                if (returnedList2 != null && returnedList2.size() > 0) {
                    LiveBroadcast liveBroadcastReq = returnedList2.get(0);
                    if (liveBroadcastReq != null) {
                        while (!liveBroadcastReq.getStatus().getLifeCycleStatus().equals("live")) {
                            Thread.sleep(5000);
                            Log.d("Mark", "publish broadcast - getLifeCycleStatus: " + liveBroadcastReq.getStatus().getLifeCycleStatus());
                            liveBroadcastResponse = liveBroadRequest.execute();
                            returnedList2 = liveBroadcastResponse.getItems();
                            liveBroadcastReq = returnedList2.get(0);
                        }
                        Log.d("Mark","publish broadcast - getLifeCycleStatus: " + liveBroadcastReq.getStatus().getLifeCycleStatus());
                    }
                }

            } catch (GoogleJsonResponseException e) {
                System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Throwable: " + t.getMessage());
                t.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Mark", "Change broadcast status to live");
            setYoutube(youTube);
            setLiveBroadcast(returnedBroadcast);
            mMainPresenter.changeStatus(Constants.STREAMING);
        }
    }

    public class EndLiveBroadcast extends AsyncTask<Void, Void, Void> {
        YouTube youTube;
        LiveBroadcast returnedBroadcast;

        public EndLiveBroadcast() {
            youTube = getYoutube();
            returnedBroadcast = getLiveBroadcast();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                YouTube.LiveBroadcasts.Transition transitionRequest = youTube.liveBroadcasts().transition("complete",
                        returnedBroadcast.getId(), "status");
                returnedBroadcast = transitionRequest.execute();

            } catch (GoogleJsonResponseException e) {
                System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                e.printStackTrace();
            } catch (Throwable t) {
                System.err.println("Throwable: " + t.getMessage());
                t.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Mark", "End broadcast");
            stopStream();
            mMainPresenter.changeStatus(Constants.ONLINE);
            mMainPresenter.closeRoom();
        }
    }


    private void setYoutube(YouTube youtube) {
        mYouTube = youtube;
    }

    private void setLiveBroadcast(LiveBroadcast liveBroadcast) {
        mLiveBroadcast = liveBroadcast;
    }

    private YouTube getYoutube() {
        return  mYouTube;
    }

    private LiveBroadcast getLiveBroadcast() {
        return mLiveBroadcast;
    }
}
