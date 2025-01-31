package com.forestfull.log.logger;

import com.forestfull.log.logger.util.Log;
import org.junit.jupiter.api.Test;
class ObservableLoggingAspectTest {

    public static class Con{
        public void testMethod(Object o, Object o1){
            Log.info(o, o1);
        }

        public void test(Object o){
            testMethod(o, null);
        }
    }

    @Test
    void test() {
        Con constructor = new Con();
        constructor.testMethod("1", 1);
        constructor.testMethod("2", 2);
        constructor.test("2");
    }
}
