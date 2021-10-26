//
//  DisplayMap.m
//  test
//
//  Created by MM Dev 01 on 1/14/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "DisplayMap.h"

@implementation DisplayMap

@synthesize subtitle,title,coordinate;


- (id)initWithName:(NSString*)namee address:(NSString*)addresss coordinate:(CLLocationCoordinate2D)coordinatee
{
    self = [super init];
    if (self) {
        title = namee;
        subtitle = addresss;
        coordinate = coordinatee;
    }
    return self;
}


@end
