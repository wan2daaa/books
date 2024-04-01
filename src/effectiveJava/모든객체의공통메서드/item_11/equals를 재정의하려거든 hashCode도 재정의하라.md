# equals를 재정의하려거든 hashCode도 재정의하라

- **equals를 재정의한 클래스 모두에서 hashCode도 재정의 해야된다.**
- 그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 HashMap 이나 HashSet 같은 컬렉션의 원소로 사용할때 문제를 일으킨다.

---
### Object 명세에서 발췌한 규약
> - equals 비교에서 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 같을 반환해야한다.
>   - 단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.
> - equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야한다.
> - equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다.
>   - 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.

---
**hashCode 재정의를 잘못했을 때 크게 문제가 되는 조항은 두번 째다. 즉, 논리적으로 같은 객체는 같은 해시코드를 반환해야 한다.**

item_10 에서 보았듯이 equals는 물리적으로 다른 두 객체를 논리적으로 같다고 할 수 있다.

하지만 Object의 기본 hashCode 메서드는 이둘이 전혀 다르다고 판단하여, 규약과 달리 서로 다른 값을 반환

---
e.x. item_10의 PhoneNumber 클래스의 인스턴스를 HashMap의 원소로 사용한다고 해보자.

```java
import java.util.HashMap;

Map<PhoneNumber, String> m = new HashMap<>()
m.put(new PhoneNumber(707,867,5309), "제니");
m.get(new PhoneNumber(707, 867, 5309))
```

- "제니"가 나와야 할 것 같지만, 실제로는 null을 반환 
- PhoneNumber 클래스는 hashCode를 재정의 하지 않았기 때문에 논치적 동치인 두 객체가 서로 다른 해시 코드를 반환하여 두 번째 규약을 지키지 못한다. 

- 아주 간단하게 안좋게 구현하면 이렇게 쓸 수 있다.
```java
  @Override
  public int hashCode() {
    return 42;
  }
```
- 모든 객체에서 같은 해시코드를 반환해서 
- 모든 객체가 해시테이블의 버킷 하나에 담겨 마치 연결 리스트 처럼 동작한다.
- O(1) -> O(N)으로 속도가 급감


---
### 좋은 hashCode를 작성하는 간단한 요령

1. int 변수 result를 선언한 후 값 c로 초기화
   - 이때 c는 해당 객체의 첫 번째 핵심 필드를 단계 2.a 방식으로 계산한 해시코드
     - 위의 핵심 필드란 **equals 비교에 사용된 필드**를 뜻함.

2. 해당 객체의 나머지 핵심 필드 f 각각에 대해 다음 작업을 수행
    - 1. 해당 필드의 해시코드 c를 계산한다.
      - 1. 기본 타입 필드라면, Type.hashcode(f)를 수행. 여기서 Type은 해당 기본타입의 박싱 클래스
      - 2. 참조 타입 필드면서 이 클래스의 equals 메서드가 이 필드의 equals 를 재귀적으로 호출해 비교한다면, 
        - 이 필드의 hashCode를 호출한다.
        - 필드의 값이 null 이면 0을 사용(다른 상수도 괜ㅊ낳지만 전통적으로 0을 사용)
      - 3. 필드가 배열이라면, 핵심 원소 각각을 별도 필드처럼 다룬다.
        - 이상의 규칙을 재귀적으로 적용해 각 핵심원소의 해시코드를 계산한 다음, 
          - 단계 2.b 방식으로 갱신한다. 배열에 핵심 원소가 하나도 없다면 단순히 상수(0을 추천한다)를 사용
          - 모든 원소가 핵심 원소라면 Arrays.hashCode를 사용
    - 1. 단계 2.1 에서 계산한 해시코드 c로 result를 갱신한다.
      - `result = 31 * result + c;`
3. result 를 반환한다.

- 이를 바탕으로 만든 hashCode 메서드 
```java
  @Override
  public int hashCode() {
    int result = Short.hashCode(areaCode);
    result = 31 * result + Short.hashCode(prefix);
    result = 31 * result + Short.hashCode(lineNum);
    return result;
  }
```
- 여기서 31은 임의의 숫자로 31을 이용하면, 이 곱셈을 시프트 연산과 뺄셈으로 대체해 최적화 할 수 있다.
- 이 과정이 비결정적 요소는 전혀 없으므로 동치인 PhoneNumber 인스턴스들은 같은 해시 코드를 가질 것이다.



단, 해시 충돌이 더욱 적은 방법을 꼭 써야겠다면, 구아바의 com.google.common.hash.Hashing을 참고하자

---
- Objects 클래스는 임의의 개수만큼 객체를 받아 해시코드를 계산해주는 정적 메서드인 `hash`를 제공한다.
- 위에서 구현한 코드와 비슷한 수준의 hashCode 함수를 한 줄로 작성할 수있다.
  - 하지만 속도는 조금 더 느리다.
  - 그러니 성능에 민감하지 않은 상황에서만 사용하자.

```java
import java.util.Objects;

@Override
public int hashCode() {
  return Objects.hash(lineNum, prefix, areaCode);
}
```
- 클래스가 불변이고 해시코드를 계산하는 비용이 크다면
  - 매번 새로 계산하기 보다는 **캐싱하는 방식을 고려**
  - 해시의 키로 사용되지 않는 경우라면 hashCode가 처음 불릴때 계산하는 **지연 초기화** 전략은 어떨까?
- 필드를 지연 초기화하려면 그 클래스를 스레드 안전하게 만들도록 신경써야 한다. (item_83)
  - 지연초기화 하려면 hashCode 필드의 초깃값은 흔히 생성되는 객체의 해시코드와는 달라야함에 유념

```java
  private int hashCode; //자동으로 0으로 초기화

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = Short.hashCode(areaCode);
      result = 31 * result + Short.hashCode(prefix);
      result = 31 * result + Short.hashCode(lineNum);
    }
    return result;
  }
```

**성능을 높인답시고 해시코드를 계산할 때 핵심 필드를 생략해서는 안된다.**
- 해시 품질이 나빠져 해시테이블의 성능을 심각하게 떨어뜨릴 수도 있다.


**hashCode가 반환하는 값의 생성 규칙을 API 사용자에게 자세히 공표하지 말자. 그래야 클라이언트가 이 값에 의지하지 않게 되고, 추후에 계산 방식을 바꿀 수도 있다.**


# 핵심정리
> equals를 재정의할 때는 hashCode도 반드시 재정의해야 한다. 그렇지 않으면 프로그램이 제대로 동작하지 않을 것
> 
> 재정의한 hashCode는 Object의 API문서에 기술된 일반 규약을 따라야 하며,
> 
> 서로 다른 인스턴스라면 되도록 해시코드는 서로 다르게 구현해야한다.