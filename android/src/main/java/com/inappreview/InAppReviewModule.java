package com.inappreview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class InAppReviewModule extends ReactContextBaseJavaModule {
    private ReviewInfo reviewInfo;
    private final ReviewManager manager;

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
        promise.resolve(true);
    }

    @ReactMethod
    public void requestReview(Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            promise.reject("error", "Activity not available");
            return;
        }
        if (reviewInfo != null) {
            manager.launchReviewFlow(activity, reviewInfo)
                    .addOnCompleteListener(task -> promise.resolve(null));
            return;
        }
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

    @ReactMethod
    public void openStoreListing(Promise promise) {
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
}
