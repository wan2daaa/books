package effectiveJava.모든객체의공통메서드.item_10;

public class ColorPoint extends Point {

  private final Color color;

  public ColorPoint(int x, int y, Color color) {
    super(x, y);
    this.color = color;
  }

   public enum Color {
    RED, BLUE
  }

  // 대칭성 위배
//  @Override
//  public boolean equals(Object o) {
//    if (!(o instanceof ColorPoint))
//      return false;
//
//    return super.equals(o) && ((ColorPoint) o).color == color;
//  }
  //추이성 위배
//  @Override
//  public boolean equals(Object o) {
//    if (!(o instanceof Point))
//      return false;
//
//    // o가 일반 Point면 색상을 무시하고 비교한다.
//    if (!(o instanceof ColorPoint))
//      return o.equals(this);
//
//    // o가 ColorPoint 이면 색상까지 비교한다.
//    return super.equals(o) && ((ColorPoint) o).color == color;
//  }

  public static void main(String[] args) {
    ColorPoint p1 = new ColorPoint(1,2, Color.RED);
    Point p2 = new Point(1, 2);
    ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);

    System.out.println("p1.equals(p2) = " + p1.equals(p2));
    System.out.println("p2.equals(p3) = " + p2.equals(p3));
    System.out.println("p1.equals(p3) = " + p1.equals(p3));
  }
}
