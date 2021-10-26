//
//  PricesViewController.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "MBProgressHUD.h"

@interface PricesViewController : UIViewController <MBProgressHUDDelegate,UITableViewDataSource,UITableViewDelegate>
@property (strong, nonatomic) IBOutlet UILabel *Unleaded;
@property (strong, nonatomic) IBOutlet UILabel *Super;
@property (strong, nonatomic) IBOutlet UILabel *Deisel;
@property (strong, nonatomic) IBOutlet UITableView *Table;

@end
