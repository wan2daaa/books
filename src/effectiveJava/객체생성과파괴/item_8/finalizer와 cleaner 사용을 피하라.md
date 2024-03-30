# finalizer와 cleaner 사용을 피하라

자바는 두가지 객체 소멸자를 제공한다. 
- 그중 `finalizer`는 **예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.**
- 오동작, 낮은 성능, 이식성 문제의 원인이 되기도 한다.
- `finalizer`는 나름의 쓰임새가 몇 가지 있긴 하지만, 기본적으로는 '**쓰지 말아야**' 한다.
  - 그래서 자바 9에서는 `finalizer`를 `deprecated API`로 지정하고 `cleaner`를 그 대안으로 소개헀다. 
    - 하지만, 자바 라이브러리 에서도 finalizer를 여전히 사용함



- `cleaner`은 **finalizer 보다 덜 위험하지만, 여전히 예측할 수 없고, 느리고, 일반적으로 불 필요하다.**
- 자바의 `finalizer` 와 `cleaner`는 C++ 의 파괴자와는 다른 개념이다. 
  - C++에서 파괴자는 특정 객체와 관련된 자원을 회수하는 보편적인 방법이다.
  - C++의 파괴자는 비메모리 자원을 회수하는 용도 
- 자바에서는 **접근할 수 없게 된 객체를 회수하는 역할**을 **가비지 컬렉터**가 담당하고,
  - 프로그래머에게는 아무런 작업도 요구하지 않는다.
- 자바에서는 `try-with-resources` 와 `try-finally`를 사용해서 해결한다.

---

- `finalizer 와 cleaner`는 즉시 수행된다는 보장이 없다.
- 객체에 접근 할 수 없게된 후 `finalizer` 와 `cleaner` 가 실행되기까지 얼마나 걸릴지 알 수 없다.

> 즉, finalizer와 cleaner로는 제때 실행되어야 하는 작업은 절대 할 수 없다.

- e.x. 파일 닫기를 finalizer나 cleaner에 맡기면 중대한 오류를 일으킬 수 있다.
  - 시스템이 동시에 열 수 있는 파일 개수에 한계가 있기 때문
- 시스템이 finalizer나 cleaner 실행을 게을리해서 파일을 계속 열어 둔다면 새로운 파일을 열지 못해 프로그램이 실패할 수 있다.

---

- 클래스에 finalizer를 달아두면 그 인스턴스의 자원 회수가 제멋대로 지연될 수 있다.
  - 한편, cleaner는 **자신을 수행할 스레드를 제어할 수 있다는 면**에서 조금 낫다.
    - 하지만 여전히 **백그라운드에서 수행**되며 **가비지 컬렉터의 통제**하에 있으니 즉각 수행되리라는 보장은 없다

---

- 자바 언어 명세는 finalizer나 cleaner의 수행시점뿐 아니라 **수행 여부 조차 보장하지 않는다.**

> 상태를 영구적으로 수정하는 작업에서는 절대 finalizer나 cleaner에 의존해서는 안된다.

- e.x. DB 같은 공유자원의 영구 락 해제를 finalizer나 cleaner에 맡겨 놓으면, 
  - -> 분산 시스템 전체가 서서히 멈출 것이다.

---


- **System.gc 나 System.runFinalization 메서드에 현혹되지 말자.** 
- finalizer나 cleaner가 실행될 가능성을 높여줄 수 있으나, 보장해주진 않는다.
  - 사실 이를 보장해주겠다는 메서드가 2개 있었다.
    - `System.runFinalizersOnExit`와 그 쌍둥이인 `Runtime.runFinalizersOnExit`이다
      - 하지만 이 두 메서드는 심각한 결함때문에 수십년간 지탄 받았다. (ThreadStop)
- finalizer의 부작용은 여기서 끝이 아니다 
  - finalizer 동작 중 발생한 예외는 무시되며, **처리할 작업이 남았더라도 그순간 종료된다.**
  - 잡지 못한 예외 때문에 해당 객체는 자칫 마무리가 덜 된 상태로 남을 수 있다.
  - 다른 스레드가 훼손된 객체를 사용하려 한다면 어떻게 동작할지 예측할 수 없다.
    - 그나마 cleaner를 사용하는 라이브러리는 자신의 스레드를 통제하기 때문에 이러한 문제가 발생하지 않는다.


