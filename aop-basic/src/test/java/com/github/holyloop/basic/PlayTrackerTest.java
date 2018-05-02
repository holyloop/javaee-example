package com.github.holyloop.basic;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.holyloop.basic.CompactDisc;
import com.github.holyloop.basic.Config;
import com.github.holyloop.basic.PlayTracker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Config.class })
public class PlayTrackerTest {

    @Autowired
    private CompactDisc cd;

    @Autowired
    private PlayTracker tracker;

    @Test
    public void testTracker() {
        cd.play(1);
        cd.play(1);
        cd.play(2);
        cd.play(3);
        cd.play(3);
        cd.play(3);
        cd.play(4);
        cd.play(5);
        cd.play(5);
        cd.play(6);
        
        assertEquals(2, tracker.getPlayCount(1));
        assertEquals(1, tracker.getPlayCount(2));
        assertEquals(3, tracker.getPlayCount(3));
        assertEquals(1, tracker.getPlayCount(4));
        assertEquals(2, tracker.getPlayCount(5));
        assertEquals(1, tracker.getPlayCount(6));
        assertEquals(0, tracker.getPlayCount(7));
    }
    
}
