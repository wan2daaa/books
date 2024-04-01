package effectiveJava.모든객체의공통메서드.item_11;

import effectiveJava.모든객체의공통메서드.item_10.PhoneNumber;
import java.util.HashMap;
import java.util.Map;

public class TestPhoneNumber {

  public static void main(String[] args) {
    Map<PhoneNumber, String> m = new HashMap<>();
    m.put(new PhoneNumber(707,867,5309), "제니");

    System.out.println(
        "m.get(new PhoneNumber(707, 867, 5309)) = " + m.get(new PhoneNumber(707, 867, 5309)));
  }

}
