package com.anubhav;

public class TokenBucket {

    private int numberOfTokenAvailable;
    private int numberOfRequests;
    private int windowSizeForRateLimitInMilliSeconds;
    private long lastRefillTime;
    private long nextRefillTime;
    private int maxBucketSize;

    public TokenBucket(int maxBucketSize, int numberOfRequests, int windowSizeForRateLimitInMilliSeconds) {
        this.maxBucketSize = maxBucketSize;
        this.numberOfRequests = numberOfRequests;
        this.windowSizeForRateLimitInMilliSeconds = windowSizeForRateLimitInMilliSeconds;
        this.refill();
    }

    public boolean tryConsume(){
        refill();
        if(this.numberOfTokenAvailable > 0){
            this.numberOfTokenAvailable --;
            return true;
        }
        return false;
    }

    private void refill(){
        if(System.currentTimeMillis() < this.nextRefillTime){
            return;
        }
        this.lastRefillTime = System.currentTimeMillis();
        this.nextRefillTime = this.lastRefillTime + this.windowSizeForRateLimitInMilliSeconds;
        this.numberOfTokenAvailable = Math.min(this.maxBucketSize, this.numberOfTokenAvailable + this.numberOfRequests);
    }
}
