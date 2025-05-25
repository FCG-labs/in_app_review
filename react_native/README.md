# React Native In App Review

This directory contains a TypeScript implementation of an In-App Review module for React Native applications. The API mirrors the original Flutter plugin and exposes three functions:

- `isAvailable()` – checks whether the native in-app review functionality can be used.
- `requestReview()` – attempts to display the in-app review dialog.
- `openStoreListing()` – opens the platform's store listing for the application.

Each function delegates to a native module called `InAppReviewModule`. Native implementations for Android and iOS are required.
