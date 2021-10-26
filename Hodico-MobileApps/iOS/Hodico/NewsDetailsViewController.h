//
//  NewsDetailsViewController.h
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CustomUIImageView.h"


@interface NewsDetailsViewController : UIViewController
{
    NSDictionary *News;
}
@property (strong, nonatomic) IBOutlet CustomUIImageView *Image;

@property (strong, nonatomic) IBOutlet UILabel *Text;
-(void)setNewsDictionary:(NSDictionary *)Dictionnary;
@end
