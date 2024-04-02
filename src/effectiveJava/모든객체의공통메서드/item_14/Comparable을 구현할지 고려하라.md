# Comparable을 구현할지 고려하라

## 핵심정리 
> 순서를 고려해야 하는 값 클래스를 작성한다면 꼭 Comparable 인터페이스를 구현하여,
> 
> 그 인스턴스를 쉽게 정렬하고, 검색하고, 비교 기능을 제공하는 컬렉션과 어울려지도록 한다.
> 
> compareTo 메서드에서 필드의 값을 비교할 때 `<` 와 `>` 연산자는 쓰지 말아야 한다.
> 
> 그 대신 박싱된 기본 타입 클래스가 제공하는 정적 compare 메서드나 Comparator 인터페이스가 제공하는 비교자 생성 메서드를 사용하자.

---

Comparable 인터페이스의 유일무이한 메서드인 `compareTo`를 알아보자.

- `compareTo` 는 Obejct의 메서드가 아니다.
- 성격은 두가지만 빼면 Object의 equals와 같다.

1. compareTo는 단순 동치성 비교에 더해 순서까지 비교할 수 있으며,
2. 제네릭하다.

---
- Comparable을 구현했다는 것은 그 **클래스의 인스턴스에게 자연적인 순서가 있음을 뜻한다.**
  - 그래서 Comparable을 구현한 객체들의 배열은 다음처럼 손쉽게 정렬 가능하다.
    - `Arrays.sort(a);`

---
Comparable을 구현하면 이 인터페이스를 활용하는 **수많은 제네릭 알고리즘과 컬렉션의 힘을 쓸 수있다.**

- 사실상 자바 플랫폼 라이브러리의 모든 값 클래스와 열거 타입이 Comparable을 구현되어있다
- **알파, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면, 반드시 Comparable 인터페이스를 구현하자.**

---
compareTo 메서드의 일반 규약은 equals의 규약과 비슷하다

1. 두객체 참조의 순서를 바꿔 비교해도 예상한 결과가 나와야 한다.
2. 첫 번째가 두번째보다 크고 두 번째가 세 번째보다 크면, 첫 번째는 세 번째보다 커야한다.
3. 크기가 같은 객체들 끼리는 어떤 객체와 비교하더라도 항상 같아야한다.
4. 이 권고는 필수는 아니지만 꼭 지키는게 좋다. `(x.compareTo(y) == 0) == (x.equals(y)`

1,2,3 번은 equals의 규약에도 있는 반사성, 대칭성, 추이성을 충족해야함을 뜻한다.

마지막 규약은 compareTo 메서드로 수행한 동치성 테스트의 결과가 equals의 결과와 같아야 한다는 뜻
- 만약 같지 않을시, 이 클래스의 객체를 정렬된 컬렉션에 넣으면
  - -> 해당 컬렉션이 구현한 인터페이스(Collection, Set, Map)에 정의된 동작과 엇박자를 낼 것
  - 위 인터페이스들은 equals 메서드의 규약을 따른다고 되어있지만,
    - 놀랍게도 **정렬된 컬렉션들은 동치성을 비교할 때** equals 대신 **compareTo를 사용하기 때문**

    
---

### compareTo 작성요령 
- Comparable은 타입을 인수로 받는 제네릭 인터페이스 
  - compareTo 메서드의 인수타입은 컴파일타임에 정해짐
    - 입력 인수의 타입을 확인하거나 형변환할 필요가 없다는 뜻
- compareTo 메서드는 각 필드가 동치인지를 비교하는게 아니라 **그 순서를 비교**
  - Comparable을 구현하지 않은 필드나 표준이 아닌 순서로 비교해야 한다면
    - Comparator를 대신 사용 

```java
public final class CaseInsensitiveString implements Comparable<CaseInsensitiveString>{
  private final String s;

  @Override
  public int compareTo(CaseInsensitiveString cis) {
    return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
  }
}
```
---
박싱된 기본 타입 클래스들에 새로 추가된 정적 메서드인 compare를 이용하면 되는 것
- **compareTo 메서드에서 관계 연산자 < 와 >를 사용하는 이전 방식은 거추장스럽고 오류를 유발하니, 이제는 추천하지 않는다.**

---
###### 클래스에 핵심 필드가 여러 개라면 어느 것을 먼저 비교하냐가 중요
- 가장 핵심적인 필드부터 비교해 나가자
```java
public int compareTo(PhoneNumber pn) {
  int result = Short.compare(areaCode, pn.areaCode); //가장 중요한 필드
  if (result == 0) {
    result = Short.compare(prefix, pn.prefix); // 두 번째로 중요한 필드
    if (result == 0) {
      result = Short.compare(lineNum, pn.lineNum); // 세 번째로 중요한 필드
    }
  }
}
```
---
##### 자바 8에서는 **Comparator** 인터페이스가 일련의 비교자 생성 메서드와 팀을 꾸려 메서드 연쇄 방식으로 비교자를 생성할 수 있게 되었다.
- 간결하지만, 약간의 성능 저하가 뒤따른다.
- 자바의 정적 임포트 기능을 이용하면 
- 정적 비교자 생성 메서드들을 그이름으로만 사용할 수 있어 코드가 훨씬 깔끔

```java
import effectiveJava.모든객체의공통메서드.item_10.PhoneNumber;
import java.util.Comparator;

import static java.util.Comparator.comparingInt;

private static final Comparator<PhoneNumber> COMPARATOR =
        comparingInt((PhoneNumber pn) -> pn.areaCode)
                .thenComparingInt(pn -> pn.prefix)
                .thenComparingInt(pn -> pn.lineNum)

public int compareTo(PhoneNumber phoneNumber) {
  return COMPARATOR.compare(this, pn);
}
```
- Comparator는 수많은 보조 생성 메서드들로 중무장하고 있다.

---
이따금 값의 차를 기준으로 첫 번째 값이 두 번째값 보다 작으면 음수를, 두 값이 같으면 0 , 첫번째 값이 크면 양수를 반환하는 compareTo나 compare 메서드와 마주할 것

```java
import java.util.Comparator;

// 사용하지 말자!
static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
  @Override
  public int compare(Object o1, Object o2) {
    return o1.hashCode() - o2.hashCode();
  }
}
```
- **이방식은 사용하면 안된다!**
- 이방식은 정수 오버플로를 일으키거나 부동소수점 계산방식에 따른 오류를 낼 수 있다.
  - 이 방식 대신 다음 방식을 이용하자

```java
import java.util.Comparator;

static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
  @Override
  public int compare(Object o1, Object o2) {
    return Integer.compare(o1.hashCode(), o2.hashCode());
  }
}

static Comparator<Object> hashCodeError =
        Comparator.comparingInt(o -> o.hashCode())
```
