# Log Up

**Log Up** is a simple and efficient Java logging library that can be easily used without instantiation.  
It supports the `@Observable` annotation for tracing method executions in a Spring environment and provides various logging features.

(**Log Up**은 인스턴스화 없이 쉽게 사용할 수 있는 간단하고 효율적인 Java 로깅 라이브러리입니다.   
Spring 환경에서 메서드 실행을 추적할 수 있는 `@Observable` 어노테이션을 지원하며, 다양한 로깅 기능을 제공합니다.)

## Features

- **Simple Logging**: Messages can be logged using static methods like `Log.info("message")`, `Log.warn("message")`, and `Log.error("message")`.   
(**간단한 로깅**: `Log.info("메시지")`, `Log.warn("메시지")`, `Log.error("메시지")` 같은 정적 메서드로 메시지를 로깅할 수 있습니다.)
- **Spring Integration**: Use the `@Observable` annotation to trace method executions in Spring beans, logging input arguments and return values.   
(**Spring 통합**: Spring 빈에서 메서드 실행을 추적하기 위해 `@Observable` 어노테이션을 사용해 입력 인자와 반환 값을 로깅할 수 있습니다.)
- **Source Code Location Tracking**: The `location()` method retrieves the caller's source code location (class, method, line number).   
(**소스 코드 위치 추적**: `location()` 메서드로 호출자의 소스 코드 위치(클래스, 메서드, 줄 번호)를 얻을 수 있습니다.)
- **Test Logging**: The `test("message")` method logs messages without color formatting, making it suitable for test environments.   
(**테스트 로깅**: 색상 포맷팅 없이 메시지를 로깅하는 `test("메시지")` 메서드를 제공하여 테스트 환경에 적합합니다.)

## Installation

If you are using Maven, add the following dependency to your `pom.xml`   
(Maven을 사용하는 경우, `pom.xml`에 다음 의존성을 추가하세요.)

```xml
<dependency>
    <groupId>com.forestfull</groupId>
    <artifactId>logup</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage Examples
Simple Logging   
(간단한 로깅)

```java
import com.forestfull.log.up.util.Log;

public class MyClass {
    public void myMethod() {
        Log.info("정보 메시지입니다.");
        Log.warn("경고 메시지입니다.");
        Log.error("에러 메시지입니다.");
    }
}
```

## Method Tracing in Spring

To trace method executions in a Spring environment, use the `@Observable` annotation   
(Spring 환경에서 메서드 실행을 추적하려면 @Observable 어노테이션을 사용하세요.)

```java
import com.forestfull.log.up.annotation.Observable;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    @Observable(arguments = true, returnValue = true)
    public String process(String input) {
        return "처리됨: " + input;
    }
}
```

## Source Code Location Tracking

To identify the caller’s location, use the `location` method   
(호출자의 위치를 확인하려면 location 메서드를 활용하세요.)

```java
import com.forestfull.log.up.util.Log;

public class MyClass {
    public void myMethod() {
        Log.location(MyClass.class, 4).info("정보 메시지입니다.");
    }
}
```

## Test Logging

To log without colors for Test-Driven Development (TDD), use the `test` method   
(TDD를 위해 색상 없이 로깅하려면 test 메서드를 활용하세요.)

```java
import com.forestfull.log.up.util.Log;

public class MyClass {
    public void myTestMethod() {
        Log.test("색상 없는 테스트 메시지입니다.");
    }
}
```
