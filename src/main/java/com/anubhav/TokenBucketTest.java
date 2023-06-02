package com.anubhav;

public class TokenBucketTest {
    public static void main(String[] args) {
        TokenBucket tokenBucket = new TokenBucket(TokenBucketConstants.maxBucketSize,
                TokenBucketConstants.numberOfRequest,
                TokenBucketConstants.windowSizeForRateLimitInMilliSeconds);

        int numberOfConsumed = 0;
        long startTime = System.currentTimeMillis();

        int totalTime =  5*1000;
        while ((System.currentTimeMillis() - startTime) < totalTime){
            boolean consumeSuccess = tokenBucket.tryConsume();
            System.out.println("try consume = "+ consumeSuccess);
            if(consumeSuccess){
                numberOfConsumed++;
            }
        }

        System.out.println("no of consumed request = "+  numberOfConsumed);
        System.out.println("time taken = "+ totalTime);
        System.out.println("no of request per window =" +  (numberOfConsumed* TokenBucketConstants.windowSizeForRateLimitInMilliSeconds/totalTime));
        System.out.println("no of request per window expected = "+ TokenBucketConstants.numberOfRequest);

    }
}