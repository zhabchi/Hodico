//
//  MasroufakViewController.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <MapKit/MapKit.h>
@interface MasroufakViewController : UIViewController <UITextFieldDelegate,UIPickerViewDataSource,UIPickerViewDelegate>
@property (strong, nonatomic) IBOutlet UITextField *Average;
@property (strong, nonatomic) IBOutlet UITextField *Start;
@property (strong, nonatomic) IBOutlet UITextField *End;
@property (strong, nonatomic) IBOutlet UIPickerView *Picker;

+(void)setStart:(CLLocation *)Loc;
+(void)setEnd:(CLLocation *)Loc;
+(void)removeStart;
+(void)removeEnd;

@end
