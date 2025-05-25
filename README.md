# React Native In App Review

This directory contains a TypeScript implementation of an In‑App Review
module for React Native applications. The package is published to npm under the
name **`react-native-in-app-review`**.

## Publishing

Before publishing to npm you must build the package. Run `npm run build` to
compile the sources to the `dist` folder and then execute `npm publish` from
this directory. The resulting package can be installed in a React Native
project via npm or yarn.

```bash
npm install react-native-in-app-review
# or
yarn add react-native-in-app-review
```

For iOS projects run:

```bash
cd ios && pod install
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
npm install <repo>#<tag>
cd ios && pod install  # for iOS
```

Android projects need to include the library module manually. Edit
`settings.gradle` to add the module:

```gradle
include ':react-native-in-app-review'
project(':react-native-in-app-review').projectDir =
    new File(rootProject.projectDir, '../node_modules/react-native-in-app-review/android')
```

and update `app/build.gradle`:

```gradle
implementation project(':react-native-in-app-review')
```

## Linking native modules

React Native will autolink the native module after installation. On iOS run
`npx pod-install` to update the CocoaPods workspace. If autolinking is disabled
follow the manual steps above to add the module to your Android project.

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

### JavaScript example

```js
import { requestReview, isAvailable } from 'react-native-in-app-review';

async function maybeReview() {
  if (await isAvailable()) {
    await requestReview();
  }
}
```
