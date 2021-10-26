//
//  CommentsViewController.m
//  test
//
//  Created by MM Dev 01 on 2/1/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "CommentsViewController.h"
#import "StationsViewController.h"


@interface CommentsViewController ()
{
    NSArray *Subjects;
    NSMutableArray *Ratings;
    NSMutableData *receivedData;
    
    int NumberOfItems ;
    
    MBProgressHUD *HUD;
    
    
    id jsonArray;
    
    
    UIActionSheet *actionSheet;
    
    
    CLLocationManager *locationManager;
    
    float CurrentLat;
    float CurrentLong;
    
    NSOperationQueue* queue;
}

@end

@implementation CommentsViewController
@synthesize email;
@synthesize phone;
@synthesize Table;
@synthesize Station;

- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (IBAction)Submit:(id)sender {
    [self dismissKeyboard];
    if ([email.text isEqualToString:@""] || [phone.text isEqualToString:@""]) {
        [[[UIAlertView alloc] initWithTitle:@"Warning" message:@"Please fill all info" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:Nil, nil] show];
    }
    else
    {
            [self StartUpload];
    }
    
}

-(void)viewDidAppear:(BOOL)animated
{
    jsonArray = [[NSMutableArray alloc] init];
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:HUD];
    HUD.delegate = self;
    HUD.labelText = @"Downloading";
    [HUD show:YES];
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, 0.01 * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        [self DownloadData];
    });
    
}
- (IBAction)ShowPickerButton:(id)sender {
    [self showPickerWithTag:2];
}
-(void)StartUpload
{
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:HUD];
    HUD.delegate = self;
    
    HUD.labelText = @"Submitting";
    
    [HUD show:YES];
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, 0.01 * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        [self UploadData];
    });
    
}
-(void)DownloadData
{
    receivedData = [[NSMutableData alloc] init];
    
    NSURL *URl = [NSURL URLWithString:@"http://www.hodico.com/mAdmin/stations_getdata.php"];
    
    NSMutableURLRequest* FurlRequest = [NSURLRequest requestWithURL:URl  cachePolicy:NSURLCacheStorageNotAllowed timeoutInterval:10];
    [NSURLConnection sendAsynchronousRequest:FurlRequest
                                       queue:queue
                           completionHandler:^(NSURLResponse* response,
                                               NSData* data,
                                               NSError* error)
     {
         
         NSLog(@"error: %@", error);
         if (error) {
             [self ExecuteError:error :6];
         }
         else
         {
             id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
             if (jsonObject) {
                 dispatch_async(dispatch_get_main_queue(), ^{
                     [HUD hide:YES];
                     if ([jsonObject isKindOfClass:[NSArray class]]) {
                         jsonArray = jsonObject;
                         NumberOfItems = 0 ;
                         
                         CLLocation *currentLoc = [[CLLocation alloc] initWithLatitude:CurrentLat longitude:CurrentLong];
                         float MinMeters = 0;
                         int Index=0;
                         
                         float LatA = [[[jsonArray objectAtIndex:0]objectForKey:@"station_lat"]floatValue];
                         float  LongA= [[[jsonArray objectAtIndex:0]objectForKey:@"station_long"]floatValue];
                         
                         CLLocation *StationLocA = [[CLLocation alloc] initWithLatitude:LatA longitude:LongA];
                         MinMeters = [StationLocA distanceFromLocation:currentLoc];
                         
                         
                         for(int i = 1 ; i < [jsonArray count];i++) {
                             float Lat = [[[jsonArray objectAtIndex:i]objectForKey:@"station_lat"]floatValue];
                             float  Long= [[[jsonArray objectAtIndex:i]objectForKey:@"station_long"]floatValue];
                             CLLocation *StationLoc = [[CLLocation alloc] initWithLatitude:Lat longitude:Long];
                             CLLocationDistance meters = [StationLoc distanceFromLocation:currentLoc];
                             if (meters < MinMeters) {
                                 Index = i;
                             }
                         }
                         Station.text = [[jsonArray objectAtIndex:Index]objectForKey:@"station_name"];
                         
                     }
                 });
             }
             else
             {
                 [self ExecuteError:error:6];
             }
         }
     }];
}
-(void)UploadData
{
    //$_GET['phone'] , $_GET['email'] , $_GET['clean'] ,$_GET['recp'] ,$_GET['serv'],$_GET['disc'],$_GET['avail'],$_GET['cond']);
    
    
    NSString *URLL = [NSString stringWithFormat:@"http://www.hodico.com/mAdmin/feedback_getdata.php?phone=%@&email=%@&clean=%lu&recp=%lu&serv=%lu&disc=%lu&avail=%lu&cond=%lu"
                      ,phone.text
                      ,email.text
                      ,(unsigned long)[[Ratings objectAtIndex:0] rating]
                      ,(unsigned long)[[Ratings objectAtIndex:1] rating]
                      ,(unsigned long)[[Ratings objectAtIndex:2] rating]
                      ,(unsigned long)[[Ratings objectAtIndex:3] rating]
                      ,(unsigned long)[[Ratings objectAtIndex:4] rating]
                      ,(unsigned long)[[Ratings objectAtIndex:5] rating]];
    NSURL *url = [NSURL URLWithString:URLL];
    
    NSMutableURLRequest* FurlRequest = [NSURLRequest requestWithURL:url  cachePolicy:NSURLCacheStorageNotAllowed timeoutInterval:10];

    [NSURLConnection sendAsynchronousRequest:FurlRequest
                                       queue:queue
                           completionHandler:^(NSURLResponse* response,
                                               NSData* data,
                                               NSError* error)
     {
         
         NSLog(@"error: %@", error);
         if (error) {
             [self ExecuteError:error :5];
         }
         else
         {
             dispatch_async(dispatch_get_main_queue(), ^{
                 [HUD hide:YES];
                 UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Success" message:@"Thank you, your review has been submitted" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:Nil, nil];
                 alert.tag = 90;
                 [alert show];
                 
             });
         }
     }];
}
- (void)ExecuteError:(NSError*)error :(NSInteger)tag
{
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:nil, nil];
    alert.tag = 91;
    [alert show];
}


