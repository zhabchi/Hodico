//
//  NewsViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "NewsViewController.h"
#import "NewsDetailsViewController.h"
#import "CustomUIImageView.h"

#define url @"http://www.hodico.com/mAdmin/news_getdata.php"

@interface NewsViewController ()
{
    int NumberOfItems ;
    NSMutableArray *jsonArray;
    MBProgressHUD *HUD;
    NSMutableData *receivedData;
}

@end

@implementation NewsViewController
@synthesize Table;

- (IBAction)BackButton:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)viewDidAppear:(BOOL)animated
{
    jsonArray = [[NSMutableArray alloc] init];
    [self StartLoading];
}

-(void)StartLoading
{
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:HUD];
    HUD.delegate = self;
    HUD.labelText = @"Loading";
    [HUD show:YES];
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, 0.01 * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        [self DownloadData];
    });
    
}
-(void)DownloadData
{
    
    NSURL *URl = [NSURL URLWithString:url];
    NSMutableURLRequest *Request = [NSMutableURLRequest requestWithURL:URl
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:5.0];
    [Request addValue:@"" forHTTPHeaderField:@"Accept-Encoding"];
    NSURLConnection *connection = [NSURLConnection connectionWithRequest:Request delegate:self];
    
    [connection start];
    
}

- (void)connection:(NSURLConnection*)connection didFailWithError:(NSError*)error
{
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self CantDownload];
}


- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    [receivedData setLength:0];
}

-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    [receivedData appendData:data];
}
- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    NSLog(@"Did Load");
    [self GetNewNews];
}

-(void)GetNewNews
{
    
    NSError *e = nil;
    
    
    jsonArray = [NSJSONSerialization JSONObjectWithData:receivedData options:NSJSONReadingAllowFragments
                                                  error:&e];
    NSLog(@"Error : %@", e);
    
    
    NumberOfItems = 0 ;
    if (!jsonArray) {
        NSLog(@"Error parsing JSON: %@", e);
        //do stuff
        dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
            // Do something...
            dispatch_async(dispatch_get_main_queue(), ^{
                [MBProgressHUD hideHUDForView:self.view animated:YES];
            });
        });
        [self CantDownload];
        
    } else {
        for(NSDictionary *item in jsonArray) {
            NSLog(@"Item: %@", item);
            NumberOfItems++;
        }
        
        [Table reloadData];
    }
}
-(void)CantDownload
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:@"Try again", nil];
    [alert show];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    jsonArray  = [[NSMutableArray alloc]init];
    receivedData = [[NSMutableData alloc] init];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return NumberOfItems;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    NSDictionary *Item = [jsonArray objectAtIndex:indexPath.row];
    
    UITableViewCell *Cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    if (Cell == NULL) {
        
        Cell = [[UITableViewCell alloc]init];
    }
    
    UILabel *Title = (UILabel *)[Cell viewWithTag:501];
    [Title setText:[Item objectForKey:@"news_title"] ];
    
    UILabel *SubtitleTitle = (UILabel *)[Cell viewWithTag:502];
    [SubtitleTitle setText:[Item objectForKey:@"news_data"] ];
    
    //= [Item objectForKey:@"news_Image_path"];
    CustomUIImageView *Image = (CustomUIImageView *)[Cell viewWithTag:500];
    
    
    id displayNameTypeValue = [Item objectForKey:@"news_Image_path"];
    NSString *ImagePath = @"";
    if (displayNameTypeValue != [NSNull null])
        ImagePath = (NSString *)displayNameTypeValue;
    
    
    
    
    NSRange tmp = [ImagePath rangeOfString:[NSString stringWithFormat:@"%c",'/']];
    if (tmp.location != NSNotFound)
    {
        [Image loadWithURL:ImagePath];
    }
    
    return Cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *URLS = [[jsonArray objectAtIndex:indexPath.row] objectForKey:@"news_Link"];
    if ([URLS hasPrefix:@"http"]) {
        
        NSURL *SelectedURL = [NSURL URLWithString:URLS];
        [[UIApplication sharedApplication] openURL:SelectedURL];
    }
}
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex) {
        case 0:
        {
            [self.navigationController popViewControllerAnimated:YES];
        }
            break;
        case 1:
        {
            [self StartLoading];
        }
            break;
            
        default:
            break;
    }
}





@end
