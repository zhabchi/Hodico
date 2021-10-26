//
//  ZeitakViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "ZeitakViewController.h"

#import <EventKit/EventKit.h>

@interface ZeitakViewController ()
{
    EKEventStore *store;
}
@end

@implementation ZeitakViewController


@synthesize AverageKM,CurrentMileage,KMSProvided,DatePicker;

- (IBAction)SetReminderButton:(id)sender {
    if ([AverageKM.text isEqualToString:@""] || [KMSProvided.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning" message:@"Please fill all info" delegate:self cancelButtonTitle:@"Dismiss" otherButtonTitles:Nil, nil] ;
        alert.tag = 100;
        [alert show];
    }
    else
    {
        
    NSInteger Average = [[AverageKM text] integerValue];
    
    float NbofDaysToChangeOil = [[KMSProvided text] integerValue]/Average;
    
    NSDate *now = [DatePicker date];
    int daysToAdd = (int)NbofDaysToChangeOil;
    NSDate *newDate1 = [now dateByAddingTimeInterval:60*60*24*daysToAdd];
    [self createReminder:newDate1];
    }
    
}
- (IBAction)CreateEvent:(id)sender {
    EKReminder *reminder = [EKReminder reminderWithEventStore:store];
    [reminder setTitle:@"Change Oil"];
    
    EKCalendar *defaultReminderList = [store defaultCalendarForNewReminders];
    
    [reminder setCalendar:defaultReminderList];
    
    
    NSError *error = nil;
    BOOL success = [store saveReminder:reminder
                                commit:YES
                                 error:&error];
    if (!success) {
        NSLog(@"Error saving reminder: %@", [error localizedDescription]);
    }
}
-(void)createReminder:(NSDate *)date
{
    EKReminder *reminder = [EKReminder
                            reminderWithEventStore:store];
    
    reminder.title = @"Change Oil";
    
    reminder.calendar = [store defaultCalendarForNewReminders];
    
    
    EKAlarm *alarm = [EKAlarm alarmWithAbsoluteDate:date];
    
    [reminder addAlarm:alarm];
    
    NSError *error = nil;
    
    [store saveReminder:reminder commit:YES error:&error];
    
    if (error)
        NSLog(@"error = %@", error);
    
}


- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    store = [[EKEventStore alloc] init];
    [store requestAccessToEntityType:EKEntityTypeReminder
                          completion:^(BOOL granted, NSError *error) {
                              // Handle not being granted permission
                          }];
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(dismissKeyboard)];
    
    [AverageKM setDelegate:self];
    [KMSProvided setDelegate:self];
    
    [self.view addGestureRecognizer:tap];
    
}
-(void)dismissKeyboard {
    [AverageKM resignFirstResponder];
    [KMSProvided resignFirstResponder];
    [CurrentMileage resignFirstResponder];
}
- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
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


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
