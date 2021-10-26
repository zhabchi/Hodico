//
//  StationsViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "StationsViewController.h"
#import "DisplayMap.h"

#define METERS_PER_MILE 1609.344
#define url @"http://www.hodico.com/mAdmin/stations_getdata.php"

@interface StationsViewController ()
{
    
    MBProgressHUD *HUD;
    NSMutableData *receivedData;
    int NumberOfItems ;
}

@end
static NSMutableArray *jsonArray;
@implementation StationsViewController
@synthesize Map;

+(void)setStations:(NSMutableArray *)Array
{
    jsonArray = Array;
}
+(NSArray *)getStations
{
    return [NSArray arrayWithArray:jsonArray];
}

- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    jsonArray = [[NSMutableArray alloc] init];
    [Map setMapType:MKMapTypeStandard];
    [Map setZoomEnabled:YES];
    [Map setScrollEnabled:YES];
    [Map setDelegate:self];
    jsonArray  = [[NSMutableArray alloc]init];
    receivedData = [[NSMutableData alloc] init];

}


- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
//    CLLocationCoordinate2D zoomLocation;
//    zoomLocation.latitude = 35.5650202;
//    zoomLocation.longitude= 33.9;
//    
//    
//    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 150*METERS_PER_MILE, 150*METERS_PER_MILE);
//    [self.Map setRegion:viewRegion animated:YES];
}
-(void)viewDidAppear:(BOOL)animated
{
    if ([jsonArray count] < 1) {
        
        [[[[self.tabBarController tabBar]items]objectAtIndex:1]setEnabled:FALSE];
        [self StartLoading];
        
    }
    CLLocationCoordinate2D zoomLocation;
    zoomLocation.latitude = 33.395298;
    zoomLocation.longitude= 35.419512;
    
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 50*METERS_PER_MILE, 50*METERS_PER_MILE);
    [self.Map setRegion:viewRegion animated:YES];
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
            
            NSString *LatS = [item objectForKey:@"station_lat"];
            NSString *LongS = [item objectForKey:@"station_long"];
            double Lat = [LatS doubleValue];
            double Long = [LongS doubleValue];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(Lat, Long);
            
            //DisplayMap *tmp = [[DisplayMap alloc] initWithName:[item objectForKey:@"station_name"] address:[item objectForKey:@"station_address"] coordinate:coordinate];
            DisplayMap *tmp = [[DisplayMap alloc] initWithName:[item objectForKey:@"station_name"] address:nil coordinate:coordinate];
            
            
            [Map addAnnotation:tmp];
            NumberOfItems++;
            
        }
        
        [[[[self.tabBarController tabBar]items]objectAtIndex:1]setEnabled:TRUE];
    }
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


- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    static NSString *const kAnnotationReuseIdentifier = @"CPAnnotationView";
    if(annotation == Map.userLocation)
    {
        [Map.userLocation setTitle:@"You are here"];
        return nil;
    }
    MKAnnotationView *annotationView = [mapView dequeueReusableAnnotationViewWithIdentifier:kAnnotationReuseIdentifier];
    if (annotationView == nil) {
        annotationView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:kAnnotationReuseIdentifier];
        annotationView.enabled = YES;
        annotationView.canShowCallout = YES;
        UIButton *Button = [UIButton buttonWithType:UIButtonTypeInfoLight];
        
        annotationView.rightCalloutAccessoryView = Button;
    }
    
    
    return annotationView;
}
- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
    NSLog(@"asdasd");
    NSLog(@"Map Item Selected");
    MKPointAnnotation *annotation = [view annotation];
    
    
    //MKPlacemark* place = [[MKPlacemark alloc] initWithCoordinate:annotation.coordinate addressDictionary:nil];
    //MKMapItem* destination = [[MKMapItem alloc] initWithPlacemark: place];
    
    
    NSString *Coord = [NSString stringWithFormat:@"comgooglemaps://?daddr=%f,%f&views=traffic",annotation.coordinate.latitude,annotation.coordinate.longitude];
   if ([[UIApplication sharedApplication] canOpenURL:
         [NSURL URLWithString:@"comgooglemaps://"]]) {
        [[UIApplication sharedApplication] openURL:
         [NSURL URLWithString:Coord]];
    } else {
        NSLog(@"Can't use comgooglemaps://");
    }

}


//-(MKAnnotationView *)mapView:(MKMapView *)mV viewForAnnotation:(id <MKAnnotation>)annotation {
//    
//    
//    if(annotation != Map.userLocation)
//    {
//        MKPinAnnotationView *pinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"Pin"];
//        
//        //pinView = (MKPinAnnotationView *)[Map dequeueReusableAnnotationViewWithIdentifier:defaultPinID];
//        if ( pinView == nil )
//        {
//           pinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"Pin"];
//        }
//        pinView.pinColor = MKPinAnnotationColorRed;
//        //pinView.canShowCallout = YES;
//        pinView.animatesDrop = YES;
//        pinView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
//        pinView.canShowCallout = YES;
//        UIButton *button=[UIButton buttonWithType:UIButtonTypeCustom];
//        button.frame = CGRectMake(0, 0, 23, 23);
//        button.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
//        button.contentHorizontalAlignment = UIControlContentHorizontalAlignmentCenter;
//        [button setTitle:@"asd" forState:UIControlStateNormal];
//        [pinView setRightCalloutAccessoryView:button];
////        UILabel *l1=[[UILabel alloc] init];
////        l1.frame=CGRectMake(0, 15, 50, 50);
////        l1.text=@"First line of subtitle";
////        l1.font=[UIFont fontWithName:@"Arial Rounded MT Bold" size:(10.0)];
////        
////        UILabel *l2=[[UILabel alloc] init];
////        l2.frame=CGRectMake(0, 30, 50, 50);
////        l2.text=@"Second line of subtitle";
////        l2.font=[UIFont fontWithName:@"Arial Rounded MT Bold" size:(10.0)];
////        [pinView addSubview : l1];
////        [pinView addSubview : l2];
//        return pinView;
//        
//        
//
//    }
//    else {
//        [Map.userLocation setTitle:@"I am here"];
//        return nil;
//    }
//    
//}
-(void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    
    if (newLocation == nil)
    {
        self.Map.centerCoordinate = newLocation.coordinate;
    }
}

-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSLog(@"locationManager:didFailWithError: %@", error);
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view
{
    //using iOS6 native maps app
    //[destination openInMapsWithLaunchOptions:@{MKLaunchOptionsDirectionsModeKey:MKLaunchOptionsDirectionsModeDriving}];
}


@end
