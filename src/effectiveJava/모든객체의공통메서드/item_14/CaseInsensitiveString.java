package effectiveJava.모든객체의공통메서드.item_14;

import java.util.Objects;

public final class CaseInsensitiveString implements Comparable<CaseInsensitiveString>{
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
/* 올바른 예
  @Override
  public boolean equals(Object o) {
    return o instanceof CaseInsensitiveString &&
           ((CaseInsensitiveString)o).s.equalsIgnoreCase(s);
  }
*/

  public static void main(String[] args) {
    CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
    String s = "polish";

    System.out.println(cis.equals(s));
    System.out.println(s.equals(cis));



  }

  @Override
  public int compareTo(CaseInsensitiveString cis) {
    return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
  }
}
