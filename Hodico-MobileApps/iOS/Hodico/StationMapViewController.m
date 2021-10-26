//
//  StationMapViewController.m
//  test
//
//  Created by MM Dev 01 on 1/7/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "StationMapViewController.h"
#import "StationsViewController.h"

@interface StationMapViewController ()

@end

@implementation StationMapViewController

@synthesize tblContentList;
@synthesize SearchBar;

- (IBAction)BackButton:(id)sender {
    //[self.navigationController popViewControllerAnimated:YES];
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


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    if (isSearching) {
        return [filteredContentList count];
    }
    else {
        return [contentList count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"StationCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    // Configure the cell...
    if (isSearching) {
        cell.textLabel.text = [filteredContentList objectAtIndex:indexPath.row];
        cell.detailTextLabel.text = [filteredSubContentList objectAtIndex:indexPath.row];
    }
    else {
        cell.textLabel.text = [contentList objectAtIndex:indexPath.row];
        cell.detailTextLabel.text = [subcontentList objectAtIndex:indexPath.row];
    }
    return cell;
    
}
//
//    cell.textLabel.text = [[[StationsViewController getStations] objectAtIndex:indexPath.row] objectForKey:@"station_name"];
//    cell.detailTextLabel.text = [[[StationsViewController getStations] objectAtIndex:indexPath.row] objectForKey:@"station_description"];
-(NSArray *)FillContent
{
    NSMutableArray *tmp = [[NSMutableArray alloc] init];
    for(NSDictionary *item in [StationsViewController getStations] ) {
        NSLog(@"Item: %@", item);
        [tmp addObject:[item objectForKey:@"station_name"]];
    }
    
    return [NSArray arrayWithArray:tmp];
}
-(NSArray *)FillSubContent
{
    NSMutableArray *tmp = [[NSMutableArray alloc] init];
    for(NSDictionary *item in [StationsViewController getStations] ) {
        NSLog(@"Item: %@", item);
        [tmp addObject:[item objectForKey:@"station_description"]];
    }
    return [NSArray arrayWithArray:tmp];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    contentList = [[NSMutableArray alloc] initWithArray:[self FillContent]];
    subcontentList = [[NSMutableArray alloc] initWithArray:[self FillSubContent]];
    filteredContentList = [[NSMutableArray alloc] init];
    filteredSubContentList = [[NSMutableArray alloc] init];
}
- (void)searchTableList {
    NSString *searchString = SearchBar.text;
    
    for (int i = 0; i < [contentList count]; i ++) {
        NSString *tempStr = [contentList objectAtIndex:i];
        
        if ([tempStr rangeOfString:searchString options:NSCaseInsensitiveSearch].location != NSNotFound) {
            [filteredContentList addObject:tempStr];
            [filteredSubContentList addObject:[subcontentList objectAtIndex:i]];
        }
    }
    
}
- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    isSearching = YES;
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
    NSLog(@"Text change - %d",isSearching);
    
    //Remove all objects first.
    [filteredContentList removeAllObjects];
    [filteredSubContentList removeAllObjects];
    
    if([searchText length] != 0) {
        isSearching = YES;
        [self searchTableList];
    }
    else {
        isSearching = NO;
    }
    [self.tblContentList reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    isSearching = NO;
    [self.tblContentList reloadData];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Search Clicked");
    [self searchTableList];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    contentList = [[NSMutableArray alloc] initWithArray:[self FillContent]];
    
    NSString *tempStr = [contentList objectAtIndex:indexPath.row];
    NSDictionary *item = [StationsViewController getStations][indexPath.row];
    CLLocationDegrees  lat = [[item objectForKey:@"station_lat"] doubleValue];
    CLLocationDegrees  lon = [[item objectForKey:@"station_long"] doubleValue];
    
    NSString *Coord = [NSString stringWithFormat:@"comgooglemaps://?daddr=%f,%f&views=traffic",lat,lon];
    if ([[UIApplication sharedApplication] canOpenURL:
         [NSURL URLWithString:@"comgooglemaps://"]]) {
        [[UIApplication sharedApplication] openURL:
         [NSURL URLWithString:Coord]];
    } else {
        NSLog(@"Can't use comgooglemaps://");
    }
}

@end
