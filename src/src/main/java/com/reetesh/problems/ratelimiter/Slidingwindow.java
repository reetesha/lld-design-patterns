package com.reetesh.problems.ratelimiter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Slidingwindow {
    int limit;
    Map<String, Deque<Long>> userRequests;
    Slidingwindow(int limit){
        this.limit=limit;
        userRequests = new ConcurrentHashMap<>();
    }
    public boolean isAllowed(String userId){

        long now= System.currentTimeMillis();
        userRequests.putIfAbsent(userId,new ArrayDeque<>());
        Deque<Long> q=userRequests.get(userId);

        synchronized (q){
            while(!q.isEmpty() && now-q.peekFirst()>=1000){
                q.pollFirst();
            }
            if(q.size()<limit){
                q.addLast(now);
                return true;
            }
        }
        return false;
    }
}
