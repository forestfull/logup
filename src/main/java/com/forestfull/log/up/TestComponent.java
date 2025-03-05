package com.forestfull.log.up;

import com.forestfull.log.up.spring.Observable;
import com.forestfull.log.up.util.Log;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestComponent {


    @PostConstruct
    public void init() {
        String hi = test("hi");
        Log.error(hi);
    }

    @Observable(returnValue = true, arguments = true)
    String test(String input) {
        return input + 'g';
    }
}
