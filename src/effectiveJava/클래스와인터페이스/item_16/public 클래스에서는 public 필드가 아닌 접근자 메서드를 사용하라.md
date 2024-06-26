# public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라

## 핵심정리 
> public 클래스는 절대 가변 필드를 직접 노출해서는 안된다.
> 
> 불변 필드라면 노출해도 덜 위험하지만 완전 안심할 수는 없다.
> 
> package-private 클래스나 private 중첩 클래스에서는 종종(불변이든 가변이든) 필드를 노출 시키는 편이 나을 때도 있다.

---
가끔 인스턴스 필드들만 모아놓은 클래스를 작성하고 싶을 때가 있다.
```java
class Point {
  public double x;
  public double y;
}
```
이런 클래스는 데이터 필드에 직접 접근 할 수 있으니 **캡슐화의 이점을 제공하지 못한다**. (item_15)
- API 수정하지 않고는 내부 표현 바꿀 수 없고,
- 불변식 보장 X
- 외부에서 필드에 접근할 때 부수 작업을 수행할 수도 없다.
- 객체지향 프로그래머라면 필드를 private으로 바꾸고 getter를 추가한다.

```java
class Point {
  private double x;
  private double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }
}
```
public 클래스에서라면 이 방식이 확실히 맞다.
- **패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공함 으로써 클래스 내부 표현 방식을 언제든 바꿀 수 있는 유연성을 얻을 수 있다.**


**하지만 package-private 클래스 혹은 private 중첩 클래스라면 데이터 필드를 노출한다 해도 문제가 없다.**
- 그 클래스가 표현하려는 추상 개념만 올바르게 표현해주면 된다.
- 이 방식은 클래스 선언 면에서나 이를 사용하는 클라이언트 코드 면에서나 접근자 방식보다 훨씬 깔끔
- 따라서 패키지 바깥 코드는 전혀 손대지 않고도 데이터 표현 방식을 바꿀 수 있다.
- private 중첩 클래스의 경우 -> 수정 범위가 더 좁아져서 이 클래스를 포함하는 외부 클래스까지로 제한된다.


---
public 클래스의 필드가 불변이라면 직접 노출할 때의 단점이 조금은 줄어들지만, 여전히 결코 좋은 생각이 아니다.
- API를 변경하지 않고는 표현방식을 바꿀 수 없고, 필드를 읽을 때 부수 작업을 수행할 수없다는 단점은 여전하다.
  - 단, 불변식은 보장할 수 있게 된다.