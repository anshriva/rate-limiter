package com.anubhav;

public class TokenBucketTest {
    public static void main(String[] args) {
        TokenBucket tokenBucket = new TokenBucket(TokenBucketConstants.maxBucketSize,
                TokenBucketConstants.numberOfRequest,
                TokenBucketConstants.windowSizeForRateLimitInMilliSeconds);

        int numberOfConsumed = 0;
        long startTime = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTime) < 10*1000){
            boolean consumeSuccess = tokenBucket.tryConsume();
            System.out.println("try consume = "+ consumeSuccess);
            if(consumeSuccess){
                numberOfConsumed++;
            }
        }

        long endTime = System.currentTimeMillis();

        long timeInMS = endTime - startTime;

        System.out.println("no of consumed request = "+  numberOfConsumed);
        System.out.println("time taken = "+ timeInMS);
        System.out.println("no of request per window =" +  (numberOfConsumed* TokenBucketConstants.windowSizeForRateLimitInMilliSeconds/timeInMS));
        System.out.println("no of request per window expected = "+ TokenBucketConstants.numberOfRequest);

    }
}