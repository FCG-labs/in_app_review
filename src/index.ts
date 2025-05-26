import { NativeModules, Platform } from 'react-native';

export interface OpenStoreOptions {
  appStoreId?: string;
  microsoftStoreId?: string;
}

const MODULE_NAME = 'InAppReviewModule';
const InAppReviewModule = NativeModules[MODULE_NAME] as {
  isAvailable: () => Promise<boolean>;
  requestReview: (isFakeMode?: boolean) => Promise<void>;
  openStoreListing: (options: OpenStoreOptions) => Promise<void>;
};

function ensureAvailable() {
  if (!InAppReviewModule) {
    throw new Error(`Native module '${MODULE_NAME}' is not linked`);
  }
}

export async function isAvailable(): Promise<boolean> {
  ensureAvailable();
  return InAppReviewModule.isAvailable();
}

export async function requestReview(isFakeMode?: boolean): Promise<void> {
  ensureAvailable();
  return InAppReviewModule.requestReview(isFakeMode);
}

export async function openStoreListing(options: OpenStoreOptions = {}): Promise<void> {
  ensureAvailable();
  if (Platform.OS === 'ios' && !options.appStoreId) {
    throw new Error('appStoreId is required on iOS');
  }
  if (Platform.OS === 'windows' && !options.microsoftStoreId) {
    throw new Error('microsoftStoreId is required on Windows');
  }
  return InAppReviewModule.openStoreListing(options);
}

export default {
  isAvailable,
  requestReview,
  openStoreListing,
};
