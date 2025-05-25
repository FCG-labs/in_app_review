Pod::Spec.new do |s|
  s.name             = 'ReactNativeInAppReview'
  s.version          = '0.1.0'
  s.summary          = 'React Native plugin for showing the In-App Review/System Rating pop up.'
  s.source           = { :path => '.' }
  s.source_files     = 'Sources/**/*'
  s.platform         = :ios, '12.0'
  s.requires_arc     = true
  s.dependency 'React-Core'

  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.resource_bundles = {'in_app_review_privacy' => ['Sources/PrivacyInfo.xcprivacy']}
end
