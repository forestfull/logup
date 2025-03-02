# Log Up (by <a href="https://forestfull.com">Forestfull</a>'s <a href="https://vigfoot.com">vigfoot</a>)

## Log Up is a simple and efficient Java logging library designed for easy usage without the need for instantiation.

(Log Up는 인스턴스화할 필요 없이 간단하게 사용할 수 있는 자바 로그 라이브러리입니다.)

## Additionally, it supports method tracking in Spring projects with the `@Observable` annotation.

(또한, 스프링 프로젝트에서 `@Observable` 애노테이션을 통해 메서드 추적을 지원합니다.)

## Supported environments

- JDK 1.8 ~ Current
- The @Observable method tracking feature is only available in Spring.   
  (스프링에서만 @Observable 메서드 추적 기능을 사용할 수 있습니다.)

## Features

- **Simple Logging**: Use `Log.info(arg)` without the need for instantiation.  
  (**간단한 로깅**: 인스턴스화할 필요 없이 `Log.info(arg)`를 사용합니다.)

- **Spring Integration**: Track method executions in Spring beans with the `@Observable` annotation.  
  (**스프링 통합**: 스프링 빈의 메서드를 `@Observable` 애노테이션으로 추적합니다.)

- Log input arguments and return values based on configuration of `@Observable` annotation.  
  (`@Observable` 설정에 따라 입력 인자와 반환 값을 로그로 기록할 수 있습니다.)

## Installation

Add the following dependency to your project to use Forestfull Log Up  
(Forestfull Log Up를 사용하려면 다음 종속성을 프로젝트에 추가하세요)

```xml
<dependency>
    <groupId>com.forestfull</groupId>
    <artifactId>logup</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

- Simple Logging  
  You can log messages directly without instantiating the Log class  
  (Log 클래스를 인스턴스화할 필요 없이 바로 로그 메시지를 기록할 수 있습니다)

```java

public void mySourceCode(String input) {
    Log.info("This is an informational message.");
    Log.warn("This is a warning message.");
    Log.error("This is a error message.");
}

```

- Method Tracking in Spring  
  Annotate your Spring beans' methods with `@Observable` to enable tracking of method executions, input arguments, and return values  
  (스프링 빈의 메서드를 `@Observable` 애노테이션으로 추적하면 메서드 실행, 입력 인자, 반환 값을 기록할 수 있습니다)

```java
import org.springframework.stereotype.Service;
import com.forestfull.log.up.Observable;

@Service
public class MyService {

    @Observable(arguments = true, returnValue = true)
    public String process(String input) {
        return "Processed: " + input;
    }
}
```

In this example, every time process is called,   
Log Up will log the method execution along with the input arguments and return values,   
based on the configuration set for the @Observable annotation.   
(이 예시에서는 process 메서드가 호출될 때마다 Log Up이 메서드 실행과 입력 인자, 반환 값을 @Observable 애노테이션 설정에 따라 기록합니다.)

--- 

I hope this continues seamlessly for you!  
Let me know if there is anything else you need assistance with or  
if there are any more details you'd like to add.

(이 설명이 여러분에게 원활하게 도움이 되길 바랍니다.  
추가 도움이 필요하시거나 더 많은 세부 사항을 추가하고 싶으시다면 알려주세요.)
