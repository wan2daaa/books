# clone 재정의는 주의해서 진행하라

- Clonable은 복제해도 되는 클래스임을 명시하는 용도의 믹스인 인터페이스지만(item_20), 
  - 아쉽게도 의도한 목적을 제대로 이루지 못했다.
  - 가장 큰 문제는 clone 메서드가 선언된 곳이 Clonable이 아닌 Object이고,
  - 그마저도 protected 라는데 있다.
- 그래서 Clonable을 구현하는 것만으로는 외부 객체에서 clone 메서드를 호출할 수 있다.


- 리플렉션(item_65)을 사용하면 가능하지만, 100% 성공하는 것도 아니다.
  - **해당 객체가 접근이 허용된 clone 메서드를 제공한다는 보장이 없기 때문**이다.
- 하지만 이를 포함한 여러 문제점에도 불구하고 Clonable 방식은 널리 쓰이고 있어서 잘 알아두는 것이 좋다.


> 이번 아이템에서는 **clone 메서드를 잘 동작하게끔 해주는 구현 방법**과 **언제 그렇게 해야 하는지를 알려주고**, 가능한 다른 선택지에 관해 논의해보자

---
메서드 하나 없는 Closable 인터페이스는 대체 무슨 일을 할까? 

- `Closable` 인터페이스는 놀랍게도 Object의 protected 메서드인 clone의 동작 방식을 결정한다.
- `Closable`을 구현한 클래스의 인스턴스에서 clone을 호출하면
  - -> 그 객체의 필드들을 하나하나 복사한 객체를 반환하며,
  - 그렇지 않은 클래스의 인스턴스에서 호출하면 `CloneNotSupportedException`을 던짐

---
**실무에서 Cloneable을 구현한 클래스는 clone 메서드를 public으로 제공하며,
사용자는 당연히 복제가 제대로 이뤄지리라 기대한다.**

- clone 메서드의 일반 규약은 허술하다. Object 명세에서 가져온 다음 설명을 보자.

> 이 객체의 복사본을 생성해 반환한다. 
> 
> '복사'의 정확한 뜻은 그 객체를 구현한 클래스에 따라 다를 수 있다.
> 
> 일반적인 의도는 다음과 같다.
> 
> `x.clone() != x`
> 
> `x.clone().getClass() == x.getClass()`
> 
> 하지만 이상의 요구를 반드시 만족해야하는 것은 아니다.
> 
> 한편 다음 식도 일반적으로 참이지만, 역시 필수는 아니다.
> 
> `x.clone().equals(x)`
> 
> 관례상, 이 메서드가 반환하는 객체는 super.clone을 호출해 얻어야 한다. 
> 
>이 클래스와 (Object를 제외한) 모든 상위 클래스가 이 관례를 따른다면, 다음 식은 참이다.
> 
> `x.clone().getClass() == x.getClass()`
> 
> 관례상, 반환된 객체와 원본 객체는 독립적이어야 한다. 
> 
> 이를 만족하려면 super.clone으로 얻은 객체의 필드 중 하나 이상을 반환 전에 수정해야 할 수도 있다.

- 강제성이 없다는 점만 빼면 생성자 연쇄와 살짝 비슷한 메커니즘
- 즉, clone 메서드가 super.clone이 아닌 생성자로 얻은 인스턴스를 반환해도 컴파일러는 불평하지 않는다.
- 하지만 이 클래스의 하위 클래스에서 super.clone을 호출한다면 잘못된 클래스의 객체가 만들어져, 결국 하위 클래스의 clone 메서드가 제대로 동작하지 않게 된다.

---
- 제대로 동작하는 clone 메서드를 가진 상위 클래스를 상속해 Cloneable을 구현하고 싶다고 해보자.

- 가변 상태를 참조하지 않는 클래스용 clone 메서드

```java
@Override
public PhoneNumber clone() {
  try {
    return (PhoneNumber) super.clone();
  } catch (CloneNotSupportedException e) {
    throw new AssertionError(); //일어날 수 없는 일  
  }
}
```
- 위 메서드가 동작하게 하려면 PhoneNumber의 **클래스 선언에 Cloneable을 구현한다고 추가**해야 한다.
- Object의 clone 메소드는 Object를 반환하지만 PhoneNumber의 clone 메서드는 PhoneNumber를 반환하게 했다.


