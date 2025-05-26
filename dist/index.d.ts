export interface OpenStoreOptions {
    appStoreId?: string;
    microsoftStoreId?: string;
}
export declare function isAvailable(): Promise<boolean>;
export declare function requestReview(isFakeMode?: boolean): Promise<void>;
export declare function openStoreListing(options?: OpenStoreOptions): Promise<void>;
declare const _default: {
    isAvailable: typeof isAvailable;
    requestReview: typeof requestReview;
    openStoreListing: typeof openStoreListing;
};
export default _default;
