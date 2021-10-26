//
//  PricesViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "PricesViewController.h"
#import "DeliveryViewController.h"

@interface PricesViewController ()
{
    NSMutableArray *jsonArray;
    MBProgressHUD *HUD;
    
    NSString *DateS;
}

@end


static NSMutableArray *PricesArray;
@implementation PricesViewController

@synthesize Unleaded,Super,Deisel;
@synthesize Table;
- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:nil];
}
-(void)viewDidAppear:(BOOL)animated
{
//    if ([DeliveryViewController PricesArray] == NULL) {
//        
        [self StartLoading];
//    }
//    else
//    {
//        jsonArray = [DeliveryViewController PricesArray];
//    }

}

-(void)StartLoading
{
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:HUD];
    HUD.delegate = self;
    HUD.labelText = @"Loading";
    [HUD show:YES];
    
    NSURL *URl = [NSURL URLWithString:@"http://www.hodico.com/mAdmin/prices_getdata.php"];
    
    NSMutableURLRequest *Request = [NSMutableURLRequest requestWithURL:URl
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:5.0];
    
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];
    [Request addValue:@"" forHTTPHeaderField:@"Accept-Encoding"];
    
    
    [NSURLConnection sendAsynchronousRequest:Request
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
                               if ([data length] > 0 && error == nil)
                               {
                                   NSError *e = nil;
                                   jsonArray = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments
                                                                                 error:&e];
                                   //NSLog(@"Error : %@", e);
                                   PricesArray = jsonArray;
                                   [Table reloadData];
                                   
                                   [MBProgressHUD hideHUDForView:self.view animated:YES];
                               }
                               else if ([data length] == 0 && error == nil)
                               {
                                   NSLog(@"Nothing was downloaded.");
                                   [MBProgressHUD hideHUDForView:self.view animated:YES];
                                   UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Server cannot be contacted" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:nil, nil];
                                   [alert show];
                               }
                               else if (error != nil){
                                   NSLog(@"Error = %@", error);
                                   [MBProgressHUD hideHUDForView:self.view animated:YES];
                                   UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Server cannot be contacted" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:nil, nil];
                                   [alert show];
                               }
                               
                           }];
    
    
    
    
    NSURL *URlDate = [NSURL URLWithString:@"http://www.hodico.com/mAdmin/prices_getdata.php?date=true"];
    
    NSMutableURLRequest* FurlRequest = [NSURLRequest requestWithURL:URlDate  cachePolicy:NSURLCacheStorageNotAllowed timeoutInterval:10];
    [NSURLConnection sendAsynchronousRequest:FurlRequest
                                       queue:queue
                           completionHandler:^(NSURLResponse* response,
                                               NSData* data,
                                               NSError* error)
     {
         
         NSLog(@"error: %@", error);
         if (error) {
         }
         else
         {
             id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
             if (jsonObject) {
                 dispatch_async(dispatch_get_main_queue(), ^{
                     if ([jsonObject isKindOfClass:[NSArray class]]) {
                         DateS = [[jsonObject objectAtIndex:0] objectForKey:@"Date"];
                         [Table reloadData];
                     }
                 });
             }
             else
             {
                 UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Server cannot be contacted" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:nil, nil];
                 [alert show];

             }
         }
     }];
    
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return ([PricesArray count] == 0 ? 0 : [PricesArray count]+1);
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 50;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == [PricesArray count]) {
        static NSString *CellIdentifier = @"DateCell";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        }
        UILabel *Date = (UILabel *)[cell viewWithTag:100];
        
        
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
        
        NSDate *date = [dateFormatter dateFromString:DateS];
        [dateFormatter setDateFormat:@"EE dd/MM/yyyy"];
        
        
        NSString *stringFromDate = [dateFormatter stringFromDate:date];
        
        
        [Date setText:stringFromDate];
        return cell;
    }
    
    static NSString *CellIdentifier = @"PricesCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    UILabel *price = (UILabel*)[cell viewWithTag:11];
    [self setPrice:[[[PricesArray objectAtIndex:indexPath.row] objectForKey:@"pp_product_price"] integerValue] inLabel:price inRow:indexPath.row];
    
    
    
    UILabel *Product = (UILabel*)[cell viewWithTag:12];
    [Product setText:[[PricesArray objectAtIndex:indexPath.row] objectForKey:@"product_description"]];
    return cell;
    
}
-(void)setPrice:(NSInteger)Price inLabel:(UILabel *)Label inRow:(NSInteger)row
{
    //Label.textAlignment = NSTextAlignmentCenter;
    Label.textAlignment = NSTextAlignmentRight;
    
    NSString *txt = [NSString stringWithFormat:@"%lD",(long)Price];
    if (row == 5) {
        txt = [NSString stringWithFormat:@"%@  $",txt];
    }
    else
    {
        txt = [NSString stringWithFormat:@"%@ L.L",txt];
    }
    Label.text = txt;
    
    Label.font = [UIFont fontWithName:@"Digital-7" size:30];
    Label.numberOfLines = 1;
    Label.minimumScaleFactor = 8./Label.font.pointSize;
    Label.adjustsFontSizeToFitWidth = YES;
    [Label setTextColor:[UIColor yellowColor]];
    [Label setBackgroundColor:[UIColor blackColor]];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view
    jsonArray = [[NSMutableArray alloc] init];
    
    
    NSDate *Now = [NSDate date];
    NSDate *Previous = [[NSUserDefaults standardUserDefaults] objectForKey:@"PreviousDate"];
    if(Previous == NULL)
    {
        [self updateFuelPrices];
    }
    else
    {
        NSCalendar* calender = [NSCalendar currentCalendar];
        
        NSDateComponents *NowDate = [calender components:NSWeekOfYearCalendarUnit fromDate:[NSDate date]];
        NSLog(@"Now: %@",NowDate);
        NSDateComponents *PriviousDate = [calender components:NSWeekdayCalendarUnit fromDate:[NSDate date]];
        NSLog(@"Now: %@",NowDate);
        
        
        NSInteger *nbofDays = [self daysBetweenDate:Previous andDate:Now];
        
        
    }
    
    
    
    
    //
    //    NSDateComponents *PreviousDate = [[NSCalendar currentCalendar] components:NSWeekdayCalendarUnit fromDate:Previous];
    //    NSLog(@"Now: %@",PreviousDate);
    //
    
    
}
-(void)CantDownload
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:@"Try again", nil];
    [alert show];
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



- (NSInteger)daysBetweenDate:(NSDate*)fromDateTime andDate:(NSDate*)toDateTime
{
    NSDate *fromDate;
    NSDate *toDate;
    
    NSCalendar *calendar = [NSCalendar currentCalendar];
    
    [calendar rangeOfUnit:NSDayCalendarUnit startDate:&fromDate
                 interval:NULL forDate:fromDateTime];
    [calendar rangeOfUnit:NSDayCalendarUnit startDate:&toDate
                 interval:NULL forDate:toDateTime];
    
    NSDateComponents *difference = [calendar components:NSDayCalendarUnit
                                               fromDate:fromDate toDate:toDate options:0];
    
    return [difference day];
}
-(void)updateFuelPrices
{
    
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
