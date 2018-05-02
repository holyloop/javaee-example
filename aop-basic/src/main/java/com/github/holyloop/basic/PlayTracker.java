package com.github.holyloop.basic;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PlayTracker {

    private Map<Integer, Integer> trackCounts = new HashMap<>();

    @Pointcut("execution(* com.github.holyloop.basic.CompactDisc.play(int)) && args(trackNumber)")
    public void track(int trackNumber) {
    }

    @Before("track(trackNumber)")
    public void countTrack(int trackNumber) {
        trackCounts.put(trackNumber, (getPlayCount(trackNumber) + 1));
    }

    public int getPlayCount(int trackNumber) {
        return trackCounts.containsKey(trackNumber) ? trackCounts.get(trackNumber) : 0;
    }

}
