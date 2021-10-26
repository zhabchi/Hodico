//
//  MappViewController.h
//  test
//
//  Created by MM Dev 01 on 3/6/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <MapKit/MapKit.h>

@interface MappViewController : UIViewController <MKMapViewDelegate>
@property (strong, nonatomic) IBOutlet UIButton *Save;
@property (strong, nonatomic) IBOutlet MKMapView *Map;

@property (nonatomic) BOOL End;


@end