-(void)CantDownload
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:@"Try again", nil];
    [alert show];
}
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 5) {
        switch (buttonIndex) {
            case 0:
            {
                [self.navigationController popViewControllerAnimated:YES];
            }
                break;
            case 1:
            {
                [self StartUpload];
            }
                
            default:
                break;
        }
    }else
    {
        NSInteger A = alertView.tag;
        if (A == 90) {
            switch (buttonIndex) {
                case 0:
                {
                }
                    break;
                case 1:
                {
                    [self StartUpload];
                }
                    break;
                    
                default:
                    break;
            }
            
        }
        
    }
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    //    Subjects = [NSArray arrayWithObjects:@"nazafe",
    //                @"istikbal",
    //                @"khidma",
    //                @"tawafour el khdma",
    //                @"khousoumat",
    //                @"7alat al bida3a", nil];
    queue = [[NSOperationQueue alloc] init];
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(dismissKeyboard)];
    
    [self.view addGestureRecognizer:tap];
    Ratings = [[NSMutableArray alloc] init];
    Subjects = [NSArray arrayWithObjects:@"نظافة",
                @"إستقبال",
                @"خدمة",
                @"توفر الخدمة",
                @"حسومات",
                @"حالة البضاعة", nil];
    receivedData = [[NSMutableData alloc] init];
    [email setDelegate:self];
    [phone setDelegate:self];
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    [locationManager startUpdatingLocation];
}
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    
    NSLog(@"newLocation lat%f - lon%f", newLocation.coordinate.latitude, newLocation.coordinate.longitude);
    
}
- (void)locationManager:(CLLocationManager *)manager
     didUpdateLocations:(NSArray *)locations {
    CLLocation *location = [locations lastObject];
    NSLog(@"lat%f - lon%f", location.coordinate.latitude, location.coordinate.longitude);
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [Subjects count];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *CellIdentifier = @"CELL";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    if (cell==nil) {
        cell = [[UITableViewCell alloc]init];
    }
    UILabel *Label = (UILabel *)[cell  viewWithTag:100];
    StarRatingControl *Rating = (StarRatingControl *)[cell viewWithTag:200];
    [Ratings addObject:Rating];
    [Label setText:[Subjects objectAtIndex:indexPath.row]];
    Rating.Name = Label.text;
    return cell;
}
-(void)dismissKeyboard {
    [email resignFirstResponder];
    [phone resignFirstResponder];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

#pragma pickeer View
-(void)showPickerWithTag:(NSInteger)Tag
{
    if (Tag > 0 && Tag < 4)
    {
        UIAlertController* selectBox = [UIAlertController alertControllerWithTitle:nil
                                                                           message: nil
                                                                    preferredStyle:UIAlertControllerStyleActionSheet];
        
        for (NSDictionary *item in jsonArray)
        {
            UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:item[@"station_name"]
                                                                    style:UIAlertActionStyleDefault
                                                                  handler:^(UIAlertAction * action){
                                                                      [self didSelectRowInAlertControler:item:Tag+10];
                                                                  }];
            [selectBox addAction:defaultAction];
        }
        
        [self presentViewController:selectBox animated:YES completion:nil];
        
        /* first create a UIActionSheet, where you define a title, delegate and a button to close the sheet again
        actionSheet = [[UIActionSheet alloc] initWithTitle:Nil delegate:self cancelButtonTitle:@"Done" destructiveButtonTitle:nil otherButtonTitles:nil];
        
        I always give my controls a tag, to make sure I can work with multiple actionsheets in one View (to identify them when an event triggers
        actionSheet.tag = Tag;
        actionSheet.actionSheetStyle = UIActionSheetStyleDefault;
        
        Initialize a UIPickerView with 100px space above it, for the button of the UIActionSheet.
        UIPickerView* positionPicker = [[UIPickerView alloc] initWithFrame:CGRectMake(0,30, 320, 200)];
        positionPicker.dataSource = self;
        positionPicker.delegate = self;
        [positionPicker setBackgroundColor:[UIColor lightGrayColor]];
        [actionSheet setBackgroundColor:[UIColor lightGrayColor]];
        another unique tag for this UIPicker
        positionPicker.tag = Tag+10;
        
        Add the UIPickerView to the UIActionSheet
        [actionSheet addSubview:positionPicker];
        
        Select the previous selected value, which for me is stored in 'currentPosition'
        [positionPicker selectRow:1 inComponent:0 animated:NO];
        
        Add the UIActionSheet to the view
        [actionSheet showInView:self.view];
        
        Make sure the UIActionSheet is big enough to fit your UIPickerView and it's buttons
        [actionSheet setBounds:CGRectMake(0,0, 320, 250)];
        
        clean up */
    }
}

