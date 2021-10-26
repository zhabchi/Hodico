//
//  DeliveryViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "DeliveryViewController.h"
#define urll @"http://www.hodico.com/mAdmin/prices_getdata.php?delivered=true"

@interface DeliveryViewController ()
{
    int NumberOfItems ;
    NSMutableArray *jsonArray;
    MBProgressHUD *HUD;
    NSMutableDictionary *SelectedItem;
    NSInteger SelectedItemPrice;
    NSMutableData *receivedData;
    
    BOOL Uploading;
}

@end

@implementation DeliveryViewController

static NSMutableArray *PricesArray;
@synthesize Picker;
@synthesize Quantity;
@synthesize Price;
@synthesize Phone;
@synthesize Email;
@synthesize PriceD;
+(NSMutableArray *)PricesArray
{
    return PricesArray;
}
- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (IBAction)SubmitButton:(id)sender {
    if ([Quantity.text isEqualToString:@""] || [Email.text isEqualToString:@""]||[Phone.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning" message:@"Please fill all info" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:Nil, nil] ;
        alert.tag = 99;
        [alert show];
    }
    else
    {
        [self StartUpload];
    }
    
}
-(void)StartUpload
{
    Uploading = YES;
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
-(void)UploadData
{
    NSString *URLL = [NSString stringWithFormat:@"http://www.hodico.com/mAdmin/orders_getdata.php?phone=%@&email=%@&productid=%@&quantity=%@"
                      ,Phone.text
                      ,Email.text
                      ,[SelectedItem objectForKey:@"pp_id"]
                      ,Quantity.text];
    NSURL *URl = [NSURL URLWithString:URLL];
    
    
    NSMutableURLRequest* FurlRequest = [NSURLRequest requestWithURL:URl  cachePolicy:NSURLCacheStorageNotAllowed timeoutInterval:10];
    
    NSOperationQueue* queue = [[NSOperationQueue alloc] init];
    [NSURLConnection sendAsynchronousRequest:FurlRequest
                                       queue:queue
                           completionHandler:^(NSURLResponse* response,
                                               NSData* data,
                                               NSError* error)
     {
         
         NSLog(@"error: %@", error);
         if (error) {
             
             [MBProgressHUD hideHUDForView:self.view animated:YES];
             UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Try again", nil];
             alert.tag = 91;
             [alert show];
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

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(dismissKeyboard)];
    
    [self.view addGestureRecognizer:tap];
    [Email setDelegate:self];
    [Phone setDelegate:self];
    [Quantity setDelegate:self];
    jsonArray  = [[NSMutableArray alloc]init];
    receivedData = [[NSMutableData alloc] init];
    
}
-(void)viewDidAppear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(textFieldChanged)
                                                 name:UITextFieldTextDidChangeNotification
                                               object:Quantity];
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
    
    Uploading = NO;
    NSURL *URl = [NSURL URLWithString:urll];
    NSOperationQueue* queue = [[NSOperationQueue alloc] init];
    
    NSMutableURLRequest* FurlRequest = [NSURLRequest requestWithURL:URl  cachePolicy:NSURLCacheStorageNotAllowed timeoutInterval:10];
    [NSURLConnection sendAsynchronousRequest:FurlRequest
                                       queue:queue
                           completionHandler:^(NSURLResponse* response,
                                               NSData* data,
                                               NSError* error)
     {
         
         NSLog(@"error: %@", error);
         if (error) {
             [MBProgressHUD hideHUDForView:self.view animated:YES];
             [self CantDownload];
         }
         else
         {
             
             id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
             if (jsonObject) {
                 dispatch_async(dispatch_get_main_queue(), ^{
                     [HUD hide:YES];
                     if ([jsonObject isKindOfClass:[NSArray class]]) {
                         jsonArray = jsonObject;
                         for(NSDictionary *item in jsonArray) {
                             NSLog(@"Item: %@", item);
                             NumberOfItems++;
                         }
                         
                         [Picker reloadAllComponents];
                         
                     }
                 });
             }
             else
             {
                 [self CantDownload];
             }
             
         }
     }];
    
    
}


-(void)CantDownload
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:@"Try again", nil];
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 99) {
        
    }
    else
    {
        if (alertView.tag == 90) {
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
        else
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
        
    }
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
    return jsonArray.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    SelectedItem = jsonArray[row];
    SelectedItemPrice = [[SelectedItem objectForKey:@"pp_product_price"] integerValue];
    //    if (Quantity.text != NULL) {
    //        NSString *tmp = Quantity.text;
    //        [Price setText:[NSString stringWithFormat:@"%li",SelectedItemPrice*[tmp integerValue]]];
    //    }
    return [SelectedItem objectForKey:@"product_description"];
}



