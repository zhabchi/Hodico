//
//  StationMapViewController.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StationMapViewController : UIViewController <UITableViewDataSource,UITableViewDelegate>
{
    NSMutableArray *contentList;
    NSMutableArray *subcontentList;
    NSMutableArray *filteredContentList;
    NSMutableArray *filteredSubContentList;
BOOL isSearching;
}
@property (strong, nonatomic) IBOutlet UISearchBar *SearchBar;
@property (strong, nonatomic) IBOutlet UITableView *tblContentList;

@end
