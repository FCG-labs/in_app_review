package com.inappreview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.play.core.review.testing.FakeReviewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InAppReviewModule extends ReactContextBaseJavaModule {
    private ReviewInfo reviewInfo;
    private final ReviewManager manager;
    private final String TAG = "InAppReviewPlugin";

    public InAppReviewModule(ReactApplicationContext reactContext) {
        super(reactContext);
        manager = ReviewManagerFactory.create(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "InAppReviewModule";
    }

    @ReactMethod
    public void isAvailable(Promise promise) {
        Log.i(TAG, "isAvailable: called");
        if (!appInstalledBySupportedStore()) {
            invalidStoreWarning();
        }

        final boolean playStoreAndPlayServicesAvailable = isPlayStoreInstalled() && isPlayServicesAvailable();
        final boolean lollipopOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        Log.i(TAG, "isAvailable: playStoreAndPlayServicesAvailable: " + playStoreAndPlayServicesAvailable);
        Log.i(TAG, "isAvailable: lollipopOrLater: " + lollipopOrLater);

        if (!(playStoreAndPlayServicesAvailable && lollipopOrLater)) {
            // The play store isn't installed or the device isn't running Android 5 Lollipop(API 21) or
            // higher
            Log.w(TAG, "isAvailable: The Play Store must be installed, Play Services must be available and Android 5 or later must be used");
            promise.resolve(false);
        } else {
            // The API is likely available but we can ensure that it is by getting a ReviewInfo object
            // from the API. This will also speed up the review flow when we're ready to launch it as
            // the ReviewInfo doesn't need to be fetched.
            Log.i(TAG, "isAvailable: Play Store, Play Services and Android version requirements met");
            cacheReviewInfo(promise);
        }
    }

    @ReactMethod
    public void requestReview(Boolean isFakeMode, Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            promise.reject("error", "Activity not available");
            return;
        }
        if (reviewInfo != null) {
            if(isFakeMode == null || !isFakeMode)
                manager.launchReviewFlow(activity, reviewInfo)
                        .addOnCompleteListener(task -> promise.resolve(null));
            else 
                new FakeReviewManager(getReactApplicationContext()).launchReviewFlow(activity, reviewInfo)
                        .addOnCompleteListener(t -> promise.resolve(null));
            return;
        }

        if(isFakeMode){
            ReviewManager fakeManager = new FakeReviewManager(getReactApplicationContext());
            Task<ReviewInfo> fakeRequest = fakeManager.requestReviewFlow();
            fakeRequest.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                    fakeManager.launchReviewFlow(activity, reviewInfo)
                            .addOnCompleteListener(t -> promise.resolve(null));
                } else {
                    promise.reject("error", "In-App Review API unavailable");
                }
            });
        }else{
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                    manager.launchReviewFlow(activity, reviewInfo)
                            .addOnCompleteListener(t -> promise.resolve(null));
                } else {
                    promise.reject("error", "In-App Review API unavailable");
                }
            });
        }
        
    }

    @ReactMethod
    public void openStoreListing(ReadableMap options, Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            promise.reject("error", "Activity not available");
            return;
        }
        String packageName = activity.getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
        activity.startActivity(intent);
        promise.resolve(null);
    }

    private boolean appInstalledBySupportedStore() {
        final List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending"));
        final String installer = getReactApplicationContext().getPackageManager().getInstallerPackageName(getReactApplicationContext().getPackageName());
        Log.i(TAG, "appInstalledBySupportedStore: installer: " + installer);
        return installer != null && validInstallers.contains(installer);
    }

    private void invalidStoreWarning() {
        Log.w(TAG, "The app should be installed by the Play Store to test in_app_review. See https://pub.dev/packages/in_app_review#testing-read-carefully for more information.");
    }

    private boolean isPlayStoreInstalled() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getReactApplicationContext().getPackageManager().getPackageInfo("com.android.vending", PackageManager.PackageInfoFlags.of(0));
            } else {
                getReactApplicationContext().getPackageManager().getPackageInfo("com.android.vending", 0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "Play Store not installed.");
            return false;
        }

        return true;
    }

    private boolean isPlayServicesAvailable() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        if (availability.isGooglePlayServicesAvailable(getReactApplicationContext()) != ConnectionResult.SUCCESS) {
            Log.i(TAG, "Google Play Services not available");
            return false;
        }

        return true;
    }

    private void cacheReviewInfo(final Promise promise) {
        Log.i(TAG, "cacheReviewInfo: called");
        final ReviewManager manager = ReviewManagerFactory.create(getReactApplicationContext());

        final Task<ReviewInfo> request = manager.requestReviewFlow();

        Log.i(TAG, "cacheReviewInfo: Requesting review flow");
        request.addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                Log.i(TAG, "onComplete: Successfully requested review flow");
                reviewInfo = task.getResult();
                promise.resolve(true);
            } else {
                // The API isn't available
                Log.w(TAG, "onComplete: Unsuccessfully requested review flow");
                promise.resolve(false);
            }
        });

    }
}
