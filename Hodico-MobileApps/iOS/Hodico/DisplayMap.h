//
//  DisplayMap.h
//  test
//
//  Created by MM Dev 01 on 1/14/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MKAnnotation.h>
@interface DisplayMap : NSObject <MKAnnotation>

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;




- (id)initWithName:(NSString*)namee address:(NSString*)addresss coordinate:(CLLocationCoordinate2D)coordinatee;



@end