//
//  TaxViewController.h
//  test
//
//  Created by MM Dev 01 on 2/23/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "MBProgressHUD.h"

@interface TaxViewController : UIViewController <UIPickerViewDelegate,UIPickerViewDataSource,MBProgressHUDDelegate,UIAlertViewDelegate,NSURLConnectionDelegate,UITextFieldDelegate,UIActionSheetDelegate>

@property (strong, nonatomic) IBOutlet UITextField *Type;
@property (strong, nonatomic) IBOutlet UITextField *Year;
@property (strong, nonatomic) IBOutlet UITextField *HPower;
@property (strong, nonatomic) IBOutlet UITextField *Symbol;
@property (strong, nonatomic) IBOutlet UITextField *NUMBER;
@property (strong, nonatomic) IBOutlet UIImageView *hImage;
@property (strong, nonatomic) IBOutlet UITextField *hText;
@property (strong, nonatomic) IBOutlet UIButton *hButton;
@property (strong, nonatomic) IBOutlet UILabel *hLabel;

@end
