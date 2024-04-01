# equals는 일반 규약을 지켜 재정의하라

equals() 메서드는 재정의하기 쉬워보이지만, 실수할 포인트가 많다.

-> 문제를 회피하는 가장 쉬운 길은 **아예 재정의 하지않는것**
- 그냥 두면 -> 그 클래스의 인스턴스는 오직 자기자신과만 같게된다.

---
아래와 같은 상황중 하나에 해당한다면 재정의 하지 않는게 최선이다.

## 재정의 하지 않으면 좋을 상황들

### 1. 각 인스턴스가 본질적으로 고유하다.
값을 표현 하는게아니라

**동작하는 개체를 표현하는 클래스**가 여기에 해당

e.x. Thread 가 좋은예로, Object의 equals 메서드는 이러한 클래스에 딱 맞게 구현되었다.

```java
  public boolean equals(Object obj) {
    return this == obj;
  }
```

### 2. 인스턴스의 '논리적 동치성(logical equality)'을 검사할 일이 없다.

e.x. Pattern은 equals 를 검사할 필요가 없다. 

### 3. 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어 맞는다. 

e.x. 
- 대부분의 Set 구현체는 AbstractSet이 구현한 equals를 상속받아 씀
- 대부분의 List 구현체는 AbstractList로 부터 받아쓴다. 

```java
// AbstractSet equals()
public boolean equals(Object o) {
  if (o == this) {
    return true;
  } else if (!(o instanceof Set)) {
    return false;
  } else {
    Collection<?> c = (Collection)o;
    if (c.size() != this.size()) {
      return false;
    } else {
      try {
        return this.containsAll(c);
      } catch (NullPointerException | ClassCastException var4) {
        return false;
      }
    }
  }
}
```

### 4. 클래스가 private 이거나 package-private 이고 **equals 메서드를 호출할 일이 없다.**

만약 여기서 equals가 실수로라도 호출되는 걸 막고 싶다면 다음처럼 구현하면됨
```java
@Override public boolean equals(Object o){
  throw new AssertionError(); //호출 금지!
}
```

### 5. 값 클래스라 해도, 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스(item_1) 이라면 equals를 재정의 하지 않아도된다.

---

## 그렇다면 equals를 재정의 해야할 때는 언제인가? 

- **논리적 동치성을 확인해야하는데**, 상위 클래스의 equals가 논리적 동치성을 비교하도록 재정의되지 않았을 때
  - 주로 **값 클래스**들이 여기 해당됨.
    - 값 클래스 : Integer 과 String처럼 값을 표현하는 클래스


두 값 객체를 equals로 비교하는 프로그래머는 객체가 같은지가 아니라 **값이 같은지** 알고 싶어 할 것이다.

equals가 논리적 동치성을 확인하도록 재정의해두면

-> 그 인스턴스는값을 비교하길 원하는 프로그래머의 기대에 부흥함은 물론
**Map의 키와 Set의 원소로 사용할 수 있게 된다.**

---

- 값 클래스라 해도, 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스(item_1) 이라면 equals를 재정의 하지 않아도된다.
  - Enum(item_34)도 여기에 해당
- 이런 클래스에서는 어차피 논리적으로 같은 인스턴스가 2개이상 만들어지지 않으니 논리적 동치성과 객체 식별성이 사실상 똑같은 의미

---

## equals() 메서드를 재정의할 때는 반드시 일반 규약을 따라야한다.
다음은 Object 명세에 적힌 규약이다 

> equals 메서드는 동치관계를 구현하며, 다음을 만족한다.
> 
> - 반사성(reflexivity) : null이 아닌 모든 참조값 x에 대해, x.equals(x)는 true 이다.
> - 대칭성(symmetry): null이 아닌 모든 참조 값 x, y에 대해 , x.equals(y)가 true 면 y.equals(x)도 true다.
> - 추이성(transitivity) : null 이 아닌 모든 참조 값 x,y,z 에 대해, x.equals(y)가 true이고, y.equals(z)가 true 이면 x.equals(z)도 true
> - 일관성(consistency) : null이 아닌 모든 참조 값 x,y에 대해, x.equals(y)를 반복해서 호출하면 **항상 true를 반환하거나 항상 false를 반환한다.**
> - null-아님 : null이 아닌 모든 참조값 x에 대해, **x.equals(null) 은 false**


Object 명세에서 말하는 동치관계란 무엇일까?
- 집합을 서로 같은 원소들로 이뤄진 부분집합으로 나누는 연산이다.
- 모든 원소가 같은 동치류에 속한 어떤 원소와도 서로 교환할 수 있어야한다.


## 동치 관계를 만족시키기 위한 다섯 요건
### 1. **반사성** : 객체는 자기 자신과 같아야 한다.
### 2. **대칭성** : 두 객체는 서로에 대한 동치 여부에 대해 똑같이 대답해야한다. 

