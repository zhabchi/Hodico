//
//  DeliveryViewController.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"

@interface DeliveryViewController : UIViewController <MBProgressHUDDelegate,UIPickerViewDataSource,UIPickerViewDelegate,UITextFieldDelegate>

@property (strong, nonatomic) IBOutlet UIPickerView *Picker;
@property (strong, nonatomic) IBOutlet UITextField *Quantity;
@property (strong, nonatomic) IBOutlet UILabel *Price;
@property (strong, nonatomic) IBOutlet UITextField *Phone;
@property (strong, nonatomic) IBOutlet UITextField *Email;
@property (strong, nonatomic) IBOutlet UILabel *PriceD;

+(NSMutableArray *)PricesArray;

@end
