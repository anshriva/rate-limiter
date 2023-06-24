package com.anubhav;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DistributedRedisRateLimiter {
    private static String redisKey  = "keyForCounter";
     public static void main(String[] args) throws InterruptedException {
         RedisClient redisClient = RedisClient.
                 create(RedisURI.builder().
                         withHost("localhost").
                         withPort(6379).
                         build());

         LettuceBasedProxyManager proxyManager = LettuceBasedProxyManager.
                 builderFor(redisClient).
                 withExpirationStrategy(
                    ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(100))
                 ).
                 build();

         BucketConfiguration configuration = BucketConfiguration.builder()
                 .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                 .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(20))))
                 .build();

         Bucket bucket = proxyManager.builder().build(redisKey.getBytes(), configuration);


         var executorService = Executors.newScheduledThreadPool(10);
         executorService.scheduleAtFixedRate(() ->
                 {
                     var probe = bucket.tryConsumeAndReturnRemaining(1);
                     System.out.println("isConsumed = "+ probe.isConsumed() +" probe = " + probe.toString());
                 },
                 0,
                 1,
                 TimeUnit.SECONDS);
         executorService.awaitTermination(100, TimeUnit.SECONDS);
         executorService.shutdown();
     }
}