```java
public final class CaseInsensitiveString {
  private final String s;

  public CaseInsensitiveString(String s) {
    this.s = Objects.requireNonNull(s);
  }
  
  //대칭성 위배!
  @Override
  public boolean equals(Object o) {
    if (o instanceof CaseInsensitiveString)
      return s.equalsIgnoreCase(
          ((CaseInsensitiveString) o).s);
    if (o instanceof String) //한 방향으로만 작동!
      return s.equalsIgnoreCase((String) o);
    return false;
  }
}
```

위 클래스 객체와 일반 String 객체가 하나씩 있다고 해보자

```java
import effectiveJava.모든객체의공통메서드.item_10.CaseInsensitiveString;

CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
String s = "polish";
```
- 예상하듯 cis.equals(s) 는 true를 반환한다. 
- 하지만 String 의 equals는 CaseInsensitiveString 을 몰라서
- s.equals(cis) 는 false 를 리턴한다. 

---
이번에는 CaseInsensitiveString을 컬렉션에 넣어보자

```java
import effectiveJava.모든객체의공통메서드.item_10.CaseInsensitiveString;
import java.util.ArrayList;
import java.util.List;

List<CaseInsensitiveString> list = new ArrayList<>()
list.add(cis);

list.contains(s);
```

- `list.contains(s)`를 호출하면 false를 반환한다 
- 근데 다른 OpenJDK 에서는 true를 반환할 수 있고, 런타임 에러를 던질 수 도 있다.

> equals 규약을 어기면 그 객체를 사용하는 다른 객체들이 어떻게 반응할지 알 수없다.

### 3. **추이성** : 첫 번째 객체와 두 번째 객체가 같고, 두 번쨰 객체와 세 번째 객체가 같다면, 첫 번째 객체와 세번 째 객체도 같아야ㅕ 한다 

- 이 요건도 간단하지만 자칫하면 어기기 쉽다. 
- 상위 클래스에는 없는 새로운 필드를 하위 클래스에 추가하는 상황을 생각해보자 
  - equals 비교에 영향을 주는 정보를 추가한 것 


간단히 2차원에서의 점을 표현하는 클래스를 예로 들어보자 
```java
public class Point {
  
  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Point)) {
      return false;
    }
    Point p = (Point) o;
    return p.x == x && p.y == y;
  }
  
}
```
이제 이 클래스를 확장해서 점에 색상을 더해보자 

```java
public class ColorPoint extends Point {
  
  private final Color color;

  public ColorPoint(int x, int y, Color color) {
    super(x, y);
    this.color = color;
  }
}
```

- equals 메서드는 어떻게 해야할까? 
- 그대로 둔다면 Point의 구현이 상속되어 생상정보는 무시한 채 비교를 수행 
- equals 규약을 어기지 않았지만, 중요한 정보를 놓치게 되니 받아들일 수 없다.


다음 코드처럼 비교대상이 또 다른 ColorPoint 이고 위치와 색상이 같을 때만 true를 반환하는 equals 를 생각해보자 
```java
// 대칭성 위배
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ColorPoint))
      return false;

    return super.equals(o) && ((ColorPoint) o).color == color;
  }
```
- 이 메서드는 일반 Point 와 ColorPoint에 비교한 결과와 그 둘을 바꿔 비교한 결과가 다르다


그럼 ColorPoint.equals가 Point와 비교할 때는 색상을 무시하도록 하면 해결될까? 
```java
  //추이성 위배
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Point))
      return false;
    
    // o가 일반 Point면 색상을 무시하고 비교한다.
    if (!(o instanceof ColorPoint))
      return o.equals(this);
    
    // o가 ColorPoint 이면 색상까지 비교한다.
    return super.equals(o) && ((ColorPoint) o).color == color;
  }
```

이 방식은 대칭성은 지켜주지만, **추이성을 깨버린다.**
```java
    ColorPoint p1 = new ColorPoint(1,2, Color.RED);
    Point p2 = new Point(1, 2);
    ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);

    System.out.println("p1.equals(p2) = " + p1.equals(p2));
    System.out.println("p2.equals(p3) = " + p2.equals(p3));
    System.out.println("p1.equals(p3) = " + p1.equals(p3));
```
- 위의 두 println 에서 true를 리턴하지만, 
- 맨 밑에 println은 false를 리턴한다.
- **추이성에 위배된다!**
- 또한, 이 방식은 **무한 재귀에 빠질 위험도 있다.**
  - Point의 또 다른 하위 클래스로 SmellPoint를 만들고, equals를 같은 방식으로 구현하고 
    - myColorPoint.equals(mySmellPoint)를 호출하면 StackOverFlowError 발생

