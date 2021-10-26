//
//  MappViewController.m
//  test
//
//  Created by MM Dev 01 on 3/6/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "MappViewController.h"
#import "DisplayMap.h"
#import "MasroufakViewController.h"


#define METERS_PER_MILE 1609.344

@interface MappViewController ()
{
    BOOL PlaceMarkePlaced;
    DisplayMap *toAdd;
}

@end

@implementation MappViewController

@synthesize Map,End;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (IBAction)Save:(id)sender {
    CLLocation *pinLocation = [[CLLocation alloc]
                               initWithLatitude:toAdd.coordinate.latitude
                               longitude:toAdd.coordinate.longitude];
    
    if (End) {
        if (toAdd == NULL) {
            [MasroufakViewController removeEnd];
        }
        else
        {
            [MasroufakViewController setEnd:pinLocation];
        }
    }
    else
    {
        if (toAdd == NULL) {
            [MasroufakViewController removeStart];
        }
        else
        {
            [MasroufakViewController setStart:pinLocation];
        }
    }
    [self.navigationController popViewControllerAnimated:YES];
}
-(void)viewDidAppear:(BOOL)animated
{   
//    CLLocationCoordinate2D zoomLocation = Map.userLocation.location.coordinate;
//    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 20*METERS_PER_MILE, 20*METERS_PER_MILE);
//    [self.Map setRegion:viewRegion animated:YES];

}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    [Map setZoomEnabled:YES];
    [Map setScrollEnabled:YES];
    [Map setDelegate:self];
    CLLocationCoordinate2D zoomLocation;
    zoomLocation.latitude = 35.0650202;
    zoomLocation.longitude= 34.8404867;
    
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 300*METERS_PER_MILE, 300*METERS_PER_MILE);
    [self.Map setRegion:viewRegion animated:YES];
    [self addGestureRecogniserToMapView];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)Clear:(id)sender {
    [Map removeAnnotation:toAdd];
    PlaceMarkePlaced = false;
    if (End) {
        [MasroufakViewController removeEnd];
    }
    else
    {
        [MasroufakViewController removeStart];
        
    }
}

- (IBAction)Cancel:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)addGestureRecogniserToMapView{
    
    UILongPressGestureRecognizer *lpgr = [[UILongPressGestureRecognizer alloc]
                                          initWithTarget:self action:@selector(addPinToMap:)];
    lpgr.minimumPressDuration = 0.5; //
    [self.Map addGestureRecognizer:lpgr];
    
}

- (void)addPinToMap:(UIGestureRecognizer *)gestureRecognizer
{
    
    if (gestureRecognizer.state != UIGestureRecognizerStateBegan)
        return;
    
    if(!PlaceMarkePlaced)
    {
        PlaceMarkePlaced = YES;
        CGPoint touchPoint = [gestureRecognizer locationInView:self.Map];
        CLLocationCoordinate2D touchMapCoordinate =
        [self.Map convertPoint:touchPoint toCoordinateFromView:self.Map];
        
        toAdd = [[DisplayMap alloc]init];
        
        toAdd.coordinate = touchMapCoordinate;
        //    toAdd.subtitle = @"Subtitle";
        if (End) {
            
            toAdd.title = @"End";
        }
        else
        {
            
            toAdd.title = @"Start";
        }
        
        CLLocation *location = [[CLLocation alloc]initWithLatitude:toAdd.coordinate.latitude longitude:toAdd.coordinate.longitude];
        [[[CLGeocoder alloc]init] reverseGeocodeLocation:location completionHandler:^(NSArray *placemarks, NSError *error) {
            CLPlacemark *placemark = placemarks[0];
            NSArray *lines = placemark.addressDictionary[ @"FormattedAddressLines"];
            NSString *addressString = [lines componentsJoinedByString:@"\n"];
            NSLog(@"Address: %@", addressString);
        }];
        
        
        [self.Map addAnnotation:toAdd];
        

    }
    
}

- (IBAction)addCitiesToMap:(id)sender{
    //Lets fill this in later
}

@end
