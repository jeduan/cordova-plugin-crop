#import <Cordova/CDVPlugin.h>
#import "PECropViewController.h"

@interface CTCrop : CDVPlugin <PECropViewControllerDelegate>
- (void) cropImage:(CDVInvokedUrlCommand *) command;
@end