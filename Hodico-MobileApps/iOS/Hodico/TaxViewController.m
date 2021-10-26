
//
//  TaxViewController.m
//  test
//
//  Created by MM Dev 01 on 2/23/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "TaxViewController.h"


#define HP @"http://www.hodico.com/mAdmin/tax_getdata.php?car_horsepower=true"
#define Yearr @"http://www.hodico.com/mAdmin/tax_getdata.php?car_yearmake=true"
#define Typee @"http://www.hodico.com/mAdmin/tax_getdata.php?car_types=true"


@interface TaxViewController ()
{
    UIActionSheet *actionSheet;
    
    
    int NumberOfItems ;
    NSMutableArray *jsonArray;
    MBProgressHUD *HUD;
    NSMutableData *receivedData;
    NSInteger ConnectionTag;
    
    
    
    NSMutableArray *CarType;
    NSMutableArray *HorsePower;
    NSMutableArray *YearMake;
    NSMutableArray *PlatSymbols;
    
    
    NSMutableDictionary *SelectedType ;
    NSMutableDictionary *SelectedHP ;
    NSMutableDictionary *SelectedYear;
    NSString *SelectedSymbol;
    
    
    NSString *IDHP,*IDYear,*IDType;
    
}

@end

@implementation TaxViewController
@synthesize Year,Type,HPower,Symbol,NUMBER;
@synthesize hButton,hImage,hLabel,hText;

- (IBAction)YearMake:(id)sender {
    [self StartLoading:Yearr];
}
- (IBAction)CarType:(id)sender {
    [self StartLoading:Typee];
}
- (IBAction)HorsePowerButton:(id)sender {
    [self StartLoading:HP];
}
- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)ValueChanged:(id)sender {
    [hText setText:NUMBER.text];
}
- (IBAction)EditingBegin:(id)sender {
    [hText setHidden:NO];
    [hImage setHidden:NO];
    [hLabel setHidden:NO];
    [hButton setHidden:NO];
    
}

- (void) hideKeyboard
{
    [hText setHidden:YES];
    [hImage setHidden:YES];
    [hLabel setHidden:YES];
    [hButton setHidden:YES];
    [NUMBER resignFirstResponder];
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [hText setHidden:YES];
    [hImage setHidden:YES];
    [hLabel setHidden:YES];
    [hButton setHidden:YES];
    [textField resignFirstResponder];
    return YES;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (IBAction)PlatSymbol:(id)sender {
    [self showPickerWithTag:99];
}

#pragma mark Submit Button
- (IBAction)Submit:(id)sender {
    //    if ([Type.text isEqualToString:@""] || [Year.text isEqualToString:@""] || [HPower.text isEqualToString:@""],[Symbol.text isEqualToString:@""] || [NUMBER.text isEqualToString:@""]) {
    if ([Type.text isEqualToString:@""] || [Year.text isEqualToString:@""] || [HPower.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning" message:@"Please fill all info" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:Nil, nil] ;
        alert.tag = 100;
        [alert show];
    }
    else
    {
        HUD = [[MBProgressHUD alloc] initWithView:self.view];
        [self.view addSubview:HUD];
        HUD.delegate = self;
        HUD.labelText = @"Loading";
        [HUD show:YES];
        dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, 0.01 * NSEC_PER_SEC);
        dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
            [self UploadData];
        });
    }
    
    
    
}

