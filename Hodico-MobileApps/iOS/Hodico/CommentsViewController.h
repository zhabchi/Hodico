//
//  CommentsViewController.h
//  test
//
//  Created by MM Dev 01 on 2/1/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "ViewController.h"
#import "StarRatingControl.h"
#import "MBProgressHUD.h"

#import <MapKit/MapKit.h>

@interface CommentsViewController : ViewController <UITableViewDelegate,UITableViewDataSource,MBProgressHUDDelegate,UITextFieldDelegate,UIPickerViewDataSource,UIPickerViewDelegate,CLLocationManagerDelegate,UIActionSheetDelegate>
@property (strong, nonatomic) IBOutlet UITableView *Table;
@property (strong, nonatomic) IBOutlet UITextField *email;
@property (strong, nonatomic) IBOutlet UITextField *phone;
@property (strong, nonatomic) IBOutlet UITextField *Station;

@end