-(void)didSelectRowInAlertControler:(NSDictionary*) row:(NSInteger) Tag
{
    Station.text = row[@"station_name"];
}

- (void)willPresentActionSheet:(UIActionSheet *)actionSheet
{
    UIColor *customTitleColor = [UIColor blueColor];
    for (UIView *subview in actionSheet.subviews) {
        if ([subview isKindOfClass:[UIButton class]]) {
            UIButton *button = (UIButton *)subview;
            
            [button setTitleColor:customTitleColor forState:UIControlStateHighlighted];
            [button setTitleColor:customTitleColor forState:UIControlStateNormal];
            [button setTitleColor:customTitleColor forState:UIControlStateSelected];
            [button setBackgroundColor:[UIColor lightGrayColor]];
        }
    }
}
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [jsonArray count];
}
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (actionSheet.tag == 1)
    {
        /* Do stuff here, only intended for the right UIActionSheet. This only applies for when you use multiple UIActionSheets on the same ViewController. Closing it is unnecessary, because you already specified a destructive button. */
    }
}
- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    
    return [[jsonArray objectAtIndex:row]objectForKey:@"station_name"];
}
#pragma mark -
#pragma mark PickerView Delegate
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component
{
    
    Station.text = [[jsonArray objectAtIndex:row]objectForKey:@"station_name"];
    
}
@end