##### 그럼 해법은 무엇일까?
사실 이 현상은 모든 객체지향 언어의 동치관계에서 나타나는 근본적인 문제다.
 
> 구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.

- 이 말은 얼핏, equals 안의 instanceof 검사를 getClass 검사로 바꾸면 규약도 지키고 값도 추가하면서 구체 클래스를 상속할 수 있다는 뜻으로 들린다. 
```java
  // 리스코프 치환 원칙 위배 !
  @Override
  public boolean equals(Object o) {
    if (o == null || o.getClass() != getClass()) {
      return false;
    }
    Point p = (Point) o;
    return p.x == x && p.y == y;
  }
}
```

- 이번 equals 는 같은 구현 클래스의 객체와 비교할 때만 true를 반환한다. 
- 괜찮아 보이지만 실제로 사용 X 
- Point의 하위 클래스는 정의상 여전히 Point 이므로 **어디서는 Point 로써 활용될 수 있어야 한다.**


---
리스코프 치환 원칙에 따르면, **어떤 타입에 있어 중요한 속성이라면 그 하위 타입에서도 마찬가지로 중요하다.**

- 따라서 그 타입의 모든 메서드가 하위 타입에서도 똑같이 잘 동작해야한다.
- 구체 클래스의 하위 클래스에서 값을 추가할 방법은 없지만, 괜찮은 우회방법이 하나 있다.
  - 상속 대신 컴포지션을 사용하라 (item_18)
  - Point 를 상속하는 대신 Point를 ColorPoint의 private 필드로 두고, 
  - ColorPoint와 같은 위치의 일반 Point를 반환하는 뷰 메서드를 public 으로 추가하는 식

```java
public class ColorPointWithoutExtends {

  private final Point point;
  private final Color color;

  public ColorPointWithoutExtends(int x, int y, Color color) {
    point = new Point(x, y);
    this.color = Objects.requireNonNull(color);
  }

  /**
   * 이 ColorPoint의 Point 뷰를 반환 한다.
   */
  public Point asPoint() {
    return point;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ColorPointWithoutExtends))
      return false;
    ColorPointWithoutExtends cp = (ColorPointWithoutExtends) o;
    return cp.point.equals(point) && cp.color.equals(color);
  }
}
```
- 자바 라이브러리에도 구체 클래스를 확장해 값을 추가한 클래스가 종종 있다.
- java.sql.Timestamp 는 java.util.Date 를 확장한후 nanoseconds 필드를 추가함
  - 그 결과로 Timestamp의 equals는 대칭성을 위배하며, Date 객체와 한 컬렉션에 넣거나 서로 섞어 사용하면 엉뚱하게 동작할 수있다.

---
> 추상 클래스의 하위 클래스에서라면 equals 규약을 지키면서도 값을 추가할 수 있다.
> 
> "태그 달린 클래스 보다는 클래스 계층구조를 활용하라"는 item_23 의 조언을 따르는 클래스 계층 구조에서는 아주 중요한 사실이다.
>> **상위클래스를 직접 인스턴스로 만드는게 불가능하다면 지금까지 이야기한 문제는 일어나지 않는다.**


### 4. **일관성**은 두 객체가 같다면 (어느 하나 혹은 두 객체 모두가 수정되지 않는 한) 앞으로도 영원히 같아야 한다

- 클래스를 작성할 때는 불변 클래스로 만드는 게 나을지를 심사숙고하자(item_17)
  - 불변 클래스로 만들기로 했다면 
    - equals가 한번 같다고 한 객체와는 영원히 같다고 답하고,
    - 다르다고한 객체와는 영원히 다르다고 답하도록 만들어야한다.



- 클래스가 불변이든 가변이든 **equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안된다.**
  - e.x. java.net.URL 의 equals는 주어진 URL과 매핑된 호스트의 IP 주소를 이용해 비교
    - 호스트 이름을 IP주소로 바꾸려면 -> 네트워크를 통해야 하는데, 그 결과가 항상 같다고 보장할 수 없다. 
    - 이는 URL의 equals가 일반 규약을 어기게 하고, 실무에서도 문제를 종종 일으킴

- **이런 문제를 피하려면 equals는 항시 메모리에 존재하는 객체만을 사용한 결정적 계산만 수행해야한다.**

### 5. null-아님 은 이름처럼 모든 객체가 null과 같지 않아야 한다는 뜻

- equals가 타입을 확인하지 않으면 잘못된 타입이 인수로 주어졌을 때 ClassCastException을 던져서 일반 규약을 위배한다.
- intanceof로 검사를 하면 null 검사를 명시적으로 하지 않아도된다.

---
## 올바른 equals 메서드 구현 방법 단계별로 정리 

