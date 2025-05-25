Pod::Spec.new do |s|
  s.name         = 'ReactNativeInAppReview'
  s.version      = '0.1.0'
  s.summary      = 'In-app review support for React Native.'
  s.homepage     = 'https://github.com/britannio/in_app_review'
  s.license      = { :type => 'MIT' }
  s.author       = { 'Britannio Jarrett' => 'britanniojarrett@gmail.com' }
  s.platform     = :ios, '12.0'
  s.source       = { :path => '.' }
  s.source_files = 'InAppReviewModule.mm'
  s.requires_arc = true
  s.dependency 'React-Core'
end