-(void)UploadData
{
    NSString *URLString = [[NSString stringWithFormat:@"http://www.hodico.com/mAdmin/tax_getdata.php?getdata=true&cartype=%@&carmake=%@&carhorsepower=%@",IDType,IDYear,IDHP] stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSURL *URl = [NSURL URLWithString:URLString];
    
    NSLog(@"%@",URl);
    
    NSMutableURLRequest *Request = [NSMutableURLRequest requestWithURL:URl
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:10.0];
    
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];
    
    [NSURLConnection sendAsynchronousRequest:Request
                                       queue:queue
                           completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
                               NSLog(@"error: %@", error);
                               if (error) {
                                   [MBProgressHUD hideHUDForView:self.view animated:YES];
                                   [self CantDownload:92];
                               }
                               else
                               {
                                   id jsonObject = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
                                   if (jsonObject) {
                                       dispatch_async(dispatch_get_main_queue(), ^{
                                           [HUD hide:YES];
                                           if ([jsonObject isKindOfClass:[NSArray class]]) {
                                               if ([jsonObject count] > 0) {
                                                   NSMutableDictionary *Dict = [jsonObject objectAtIndex:0];
                                                   
                                                   NSString *msg = [NSString stringWithFormat:@"Amount due: %@ L.L",[Dict objectForKey:@"tax_amount_due"]];
                                                   
                                                   UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Information" message:msg delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                                                   
                                                   [alert show];
                                               }
                                               else
                                               {
                                                   UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Information" message:@"Amount due: - L.L" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                                                   
                                                   [alert show];

                                               }
                                               
                                           }
                                       });
                                   }
                                   else
                                   {
                                       [self CantDownload:92];
                                   }
                                   
                               }
                           }];
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (actionSheet.tag == 1)
    {
        /* Do stuff here, only intended for the right UIActionSheet. This only applies for when you use multiple UIActionSheets on the same ViewController. Closing it is unnecessary, because you already specified a destructive button. */
    }
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    [hText setText:textField.text];
    return YES
    ;
}
#pragma mark - View Delegates

