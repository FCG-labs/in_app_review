#import <React/RCTBridgeModule.h>
#import <StoreKit/StoreKit.h>

@interface InAppReviewModule : NSObject <RCTBridgeModule>
@end

@implementation InAppReviewModule

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(isAvailable:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
  if (@available(iOS 10.3, *)) {
    resolve(@(YES));
  } else {
    resolve(@(NO));
  }
}

RCT_EXPORT_METHOD(requestReview:(BOOL)isFakeMode
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
  if (@available(iOS 10.3, *)) {
    if(isFakeMode){}else{}
    
    [SKStoreReviewController requestReview];
  }
  resolve(nil);
}

RCT_EXPORT_METHOD(openStoreListing:(NSDictionary *)options
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
  NSString *appStoreId = options[@"appStoreId"];
  if (appStoreId == nil) {
    reject(@"error", @"appStoreId is required on iOS", nil);
    return;
  }
  NSString *urlString = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/app/id%@?action=write-review", appStoreId];
  [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlString] options:@{} completionHandler:nil];
  resolve(nil);
}

@end