---


- **finalizer와 cleaner는 심각한 성능 문제도 동반한다.**
- e.x. AutoClosable 객체를 생성하고 가비지 컬렉터가 수거하기까지 12ns 가 걸린 반견 (try-with-resources 로 자신을 닫도록 함 )
- finalizer를 사용하면 550ns가 걸렸다, cleaner 도 비슷
  - 다시 말해 finalizer를 사용한 객체를 생성하고 파괴하니 50배나 느림(대략적 수치)
  - finalizer가 가비지 컬렉터의 효율을 떨어뜨리기 때문 
- 안전망 방식을 활용하면 객체를 생성, 정리, 파괴하는데 약 5배 정도 느려지게 만들 수 있다.

---


- **finalizer를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안문제를 일으킬 수 도 있다.**
  - 생성자나 직렬화 과정에서 예외가 발생하면, 
  - 이 생성되다 만 객체에서 악의적인 하위클래스의 finalizer가 수행될 수 있게 됨
    - 이 finalizer는 정적 필드에 자신의 참조를 할당하여 가비지 컬렉터가 수집못하게 막을 수 있다.
- **객체 생성을 막으려면 생성자에서 예외를 던지는 것만으로 충분하지만, finalizer가 있다면 그렇지도 않다.**
  - final 클래스들은 그 누구도 하위 클래스를 만들 수없으니 이 공격에서 안전하지만
    - **final이 아닌 클래스를 finalizer 공격으로부터 방어하려면 아무 일도 하지 않는 finalize 메서드를 만들고 final로 선언하자.**

---

- 그렇다면 파일이나 스레드 등 종료해야 할 자원을 담고 있는 객체의 클래스에서 finalizer 나 cleaner를 대신해줄 방법은? 

> 그저 **AutoClosable을 구현해주고**, 클라이언트에서 인스턴스를 다 쓰고 나면 close 메서드를 호출하면된다.
>> 일반적으로 예외가 발생해도 제대로 종료되도록 `try-with-resources`를 사용해야함

- **각 인스턴스는 자신이 닫혔는지를 추적하는 것이 좋다.**
  - 다시 말해, close 메서드에서 이 객체는 더이상 유효하지 않음을 필드에 기록하고,
  - 다른 메서드는 이 필드를 검사해서 객체가 닫힌 후에 불렸다면 `IllegalStateException`을 던지는 것이다.

---

- 도대체 그럼 cleaner와 finalizer은 어디에 쓰이는지 
- 적절한 쓰임새는 두가지 정도 있다

1. 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할
   - cleaner나 finalizer가즉시 호출된다는 보장은 없지만, 클라이언트가 하지 않은 자원 회수를 늦게라도 해주는 것이 아예 안하는 것 보다는 나음
   - 이런 안전망 역할의 finalizer를 작성할 때는 그럴만한 값어치가 있는지 심사숙고하자
   - 자바 라이브러리의 일부 클래스는 안전망 역할의 finalizer를 제공
     - e.x. FileInputStream, FileOutputStream, ThreadPoolExecuter 가 대표적



2. 네이티브 피어(native-peer)와 연결된 객체에서다
   - 네이티브 피어란 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말함
   - 자바 객체가 아니니 가비지 컬렉터는 그 존재를 알지 못한다.
     - 그 결과 자바 피어르 회수 할 때 네이티브 객체까지 회수하지 못한다.
   - cleaner나 finalizer가 나서서 처리하게 적당한 작업
     - 단, 성능 저하를 감당할 수 있고 네이티브 피어가 심각한 자원을 가지고 있지 않을 때만 해당 
     - 위의 경우가 아니라면, close 메서드를 사용해야함


---


- cleaner는 사용하기에 조금 까다롭다
- e.x. Room 클래스로 이 기능을 설명
  - Room 자원을 수거하기 전에 반드시 청소(clean) 해야 한다고 가정 
  - Room 클래스는 AutoClosable을 구현한다.
  - 사실 자동 청소 안전망이 cleaner를 사용할지 말지는 내부 구현 방식에 관한 문제다 
    - 즉, finalizer와 달리 cleaner는 클래스의 public API에 나타나지 않는다.

