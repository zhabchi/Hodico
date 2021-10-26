//
//  SplashScreenViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "SplashScreenViewController.h"
#import "HomeViewController.h"
#import "AppDelegate.h"


@interface SplashScreenViewController ()
{
    int NumberOfItems ;
    NSMutableArray *jsonArray;
}
@end

@implementation SplashScreenViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (IBAction)StartButton:(id)sender {
    UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        HomeViewController *demoController= (HomeViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"Home"];
    
    
    [self.navigationController popViewControllerAnimated:YES];
    [self.navigationController pushViewController:demoController animated:YES];
    
}
-(void)viewDidLoad
{
    [super viewDidLoad];
}
-(void)viewDidAppear:(BOOL)animated
{
    jsonArray  = [[NSMutableArray alloc]init];
//    UIDevice *device = [UIDevice currentDevice];
//    if ([[device model] isEqualToString:@"iPhone"] ) {
//        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"tel:130-032-2837"]]];
//    } else {
//        UIAlertView *notPermitted=[[UIAlertView alloc] initWithTitle:@"Alert" message:@"Your device doesn't support this feature." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
//        [notPermitted show];
//    }
    [NSThread detachNewThreadSelector:@selector(GetNewNews) toTarget:self withObject:nil];
    
}


-(void)GetNewNews
{
    
    NSError *e = nil;
    NSData *data = [NSData dataWithContentsOfURL:
                    [NSURL URLWithString:@"http://www.hodico.com/mAdmin/tips_getdata.php"]];
    

    if (data == nil) {
        
    }
    else
    {
        jsonArray = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments
                                                      error:&e];
        
        if (e != nil) {
            NSLog(@"Error : %@", e);

        }
        else
        {
            
        }
        
        NumberOfItems = 0 ;
        if (!jsonArray) {
            NSLog(@"Error parsing JSON: %@", e);
            [self CantDownload];
            
        } else {
            for(NSDictionary *item in jsonArray) {
                NSLog(@"Item: %@", item);
                NumberOfItems++;
            }
            
            [[[[self.tabBarController tabBar]items]objectAtIndex:1]setEnabled:TRUE];
        }
    }

}
-(void)CantDownload
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Continue" otherButtonTitles:@"Try again", nil];
    [alert show];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event
{
    if(motion == UIEventSubtypeMotionShake)
    {
        if (jsonArray == nil) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Server cannot be contacted" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:nil, nil];
            [alert show];
        }
        else
        {
            NSDictionary *Item = [jsonArray objectAtIndex:0];
            NSString *tip_Desc = [Item objectForKey:@"tip_Desc"];
            NSLog(@"DID SHAKE DEVICE");
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Tip of the day" message:tip_Desc delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            alert.tag = 1;
            [alert show];
        }
    }
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex  // after animation
{
    if (alertView.tag == 1) {
        UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
        HomeViewController *demoController= (HomeViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"Home"];
        
        
        [self.navigationController popViewControllerAnimated:YES];
        [self.navigationController pushViewController:demoController animated:YES];
    }
}
@end