#### 1. **== 연산자를 사용해 입력이 자기 자신의 참조인 지 확인**
- 단순 성능 최적화용

#### 2. **instanceof 연산자로 입력이 올바른 타입인지 확인**
- 이때 올바른 타입은 equals가 정의된 클래스 인 것이 보통이지만, 
  - 가끔은 그 클래스가 구현한 특정 인터페이스가 될 수 있다.
- Set, List, Map, Map.Entry등의 컬렉션 인터페이스들이 여기 해당됨

#### 3. **입력을 올바른 타입으로 형변환한다.**

#### 4. **입력 객체와 자기 자신의 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다.**
- 모든 필드가 일치하면 true
- 하나라도 다르면 false

1. float 와 double을 제외한 기본 타입 필드는 == 연산자로 비교하고,
2. 참조타입 필드는 각각의 equals 메서드로,
3. float 와 double 필드는 각각의 정적 메서드인 `Float.compare(float, float)`, `Double.compare(double,double)`로 비교
  - float 와 double 을 특별 취급 하는 이유는 : 특수한 부동 소수값 등을 다뤄야 하기 때문
  - `Float.equals` 와 `Double.equals` 를 사용해도 되지만, 오토박싱을 수반할 수 있기 때문에 성능상 좋지 않다.
4. 배열 필드는 **원소 각각을 앞선 지침대로 비교**
5. 배열의 모든 원소가 핵심 필드라면 `Arrays.equals` 메서드들 중 하나를 사용
6. 때론 null도 정상 값으로 취급하는 참조 타입 필드도 있다.
  - 이런 필드는 정적 메서드인 `Objects.equals(Object, Object)`로 비교해 NPE 발생을 예방하자.
7. 앞서 CaseInsensitiveString 예처럼 **비교하기가 아주 복잡한 필드를 가진 클래스는**
   - 그 필드의 표준형을 저장해둔 후 표준형 끼리 비교하면 아주 경제적 
     - 이 기법은 특히 불변 클래스(item_17)에 제격
   - 가변 객체라면 값이 바뀔 때마다 표준형을 최신 상태로 갱신해줘야함 

---
어떤 필드를 먼저 비교하느냐가 equals의 성능을 좌우하기도 한다.
- 최상의 성능을 바란다면 **다를 가능성이 더 크거나** **비교하는 비용이 싼 필드**를 먼저 비교하자.
- 동기화용 락 필드 같이 객체의 논리적 상태와 관련없는 필드는 비교하면 안된다.
- 핵심 필드로부터 계산해낼 수 있는 파생 필드 역시 굳이 비교할 필요없지만,
  - 파생 필드를 비교하는 쪽이 더 빠를때도 있다

---
> equals를 다 구현했다면 세 가지만 자문해보자.
> 
> 대칭적인가? x.equals(y)가 true 이면 y.equals(x) 도 true 
>
> 추이성이 있는가? x.equals(y)가 true 이고, y.equals(z)도 true 이면, x.equals(z) 도 true 이다.
> 
> 일관적인가? x.equals(y)를 반복해서 호출하면 항상 true 거나 false

---
다음은 이상의 비법에 따라 작성해본 PhoneNumber 클래스용 equals 메서드이다.

```java
public final class PhoneNumber {
  private final short areaCode, prefix, lineNum;

  public PhoneNumber(short areaCode, short prefix, short lineNum) {
    this.areaCode = rangeCheck(areaCode, 999 , "지역코드");
    this.prefix = rangeCheck(prefix, 999, "프리픽스");
    this.lineNum = rangeCheck(lineNum, 999, "가입자 번호");
  }

  private static short rangeCheck(int val, int max, String arg) {
    if (val < 0 || val > max) {
      throw new IllegalArgumentException(arg + ": " + val);
    }
    return (short) val;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PhoneNumber)) {
      return false;
    }
    PhoneNumber pn = (PhoneNumber) o;
    return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
  }
}
```

---
## 마지막 주의사항 
1. **equals를 재정의할 땐 hashCode도 반드시 재정의하자**(item_11)
2. **너무 복잡하게 해결하려 들지말자.**
   - 필드들의 동치성만 검사해도 equals 규약을 어렵지 않게 지킬 수 있다. 
3. Object 외의 타입을 매개변수로 받는 equals 메서드를 선언하지 말자.
   - Object 외의 타입을 매개변수로 하면 Object.equals를 재정의한게 아니다

---
# 핵심정리
> 꼭 필요한 경우가 아니면 equals를 재정의 하지말자. 
> 
> 많은 경우에 Object의 equals가 우리가 원하는 비교를 정확히 수행해 준다. 
> 
> 재정의해야 할 때는 그 클래스의 핵심필드 모두 빠짐없이, 다섯가지의 규약을 지켜가며 비교해야한다.