- 재정의한 메서드의 반환 타입은 상위 클래스의 메서드가 반환하는 타입의 하위 타입일 수 있다.

---
클래스가 가변 객체를 참조하는 순간 재앙으로 변한다. 


Stack 클래스를 예로 들어보자 

- clone 메서드가 단순히 super.clone의 결과를 그대로 반환한다면 어떻게 될까? 
  - 반환된 Stack 인스턴스의 size 필드는 올바른 값을 갖겠지만, 
  - elements 필드는 원본 Stack 인스턴스와 똑같은 배열을 참조할 것이다.
  - 원본이나 복제본중 하나를 수정하면 다른 하나도 수정되어 불변식을 해친다는 얘기다.
    - 따라서 프로그램이 이상하게 동작하거나 NPE을 던질 것이다.

> clone 메서드는 사실상 생성자와 같은 효과를 낸다. 
> 
> 즉, clone은 원본 객체에 아무런 해를 끼치지 않는 동시에 복제된 객체의 불변식을 보장해야한다.

```java
@Override public Stack clone(){
  try {
    Stack result = (Stack) super.clone();
    result.elements = elements.clone();
    return result;
  } catch (CloneNotSupportedException e) {
    throw new AssertionError();
  }
}
```

- 배열을 복제할 때는 배열의 clone 메서드를 사용하라고 권장한다.
- 사실, 배열은 clone 기능을 제대로 사용하는 유일한 예라 할 수 있다.


---

**Cloneable 아키텍처는 '가변객체를 참조하는 필드는 final로 선언하라' 는 일반 용법다 충돌한다.**

---

Cloneable을 구현한 스레드 안전 클래스를 작성할 때는 clone 메서드 역시 적절히 동기화해줘야한다.

---
- 요약하자면, Clonable을 구현하는 모든 클래스는 clone을 재정의해야한다.
  - 이때 접근 제한자는 public으로, 반환타입은 클래스 자신으로 변경한다.

- Clonable을 이미 구현한 클래스를 확장한다면 어쩔 수 없이 clone을 잘 작동하도록 구현해야하지만, 
- 그렇지 않은 상황에서는 **복사 생성자와 복사 팩터리 라는 더 나은 객체 복사 방식을 제공할 수 있다.**


복사 생성자란 단순히 자신과 같은 클래스의 인스턴스르 인수로 받는 생성자를 말한다.
```java
public Yum(Yum yum){...};
```
복사 팩터리는 복사 생성자를 모방한 정적 팩터리다.
```java
public static Yum newInstance(Yum yum) {...};
```
복사 생성자와 그 변형인 복사 팩터리는 Clonable/clone 방식보다 나은 면이 많다

복사 생성자와 복사 팩터리는 해당 클래스가 구현한 '인터페이스' 타입의 인스턴스를 인수로 받을 수 있다.

인터페이스 기반 복사 생성자와 복사 팩터리의 더 정확한 이름은 '변환 생성자' 와 '변환 팩터리'다

예컨대 HashSet 객체 s를 TreeSet 타입으로 복제할 수 있다.

clone으로는 불가능한 이 기능을 변환 생성자로는 간단히 new TreeSet<>(s)로 처리할 수 있다.


## 핵심정리
> Clonable이 몰고 온 모든 문제를 되짚어봤을 때,
> 
> 새로운 인터페이스를 만들 때는 절대 Clonable을 확장해서는 안 되며,
> 
> 새로운 클래스도 이를 구현해서는 안 된다.
> 
> final 클래스라면 Clonable을 구현해도 위험이 크지 않지만,
> 
> 성능 최적화 관점에서 검토한 후 별다른 문제가 없을 때만 드물게 허용해야한다.(item_67)
> 
> 기본 원칙은 '복제 기능은 생성자와 팩터리를 이용하는게 최고'
> 
> 단, 배열만은 clone 메서드 방식이 가장 깔끔한, 이 규칙에 합당한 예외