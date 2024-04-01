package effectiveJava.모든객체의공통메서드.item_10;

import java.util.Objects;

public final class PhoneNumber {
  private final short areaCode, prefix, lineNum;

  public PhoneNumber(int areaCode, int prefix, int lineNum) {
    this.areaCode = rangeCheck(areaCode, 999 , "지역코드");
    this.prefix = rangeCheck(prefix, 999, "프리픽스");
    this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
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

//  @Override
//  public int hashCode() {
//    return 42;
//  }

//  @Override
//  public int hashCode() {
//    int result = Short.hashCode(areaCode);
//    result = 31 * result + Short.hashCode(prefix);
//    result = 31 * result + Short.hashCode(lineNum);
//    return result;
//  }

//  @Override
//  public int hashCode() {
//    return Objects.hash(lineNum, prefix, areaCode);
//  }

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

}
