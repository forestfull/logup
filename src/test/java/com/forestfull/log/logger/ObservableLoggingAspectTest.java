package com.forestfull.log.logger;

import com.forestfull.log.test.Constructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ObservableLoggingAspectTest {


    @Test
    void test() {
        Constructor constructor = new Constructor();
        constructor.testMethod("1", 1);
        constructor.testMethod("2", 2);
        constructor.test("2");
    }
}