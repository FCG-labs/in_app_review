var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { NativeModules, Platform } from 'react-native';
const MODULE_NAME = 'InAppReviewModule';
const InAppReviewModule = NativeModules[MODULE_NAME];
function ensureAvailable() {
    if (!InAppReviewModule) {
        throw new Error(`Native module '${MODULE_NAME}' is not linked`);
    }
}
export function isAvailable() {
    return __awaiter(this, void 0, void 0, function* () {
        ensureAvailable();
        return InAppReviewModule.isAvailable();
    });
}
export function requestReview(isFakeMode) {
    return __awaiter(this, void 0, void 0, function* () {
        ensureAvailable();
        return InAppReviewModule.requestReview(isFakeMode);
    });
}
export function openStoreListing() {
    return __awaiter(this, arguments, void 0, function* (options = {}) {
        ensureAvailable();
        if (Platform.OS === 'ios' && !options.appStoreId) {
            throw new Error('appStoreId is required on iOS');
        }
        if (Platform.OS === 'windows' && !options.microsoftStoreId) {
            throw new Error('microsoftStoreId is required on Windows');
        }
        return InAppReviewModule.openStoreListing(options);
    });
}
export default {
    isAvailable,
    requestReview,
    openStoreListing,
};