```java
public class Room implements AutoCloseable{

  private static final Cleaner cleaner = Cleaner.create();   
  
  //청소가 필요한 자원. 절대 Room을 참조해서는 안된다!
  private static class State implements Runnable {
    int numJunkPiles ; //방안의 쓰레기 수

    State(int numJunkPiles) {
      this.numJunkPiles = numJunkPiles;
    }

    //close 메서드나 cleaner가 호출한다.
    @Override
    public void run() {
      System.out.println("방 청소");
      numJunkPiles = 0;
    }
  }

  //방의 상태. cleanable과 공유
  private final State state;

  //cleanable 객체. 수거 대상이 되면 방을 청소한다.
  private final Cleaner.Cleanable cleanable;

  public Room(int numJunkPiles) {
    state = new State(numJunkPiles);
    cleanable = cleaner.register(this, state);
  }
  @Override
  public void close() throws Exception {
    cleanable.clean();
    
  }
}
```

- static으로 선언된 중첩 클래스인 State는 cleaner가 방을 청소할 때 수거할 자원들을 담고 있다.
- numJunkPiles 필드가 수거할 자원에 해당
- 더 현실적으로 만들려면 이 필드는 네이티브 피어를 가리키는 포인터를 담은 final long 변수여야함
- State는 Runnable을 구현하고, 그안에 run 메서드는 cleanable에 의해 딱 한번만 호출될 것
- 이 cleanable 객체는 Room 생성자에서 cleaner에 Room 과 State를 등록할 때 얻음 



- run 메서드가 호출되는 상황은 두가지 
1. 보통은 Room의 close 메서드를 호출할 때 
   - close 메서드에서 Cleanable의 clean을 호출하면 이 메서드 안에서 run을 호출
2. 혹은 가비지 컬렉터가 Room을 회수할 때까지 클라이언트가 close를 호출하지 않는다면, cleaner가(바라건대) State의 run 메서드를 호출함


---


- State 인스턴스는 '**절대로**' Room 인스턴스를 참조해서는 안된다.
- 참조할 경우, -> 순환참조가 생겨 가비지 컬렉터가 Room 인스턴스를 회수해갈(자동으로 청소될) 기회가 오지 않음
- 정적이 아닌 중첩 클래스는 자동으로 바깥 객체의 참조를 갖기 때문이다.(item_24)
- 이와 비슷하게, 람다 역시 바깥 객체의 참조를 갖기 쉬우니 사용하지 않는 것이 좋다.


---


- Room의 cleaner는 단지 안전망으로만 쓰였다. 
- 클라이언트가 모든 Room 생성을 try-with-resources 블록으로 감쌌다면, 
  - -> 자동 청소는 전혀 필요 X


- 다음은 잘 짜여진 클라이언트 코드의 예

```java
import effectiveJava.객체생성과파괴.item_8.Room;

public class Adult {

  public static void main(String[] args) {
    try (Room myRoom = new Room(7)) {
      System.out.println("안녕~");
    }
  }
}
// 기대한 대로 "안녕~" 출력후, "방 청소" 를 출력한다
```

- 이번엔 결코 방 청소를 하지 않는 코드다 
```java
public class Teenager {

  public static void main(String[] args) {
    new Room(99);
    System.out.println("아무렴");
  }
}
```
- "방청소"는 출력되지 않음


---
- cleaner의 명세에는 이렇게 적혀있다.
> System.exit을 호출할 때의 cleaner 동작은 구현하기 나름이다. 청소가 이뤄질지는 보장하지 않는다.

- 일반적인 프로그램 종료에서도 마찬가지이다 
- Teenager 클래스 main 메서드에 System.gc()를 추가하는 것으로 종료 전에 "방청소" 를 출력할 수 있었지만 무조건 그러리라는 보장은 없다

## 핵심 정리
> cleaner(자바 8 까지는 finalizer)는 안전망 역할이나 중요하지 않은 네이티브 자원 회수용으로만 사용하자.
> 물론 이런 경우라도 불확실성과 성능 저하에 주의해야함 
