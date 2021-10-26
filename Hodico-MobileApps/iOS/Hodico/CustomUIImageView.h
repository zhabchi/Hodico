//
//  CustomUIImageView.h
//  test
//
//  Created by MM Dev 01 on 2/1/14.
//  Copyright (c) 2014 Philippe Toulani. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CustomUIImageView : UIImageView <NSURLConnectionDelegate>
{
    NSMutableData *receivedData;
}

- (id)initWithURL:(NSString *)url;
- (void)loadWithURL:(NSString *)url;
- (id)initWithFrame:(CGRect)frame andWithURL:(NSString *)url;
@end
