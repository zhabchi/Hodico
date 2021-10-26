//
//  CustomUIImageView.m
//  test
//
//  Created by MM Dev 01 on 2/1/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import "CustomUIImageView.h"

@implementation CustomUIImageView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (id)initWithFrame:(CGRect)frame andWithURL:(NSString *)url
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        receivedData = [[NSMutableData alloc] init];
        [self loadWithURL:url];
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (id)initWithURL:(NSString *)url
{
    self = [self init];
    
    if (self)
    {
        receivedData = [[NSMutableData alloc] init];
        [self loadWithURL:url];
    }
    
    return self;
}
- (void)loadWithURL:(NSString *)url
{
    receivedData = [NSMutableData data];
    [self performSelectorOnMainThread:@selector(DownloadImage:) withObject:url waitUntilDone:NO];
    
    //[NSThread detachNewThreadSelector:@selector(DownloadImage:) toTarget:self withObject:url];
    //[self DownloadImage:url];
}
-(void)DownloadImage:(NSString *)url
{
    NSString *FinalString =  @"http://www.hodico.com/mAdmin/";
    FinalString = [FinalString stringByAppendingString:url];
    NSLog(@"Downloading image from: %@",FinalString);
    NSURL *URl = [NSURL URLWithString:FinalString];
    NSURLRequest *Request = [NSURLRequest requestWithURL:URl
                                             cachePolicy:NSURLRequestUseProtocolCachePolicy
                                         timeoutInterval:5.0];
    NSURLConnection *connection = [NSURLConnection connectionWithRequest:Request delegate:self];

    [connection start];
    
    
}

- (void)connection:(NSURLConnection*)connection didFailWithError:(NSError*)error
{
    NSLog(@"Did Fail");
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
    NSLog(@"Did Load");
    UIImage * Downloadedimage = [UIImage imageWithData:receivedData];
    [self setImage:Downloadedimage];
}


@end
