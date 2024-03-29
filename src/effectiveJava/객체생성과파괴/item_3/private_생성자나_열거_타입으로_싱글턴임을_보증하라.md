# private 생성자나 열거 타입으로 싱글턴임을 보증하라

### 싱글턴이란
- 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다 

#### 싱글턴의 예시
함수와 같은 무상태(stateless)객체나 설계상 유일해야하는 시스템 컴포넌트를 들 수 있다. 

- 그런데 **클래스를 싱글턴으로 만들면**
  - -> **이를 사용하는 클라이언트를 테스트하기가 어려워 질 수 있다 .** 
    - 타입을 인터페이스로 정의한 다음 그 인터페이스를 구현해서 만든 싱글턴이 아니라면,
      - 싱글턴 인스턴스를 가짜 구현으로 대체 할 수 없기 때문

#### 싱글턴을 만드는 방법 

##### 첫번째 방법 
```java
public class Elvis {
  public static final Elvis INSTANCE = new Elvis();
  private Elvis() {}  
  public void leaveTheBuilding(){}
}
```

- Elvis.INSTANCE 를 초기화 할때 생성자가 딱 한번 호출된다
- Elvis 클래스가 초기화될 때 만들어진 인스턴스가 전체 시스템에서 하나뿐임이 보장됨
  - 예외는 있다 
    - 권한이 있는 클라이언트는 리플렉션 API 인 `AccesibleObject.setAccessible`을 사용해 private 생성자를 호출할 수 있다. 

##### 두번째 방법
```java
public class Elvis {
  private static final Elvis INSTANCE = new Elvis();
  private Elvis() {}
  public static Elvis getInstance() {return INSTANCE;}
  
  public void leaveTheBuilding(){}
}
```
- `Elvis.getInstance`는 항상 같은 객체의 참조를 반환하므로 싱글톤이다 

### 첫번째 방법의 장점
- 첫번째 장점은 해당 클래스가 싱글턴임이 API에 명백히 드러난다 
- 두번째 장점은 간결함이다. 

### 두번째 방법의 장점 
- 첫번째 장점은 API를 바꾸지 않고도 싱글턴이 아니게 바꿀수 있다 
- 원한다면 정적 팩토리 메소드를 제네릭 싱글턴 팩터리로 만들 수 있다
- 정적 팩토리 메서드의 메서드 참조를 공급자로 사용할 수 있다. 
  - Elivs::getInstance 를 Supplier<Elvis> 로 사용하는 식이다 (item_43,44)

> 두번째 방법의 장점들이 굳이 필요없다면 public 필드 방식이 좋다

- 둘 중 하나의 방식으로 만든 싱글턴을 직렬화 하려면 단순히 **`Serializable`을 구현한다고 선언하는 것 만으로는 부족하다**
- 모든 인스턴스 필드를 일시적(transient)이라고 선언하고 readResolve 메서드를 제공해야함 (item_89)
  - 이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화할 때마다 새로운 인스턴스가 만들어짐
    - 코드 3-2의 예에서라면 가짜 Elvis가 탄생한다는 뜻 
    - 원치 않은 객체의 생성을 예방하고 싶다면 Elvis 클래스에 다음의 readResolve 메서드를 추가하자

```java
public class Elvis {
  private static final Elvis INSTANCE = new Elvis();
  private Elvis() {}
  public static Elvis getInstance() {return INSTANCE;}
  
  //싱글턴임을 보장해주는 readResolve 메서드
  private Object readResolve() {
    // '진짜' Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에 맡긴다. 
    return INSTANCE;
  }
}
```


##### 세번째 방법
- 원소가 하나인 열거 타입을 선언하는 것 
```java
public enum Elvis {
  INSTANCE;
  
  public void leaveTheBuilding() {}
}
```
- public 필드 방식과 비슷하지만, 
  - 더 간결하고,
  - 추가 노력 없이 직렬화 할 수 있고, 
  - 심지어 아주 복잡한 직렬한 상황이나 
  - 리플렉션 공격에서도 제 2의 인스턴스가 생기는 일을 완벽히 막아준다.

- **대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.**
  - 단, **만들려는 싱글턴이 Enum 외의 클래스를 상속해야한다면,**
    - 이 방법은 사용할 수 없다.(열거 타입이 다른 인터페이스를 구현하도록 선언할 수 있다.)

