//
//  MasroufakViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "MBProgressHUD.h"
#import "MasroufakViewController.h"
#import <EventKit/EventKit.h>
#import "MappViewController.h"
#define tripchi @"http://www.hodico.com/mAdmin/prices_getdata.php?trip=1"


@interface MasroufakViewController ()



@end

@implementation MasroufakViewController
{
    NSMutableDictionary *Data;
    MBProgressHUD *HUD;
    NSInteger SelectedItemPrice;
}
@synthesize Start,End,Average;
@synthesize Picker;
static CLLocation *SLoc;
static CLLocation *ELoc;
static bool setS;
static bool setE;

+(void)removeStart
{
    setS = FALSE;
}
+(void)removeEnd
{
    setE = NO;
}
+(void)setStart:(CLLocation *)Loc
{
    setS = YES;
    SLoc = Loc;
}
+(void)setEnd:(CLLocation *)Loc
{
    setE = YES;
    ELoc = Loc;
}

- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (IBAction)Start:(id)sender {
    UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
    MappViewController *demoController= (MappViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"Mapp"];
    demoController.End = FALSE;
    [self.navigationController pushViewController:demoController animated:YES];
}
- (IBAction)End:(id)sender {
    UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main" bundle: nil];
    MappViewController *demoController= (MappViewController*)[mainStoryboard instantiateViewControllerWithIdentifier: @"Mapp"];
    demoController.End = true;
    [self.navigationController pushViewController:demoController animated:YES];
    
}

- (IBAction)Submit:(id)sender {
    
    CLLocationDistance distance = [ELoc distanceFromLocation:SLoc];
    NSInteger AvDistanceper20L = [[Average text] integerValue] ;
    float Liters = ((distance/1000) / (AvDistanceper20L/20));
    int Price = (int)((Liters/20) * SelectedItemPrice);
    
    if (Liters <= 0 || Price <= 0 ) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning" message:@"Please fill all info" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:Nil, nil] ;
        alert.tag = 100;
        [alert show];
    }
    
    else
    {
        NSInteger integerValue=Price;
        NSNumberFormatter *numberFormatterComma = [NSNumberFormatter new];
        [numberFormatterComma setNumberStyle:NSNumberFormatterDecimalStyle];
        NSString *formatted = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];

        
        UIAlertView *aler = [[UIAlertView alloc] initWithTitle:@"Trip cost" message:[NSString stringWithFormat:@"You will need\n %.2f Liters for %@ L.L",Liters,formatted] delegate:nil cancelButtonTitle:@"Dismiss" otherButtonTitles:nil, nil];
        [aler show];
    }
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
    NSOperationQueue* queue = [[NSOperationQueue alloc] init];
    
    
    
    NSURL *URL = [NSURL URLWithString:tripchi];
    
    
    NSMutableURLRequest* FurlRequest = [NSURLRequest requestWithURL:URL  cachePolicy:NSURLCacheStorageNotAllowed timeoutInterval:10];
    //[urlRequest addValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    
    [NSURLConnection sendAsynchronousRequest:FurlRequest
                                       queue:queue
                           completionHandler:^(NSURLResponse* response,
                                               NSData* data,
                                               NSError* error)
     {
         
         NSLog(@"error: %@", error);
         if (error) {
             [self ExecuteError:error];
         }
         else
         {
             id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
             if (jsonObject) {
                 dispatch_async(dispatch_get_main_queue(), ^{
                     [HUD hide:YES];
                     NSLog(@"Standings jsonObject: %@", jsonObject);
                     if ([jsonObject isKindOfClass:[NSArray class]]) {
                         Data = [[NSMutableDictionary alloc] init];
                         for (NSMutableDictionary *tmp in jsonObject) {
                             [Data setObject:[tmp objectForKey:@"pp_product_price"]  forKey:[tmp objectForKey:@"product_description"]];
                         }
                         
                         NSArray *Keys = [Data allKeys];
                         SelectedItemPrice = [[Data objectForKey:Keys[0]] integerValue];
                         [Picker reloadAllComponents];
                     }
                 });
             }
             else
             {
                 [self ExecuteError:error];
             }
         }
     }];
    
}
-(void)viewDidAppear:(BOOL)animated
{
    if (setE) {
        [End setBackgroundColor:[UIColor lightGrayColor]];
        [End setText:@"End Chosen"];
        
    }
    else{
        [End setBackgroundColor:[UIColor whiteColor]];
        [End setText:@"Click Here to Choose"];
        
    }
    if (setS) {
        [Start setBackgroundColor:[UIColor lightGrayColor]];
        [Start setText:@"Start Chosen"];
        
    }
    else
    {
        [Start setBackgroundColor:[UIColor whiteColor]];
        [Start setText:@"Click Here to Choose"];
    }
    if (Data == NULL) {
        [self StartLoading];
    }
}
-(void)ExecuteError:(NSError*)Error
{
    [HUD hide:YES];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving data" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:@"Try again", nil];
    [alert show];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(dismissKeyboard)];
    
    [self.view addGestureRecognizer:tap];
    Average.returnKeyType = UIReturnKeyDone;
    [Average setDelegate:self];
    [Picker setDelegate:self];
    [Picker setDataSource:self];
}
-(void)doneButton:(id)sender
{
    
}
-(void)viewWillAppear:(BOOL)animated
{
    //    [[NSNotificationCenter defaultCenter] removeObserver:self];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)dismissKeyboard {
    [Average resignFirstResponder];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}



#pragma mark -
#pragma mark PickerView DataSource
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
numberOfRowsInComponent:(NSInteger)component
{
    return [[Data allKeys] count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    NSArray *Keys = [Data allKeys];
    return Keys[row];
}



#pragma mark -
#pragma mark PickerView Delegate
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component
{
    
    NSArray *Keys = [Data allKeys];
    SelectedItemPrice = [[Data objectForKey:Keys[row]] integerValue];
    
}


@end