-(void)viewDidAppear:(BOOL)animated
{
    jsonArray = [[NSMutableArray alloc] init];
    YearMake = [[NSMutableArray alloc] init];
    CarType = [[NSMutableArray alloc] init];
    HorsePower = [[NSMutableArray alloc] init];
    
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [NUMBER setDelegate:self];
    jsonArray  = [[NSMutableArray alloc]init];
    PlatSymbols = [[NSMutableArray alloc] initWithObjects:@"None",@"B",@"C",@"G",@"J",@"M",@"N",@"O",@"R",@"S",@"T",@"Z", nil];
    
}
-(void)StartLoading:(NSString *)URL
{
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:HUD];
    HUD.delegate = self;
    HUD.labelText = @"Loading";
    [HUD show:YES];
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, 0.01 * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        [self DownloadData:URL];
    });
    
}
-(void)DownloadData:(NSString *)URL
{
    receivedData = [[NSMutableData alloc] init];
    NSURL *URl = [NSURL URLWithString:URL];
    NSMutableURLRequest *Request = [NSMutableURLRequest requestWithURL:URl
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:5.0];
    [Request addValue:@"" forHTTPHeaderField:@"Accept-Encoding"];
    NSURLConnection *connection = [NSURLConnection connectionWithRequest:Request delegate:self];
    
    if ([URL isEqualToString:Yearr]) {
        ConnectionTag = 1;
    }
    else if ([URL isEqualToString:HP])
    {
        ConnectionTag = 2;
    }else if ([URL isEqualToString:Typee])
    {
        ConnectionTag = 3;
    }
    
    [connection start];
    
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
        [self CantDownload:ConnectionTag];
        
    } else {
        for(NSDictionary *item in jsonArray) {
            NSLog(@"Item: %@", item);
            NumberOfItems++;
        }
        
        if (ConnectionTag == 1) {
            YearMake = jsonArray;
            [self showPickerWithTag:ConnectionTag];
            SelectedYear = [YearMake objectAtIndex:0];
            Year.text =  [SelectedYear objectForKey:@"car_yearmake_description"];
            IDYear = [SelectedYear objectForKey:@"car_yearmake_id"];
            
        }
        else if (ConnectionTag == 2)
        {
            HorsePower = jsonArray;
            [self showPickerWithTag:ConnectionTag];
            SelectedHP = [HorsePower objectAtIndex:0];
            HPower.text =  [SelectedHP objectForKey:@"car_horsepower_description"];
            IDHP =  [SelectedHP objectForKey:@"car_horsepower_id"];
            
            
        }else if (ConnectionTag == 3)
        {
            
            CarType = jsonArray;
            
            [self showPickerWithTag:ConnectionTag];
            SelectedType = [CarType objectAtIndex:0];
            Type.text =  [SelectedType objectForKey:@"car_type_description"];
            IDType =  [SelectedType objectForKey:@"car_type_id"];
            
        }
        
        ConnectionTag = 0;
    }
}
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1) {
        switch (alertView.tag) {
            case 1:
            {
                [self StartLoading:Yearr];
                break;
            }
            case 2:
            {
                [self StartLoading:HP];
                break;
            }
            case 3:
            {
                [self StartLoading:Typee];
                break;
            }
            case 92:
            {
                [self UploadData];
                break;
            }
            default:
            {
                break;
            }
        }
        
    }
    else
    {
        [self.navigationController popViewControllerAnimated:YES];
    }
}
-(void)CantDownload:(NSInteger)CTag
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error retreiving news" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:@"Try again", nil];
    alert.tag = CTag;
    [alert show];
}
-(void)CantUpload:(NSInteger)CTag
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:@"Error contacting server" delegate:self cancelButtonTitle:@"Back" otherButtonTitles:@"Try again", nil];
    alert.tag = CTag;
    [alert show];
}
- (IBAction)OKButtonEditing:(id)sender {
    [hText setHidden:YES];
    [hImage setHidden:YES];
    [hLabel setHidden:YES];
    [hButton setHidden:YES];
    [NUMBER resignFirstResponder];
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
-(void)showPickerWithTag:(NSInteger)Tag
{
    if ((Tag > 0 && Tag < 4) || Tag == 99)
    {
        NSString *myKey = @"car_type_id";//Default Values
        NSString *myDescription = @"car_type_description";//Default Values
        
        /* first create a UIActionSheet, where you define a title, delegate and a button to close the sheet again */
        //actionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Done" destructiveButtonTitle:nil otherButtonTitles:nil];
        UIAlertController* selectBox = [UIAlertController alertControllerWithTitle:nil
                                                                           message: nil
                                                                preferredStyle:UIAlertControllerStyleActionSheet];
        selectBox.modalInPopover = true;
        if(Tag == 1)
        {
            myKey = @"car_yearmake_id";//Default Values
            myDescription = @"car_yearmake_description";//Default Values
        }
        else if (Tag ==2)
        {
            myKey = @"car_horsepower_id";//Default Values
            myDescription = @"car_horsepower_description";//Default Values
        }
        else if (Tag == 3)
        {
            myKey = @"car_type_id";//Default Values
            myDescription = @"car_type_description";//Default Values

        }
        
        for (NSDictionary *item in jsonArray)
        {
            UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:item[myDescription]
                                                                    style:UIAlertActionStyleDefault
                                                                    handler:^(UIAlertAction * action){
                                                                    [self didSelectRowInAlertControler:item:Tag+10];
                                                                    }];
            [selectBox addAction:defaultAction];
        }
            
        
        /* I always give my controls a tag, to make sure I can work with multiple actionsheets in one View (to identify them when an event triggers */
        
        //UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
        //                                                      handler:^(UIAlertAction * action) {}];
        //actionSheet.tag = Tag;
        //actionSheet.actionSheetStyle = UIActionSheetStyleDefault;
        
        //[actionSheet addAction:defaultAction];
        //[self presentViewController:actionSheet animated:YES completion:nil];
        //CGRect  viewRect = CGRectMake(0, 50, 100, 100);
        //UIView* toolView = [[UIView alloc] initWithFrame:viewRect];
        
        
        //CGRect buttonOkFrame = CGRectMake(0, 25, 100, 30); //size & position of the button as placed on the toolView
        
        //Create the Select button & set the title
        //UIButton *buttonOk = [[UIButton alloc] initWithFrame:buttonOkFrame];
        //buttonOk.setTitle("Select", forState: UIControlState.Normal);
        //buttonOk.setTitleColor(UIColor.blueColor(), forState: UIControlState.Normal);
        //[toolView addSubview: buttonOk]; //add to the subview
        
        //add the toolbar to the alert controller
        //[selectBox.view addSubview:toolView];

        /* Initialize a UIPickerView with 100px space above it, for the button of the UIActionSheet. */
        
        //UIPickerView* positionPicker = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 0, 300, 100)];
        //positionPicker.dataSource = self;
        //positionPicker.delegate = self;
        //positionPicker.showsSelectionIndicator = true;
        
        //[positionPicker setBackgroundColor:[UIColor lightGrayColor]];
        //[actionSheet setBackgroundColor:[UIColor lightGrayColor]];

        //[selectBox.view addSubview:positionPicker];
        
        /* another unique tag for this UIPicker */
        //positionPicker.tag = Tag+10;
        
        /* Add the UIPickerView to the UIActionSheet */
        //[actionSheet addSubview:positionPicker];
        
        /* Select the previous selected value, which for me is stored in 'currentPosition' */
        //[positionPicker selectRow:0 inComponent:0 animated:NO];
        
        /* Add the UIActionSheet to the view */
        //[actionSheet showInView:self.view];
        
        /* Make sure the UIActionSheet is big enough to fit your UIPickerView and it's buttons */
        //[selectBox setBounds:CGRectMake(0,0, 320, 250)];
        
        /* clean up */
        [self presentViewController:selectBox animated:YES completion:nil];
        //[positionPicker reloadAllComponents];

    }
}

