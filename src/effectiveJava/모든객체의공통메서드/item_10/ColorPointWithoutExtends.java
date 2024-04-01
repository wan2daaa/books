package effectiveJava.모든객체의공통메서드.item_10;

import java.util.Objects;

public class ColorPointWithoutExtends {

  private final Point point;
  private final Color color;

  public ColorPointWithoutExtends(int x , int y, Color color) {
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


  public enum Color {
    RED, BLUE
  }
}