#pragma mark -
#pragma mark PickerView Delegate
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component
{
    dispatch_async(dispatch_get_main_queue(), ^{
    SelectedItem = jsonArray[row];
    SelectedItemPrice = [[SelectedItem objectForKey:@"pp_product_price"] integerValue];
    if (Quantity.text != NULL) {
        NSString *tmp = Quantity.text;
        NSInteger PriceInt = SelectedItemPrice*[tmp integerValue] /20;
        NSInteger PrioceDol = PriceInt / 1500;
        
        NSInteger integerValue=PriceInt;
        NSNumberFormatter *numberFormatterComma = [NSNumberFormatter new];
        [numberFormatterComma setNumberStyle:NSNumberFormatterDecimalStyle];
        NSString *PriceIntF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
        
        integerValue=PrioceDol;
        NSString *PrioceDolF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
        
        
        [Price setText:[NSString stringWithFormat:@"%@",PriceIntF]];
        [PriceD setText:[NSString stringWithFormat:@"%@",PrioceDolF]];
    }
    });
}

-(void)dismissKeyboard {
    [Quantity resignFirstResponder];
    [Email resignFirstResponder];
    [Phone resignFirstResponder];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    NSString *tmp = Quantity.text;
    NSInteger PriceInt = SelectedItemPrice*[tmp integerValue] /20;
    NSInteger PrioceDol = PriceInt / 1500;
    
    
    NSInteger integerValue=PriceInt;
    NSNumberFormatter *numberFormatterComma = [NSNumberFormatter new];
    [numberFormatterComma setNumberStyle:NSNumberFormatterDecimalStyle];
    NSString *PriceIntF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
    
    integerValue=PrioceDol;
    NSString *PrioceDolF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
    
    
    [Price setText:[NSString stringWithFormat:@"%@",PriceIntF]];
    [PriceD setText:[NSString stringWithFormat:@"%@",PrioceDolF]];

    
}
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    return YES;
}
// It is important for you to hide kwyboard

-(void)textFieldChanged
{
    NSString *tmp = Quantity.text;
    NSInteger PriceInt = SelectedItemPrice*[tmp integerValue] /20;
    NSInteger PrioceDol = PriceInt / 1500;
    
    
    NSInteger integerValue=PriceInt;
    NSNumberFormatter *numberFormatterComma = [NSNumberFormatter new];
    [numberFormatterComma setNumberStyle:NSNumberFormatterDecimalStyle];
    NSString *PriceIntF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
    
    integerValue=PrioceDol;
    NSString *PrioceDolF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
    
    
    [Price setText:[NSString stringWithFormat:@"%@",PriceIntF]];
    [PriceD setText:[NSString stringWithFormat:@"%@",PrioceDolF]];

    
    
}

- (void) hideKeyboard
{
    NSString *tmp = Quantity.text;
    NSInteger PriceInt = SelectedItemPrice*[tmp integerValue] /20;
    NSInteger PrioceDol = PriceInt / 1500;
    
    NSInteger integerValue=PriceInt;
    NSNumberFormatter *numberFormatterComma = [NSNumberFormatter new];
    [numberFormatterComma setNumberStyle:NSNumberFormatterDecimalStyle];
    NSString *PriceIntF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
    
    integerValue=PrioceDol;
    NSString *PrioceDolF = [numberFormatterComma stringFromNumber:[NSNumber numberWithInteger:integerValue]];
    
    
    [Price setText:[NSString stringWithFormat:@"%@",PriceIntF]];
    [PriceD setText:[NSString stringWithFormat:@"%@",PrioceDolF]];

    
    [Email resignFirstResponder];
    [Phone resignFirstResponder];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}


- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}

@end
