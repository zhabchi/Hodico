//
//  AppDelegate.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//




#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate, NSURLConnectionDelegate>
{
    NSMutableData *_responseData;
}

@property (strong, nonatomic) UIWindow *window;


@end
