//
//  AppDelegate.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "AppDelegate.h"
#import "HomeViewController.h"
#import "PricesViewController.h"
#import "NewsViewController.h"

@implementation AppDelegate
{
    NSString *FetchedToken;
    
}

-(void)clearNotifications
{
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber: 1];
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber: 0];
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
    
}

-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    NSLog(@"%@",userInfo);
    UIApplicationState state = [application applicationState];
    if (state == UIApplicationStateActive) {
        
        NSString *Title = [userInfo objectForKey:@"Title"];
        NSString *message = [[userInfo valueForKey:@"aps"] valueForKey:@"alert"];
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:Title
                                                            message:message
                                                           delegate:self
                                                  cancelButtonTitle:@"Ok"
                                                  otherButtonTitles:nil, nil];
        //                                                  otherButtonTitles:showTitle, nil];
        [alertView show];
    } else
    {
        NSInteger Option = 0;
        if ([userInfo objectForKey:@"path"]) {
            Option = [[userInfo objectForKey:@"path"] integerValue];
        }
        //Do stuff that you would do if the application was not active
        NSLog(@"state: Not UIApplicationStateActive");
        self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
        
        UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        HomeViewController *demoController= (HomeViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"Home"];
        
        UINavigationController *Nav = [[UINavigationController alloc] initWithRootViewController:demoController];
        Nav.navigationBarHidden = YES;

        if (Option == 1) {
            
            
            NewsViewController *demoController= (NewsViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"News"];
            [Nav pushViewController:demoController animated:YES];
            
            
        }
        else if (Option == 2)
        {
            PricesViewController *demoController= (PricesViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"Prices"];
            [Nav pushViewController:demoController animated:YES];
        }    

        self.window.rootViewController = Nav;
        [self.window makeKeyAndVisible];
        //        return YES;
    }

}
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    if (launchOptions[UIApplicationLaunchOptionsRemoteNotificationKey]) {
//        if (true) {
//            NSDictionary *Test = @{@"path": @"2"};
//            [self application:application didReceiveRemoteNotification:Test];

            [self application:application didReceiveRemoteNotification:launchOptions[UIApplicationLaunchOptionsRemoteNotificationKey]];
            NSDictionary* dictionary = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
        
        if (dictionary != nil)
        {
            NSLog(@"Launched from push notification: %@", dictionary);
            
            [self clearNotifications];
        }
    }
    
    
    //[[UIApplication sharedApplication] registerForRemoteNotificationTypes:
    // (UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    
    
    [[UIApplication sharedApplication] registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge) categories:nil]];
    [[UIApplication sharedApplication] registerForRemoteNotifications];


    return YES;
}

- (void)application:(UIApplication*)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData*)deviceToken
{
    NSString* newToken = [deviceToken description];
	newToken = [newToken stringByTrimmingCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"<>"]];
	newToken = [newToken stringByReplacingOccurrencesOfString:@" " withString:@""];
    
    
    [[NSUserDefaults standardUserDefaults] setObject:newToken forKey:@"Token"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
	//NSLog(@"My token is: %@", newToken);
    [self registerDevice:newToken];
}

-(void)registerDevice:(NSString *)newToken
{
    if (![[NSUserDefaults standardUserDefaults] boolForKey:@"TokenSaved"]) {
       
        FetchedToken = newToken;
        
        NSString *URLL = [NSString stringWithFormat:@"http://www.hodico.com/mAdmin/Register.php?Token=%@",newToken];
        NSURL *url = [NSURL URLWithString:URLL];
        
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
        [request setHTTPMethod:@"POST"];
        [request setValue:@"application/x-www-form-urlencoded; charset=utf-8" forHTTPHeaderField:@"Content-Type"];

        NSString *postString = [NSString stringWithFormat:@"Token=%@",newToken];
        [request setHTTPBody:[postString dataUsingEncoding:NSUTF8StringEncoding]];
        
        NSLog(@"Sending: %@",postString);
        NSURLConnection *connection= [[NSURLConnection alloc] initWithRequest:request
                                                                     delegate:self];
        
    }

}
#pragma mark NSURLConnection Delegate Methods

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    // A response has been received, this is where we initialize the instance var you created
    // so that we can append data to it in the didReceiveData method
    // Furthermore, this method is called each time there is a redirect so reinitializing it
    // also serves to clear it
    _responseData = [[NSMutableData alloc] init];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    // Append the new data to the instance variable you declared
    [_responseData appendData:data];
}

- (NSCachedURLResponse *)connection:(NSURLConnection *)connection
                  willCacheResponse:(NSCachedURLResponse*)cachedResponse {
    // Return nil to indicate not necessary to store a cached response for this connection
    return nil;
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    // The request is complete and data has been received
    // You can parse the stuff in your instance variable now
    NSString* newStr = [NSString stringWithUTF8String:[_responseData bytes]];
    NSLog(@"Token: %@",newStr);
    FetchedToken = [FetchedToken stringByAppendingString:FetchedToken];
    NSLog(@"FetchedToken: %@",FetchedToken);
    
    if ([newStr isEqualToString:FetchedToken]) {
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"TokenSaved"];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }    
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    // The request has failed for some reason!
    // Check the error var
    
	NSLog(@"Connection Failed, error: %@", error);
}

- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error
{
	NSLog(@"Failed to get token, error: %@", error);
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
