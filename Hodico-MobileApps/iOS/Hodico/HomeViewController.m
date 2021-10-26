//
//  HomeViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "HomeViewController.h"
#import "AppDelegate.h"


@interface HomeViewController ()

@end

@implementation HomeViewController
- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.

  
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}




- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)Prices:(id)sender {
}
- (IBAction)Stations:(id)sender {
}
- (IBAction)Cars:(id)sender {
}
- (IBAction)Delivery:(id)sender {
}
- (IBAction)Comments:(id)sender {
}
- (IBAction)News:(id)sender {
}
- (IBAction)Calls:(id)sender {
}

- (void) mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error
{
    switch (result)
    {
        case MFMailComposeResultCancelled:
            NSLog(@"Mail cancelled");
            break;
        case MFMailComposeResultSaved:
            NSLog(@"Mail saved");
            break;
        case MFMailComposeResultSent:
            NSLog(@"Mail sent");
            break;
        case MFMailComposeResultFailed:
            NSLog(@"Mail sent failure: %@", [error localizedDescription]);
            break;
        default:
            break;
    }
    
    // Close the Mail Interface
    [self dismissViewControllerAnimated:YES completion:NULL];
}


@end
