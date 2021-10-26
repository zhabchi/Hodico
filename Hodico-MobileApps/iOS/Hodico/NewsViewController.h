//
//  NewsViewController.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "MBProgressHUD.h"

@interface NewsViewController : UIViewController <UITableViewDataSource,UITableViewDelegate,MBProgressHUDDelegate,UIAlertViewDelegate,NSURLConnectionDelegate>
{
    UIView *LoadingAnimation;
}
@property (strong, nonatomic) IBOutlet UITableView *Table;


@end
