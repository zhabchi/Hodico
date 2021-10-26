//
//  NewsDetailsViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "NewsDetailsViewController.h"

@interface NewsDetailsViewController ()

@end

@implementation NewsDetailsViewController

@synthesize Image;
@synthesize Text;
-(void)setNewsDictionary:(NSDictionary *)Dictionnary
{
    News = Dictionnary;
}
- (IBAction)BackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    [Text setText:[News objectForKey:@"news_title"]];
    
    [Image loadWithURL:[News objectForKey:@"news_Image_path"]];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
