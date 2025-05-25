# React Native In App Review

This directory contains a TypeScript implementation of an In‑App Review
module for React Native applications. The package is published to npm under the
name **`react-native-in-app-review`**.

## Publishing

Run `npm run build` to compile the sources to the `dist` folder and then
execute `npm publish` from this directory. The resulting package can be
installed in a React Native project via npm or yarn.

```bash
npm install react-native-in-app-review
# or
yarn add react-native-in-app-review
```

## Installing from this repository

If you want to use a commit or tag that hasn't been published to npm you can
install the package directly from Git. First build the package so that the
`dist` folder is generated:

```bash
npm run build
```

Then from your React Native project run:

```bash
npm install <git-url>#<tag> --workspace=react_native
```

After installation React Native will autolink the native module. On iOS run
`npx pod-install` (or `cd ios && pod install`) to update the CocoaPods workspace.
Android requires no additional steps when autolinking is enabled.

## Linking native modules

React Native will autolink the native module after installation. For iOS you
should run `npx pod-install` to update the CocoaPods workspace. Android requires
no additional steps provided autolinking is enabled.

## TypeScript API

The API mirrors the original Flutter plugin and exposes three functions:

- `isAvailable()` – checks whether the native in-app review functionality can
  be used.
- `requestReview()` – attempts to display the in-app review dialog.
- `openStoreListing()` – opens the platform's store listing for the
  application.

Each function delegates to a native module called `InAppReviewModule`. Native
implementations for Android and iOS are required.

```ts
import { requestReview, isAvailable, openStoreListing } from 'react-native-in-app-review';

// Example usage
if (await isAvailable()) {
  await requestReview();
}
```
