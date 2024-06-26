package effectiveJava.객체생성과파괴.item_2;

public class NutritionFactsBuilderPattern {

  //필수
  private final int servingSize;
  private final int servings;
  //선택
  private final int calories;
  private final int fat;
  private final int sodium;
  private final int carbohydrate;

  public static class Builder {

    //필수 매개변수
    private final int servingSize;
    private final int servings;

    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public Builder(int servingSize, int servings) {
      this.servingSize = servingSize;
      this.servings = servings;
    }

    public Builder calories(int val) {
      calories = val;
      return this;
    }

    public Builder fat(int val) {
      fat = val;
      return this;
    }

    public Builder sodium(int val) {
      sodium = val;
      return this;
    }

    public Builder carbohydrate(int val) {
      carbohydrate = val;
      return this;
    }

    public NutritionFactsBuilderPattern build() {
      return new NutritionFactsBuilderPattern(this);
    }
  }

  private NutritionFactsBuilderPattern(Builder builder) {
    this.servingSize = builder.servingSize;
    this.servings = builder.servings;
    this.calories = builder.calories;
    this.fat = builder.fat;
    this.sodium = builder.sodium;
    this.carbohydrate = builder.carbohydrate;
  }

  public static void main(String[] args) {
    NutritionFactsBuilderPattern cocaCola = new Builder(240, 8)
        .calories(100)
        .sodium(35)
        .carbohydrate(27)
        .build();
  }
}
