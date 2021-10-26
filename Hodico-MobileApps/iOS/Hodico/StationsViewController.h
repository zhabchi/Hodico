//
//  StationsViewController.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

#import "MBProgressHUD.h"

@interface StationsViewController : UIViewController <MKMapViewDelegate,MBProgressHUDDelegate,UIAlertViewDelegate>
@property (strong, nonatomic) IBOutlet MKMapView *Map;

+(NSArray *)getStations;
+(void)setStations:(NSMutableArray *)Array;
@end