-(void)didSelectRowInAlertControler:(NSDictionary*) row:(NSInteger) Tag
{
    switch (Tag){
        case 11:
            
            SelectedYear = row;
            Year.text =  [SelectedYear objectForKey:@"car_yearmake_description"];
            IDYear = [SelectedYear objectForKey:@"car_yearmake_id"];
            break;
            
        case 12:
            SelectedHP = row;
            HPower.text =  [SelectedHP objectForKey:@"car_horsepower_description"];
            IDHP =  [SelectedHP objectForKey:@"car_horsepower_id"];
            break;
            
        case 13:
            SelectedType = row;
            Type.text =  [SelectedType objectForKey:@"car_type_description"];
            IDType =  [SelectedType objectForKey:@"car_type_id"];
            break;
            
        case 109:
            SelectedSymbol = row;
            //Symbol.text = [PlatSymbols objectAtIndex:row];
            break;
            
        default:
            
            break;
            
    }
    
}


#pragma mark PickerView Delegate
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component
{
    switch (pickerView.tag){
        case 11:
            
            SelectedYear = [YearMake objectAtIndex:row];
            Year.text =  [SelectedYear objectForKey:@"car_yearmake_description"];
            IDYear = [SelectedYear objectForKey:@"car_yearmake_id"];
            break;
            
        case 12:
            SelectedHP = [HorsePower objectAtIndex:row];
            HPower.text =  [SelectedHP objectForKey:@"car_horsepower_description"];
            IDHP =  [SelectedHP objectForKey:@"car_horsepower_id"];
            break;
            
        case 13:
            SelectedType = [CarType objectAtIndex:row];
            Type.text =  [SelectedType objectForKey:@"car_type_description"];
            IDType =  [SelectedType objectForKey:@"car_type_id"];
            break;
            
        case 109:
            SelectedSymbol = [PlatSymbols objectAtIndex:row];
            Symbol.text = [PlatSymbols objectAtIndex:row];
            break;
            
        default:
            
            break;
            
    }
    
    
}
#pragma mark - UIPickerView Data Source
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    NSInteger countofRows = 0;
    switch (pickerView.tag){
        case 11:
            
            countofRows = [YearMake count];
            break;
            
        case 12:
            
            countofRows = [HorsePower count];
            break;
            
        case 13:
            
            countofRows = [CarType count];
            break;
            
        case 109:
            countofRows = [PlatSymbols count];
            break;
            
        default:
            
            countofRows = 0;
            break;
            
    }
    return countofRows;
}

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    switch (pickerView.tag){
        case 11:
            
            return [[YearMake objectAtIndex:row]objectForKey:@"car_yearmake_description"];;
            break;
            
        case 12:
            
            return [[HorsePower objectAtIndex:row]objectForKey:@"car_horsepower_description"];;
            break;
            
        case 13:
            
            return [[CarType objectAtIndex:row] objectForKey:@"car_type_description"];
            break;
            
        case 109:
            return [PlatSymbols objectAtIndex:row];
            break;
        default:
            
            return Nil;
            break;
            
    }
    return  Nil;
}


#pragma mark - Connection Delegates

- (void)connection:(NSURLConnection*)connection didFailWithError:(NSError*)error
{
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self CantDownload:ConnectionTag];
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


@end
