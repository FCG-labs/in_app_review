import Foundation

extension Foundation.Bundle {
    static let module: Bundle = {
        let mainPath = Bundle.main.bundleURL.appendingPathComponent("in_app_review_in_app_review.bundle").path
        let buildPath = "/Users/fcg_dev01/project/atflee/rn_module/in_app_review/flutter/in_app_review/ios/in_app_review/.build/arm64-apple-macosx/debug/in_app_review_in_app_review.bundle"

        let preferredBundle = Bundle(path: mainPath)

        guard let bundle = preferredBundle ?? Bundle(path: buildPath) else {
            // Users can write a function called fatalError themselves, we should be resilient against that.
            Swift.fatalError("could not load resource bundle: from \(mainPath) or \(buildPath)")
        }

        return bundle
    }()
}