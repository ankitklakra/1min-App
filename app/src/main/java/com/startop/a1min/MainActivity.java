package com.startop.a1min;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // When the activity is created, check for updates.
        checkForUpdate();
    }

    private void checkForUpdate() {
        // Create an AppUpdateManager instance for managing app updates.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns a Task that retrieves the update information from the Play Store.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Add a listener to handle the update information retrieval success.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            // Check if an update is available and if the update type is allowed (immediate).
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // If an update is available and allowed, request the update flow.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the update information retrieved earlier.
                            appUpdateInfo,
                            // Use the IMMEDIATE update type (you can use FLEXIBLE for non-blocking updates).
                            AppUpdateType.IMMEDIATE,
                            // The current activity (this) initiates the update process.
                            this,
                            // Provide a request code to identify the update request in onActivityResult.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    // Exception handling in case starting the update flow fails.
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            // Check if the update flow was successful (RESULT_OK) or not.
            if (resultCode != RESULT_OK) {
                // If the update is cancelled or fails, you can handle it here.
                // For example, you can decide whether to request the update again.
                // You might want to show a message to the user informing them about the update failure.
            }
        }
    }

}
