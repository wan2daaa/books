package effectiveJava.객체생성과파괴.item_2;

public class NutritionFactsTelescopingConstructorPattern {
  //필수
  private final int servingSize;
  private final int servings;
  //선택
  private final int calories;
  private final int fat;
  private final int sodium;
  private final int carbohydrate;


  public NutritionFactsTelescopingConstructorPattern(int servingSize, int servings) {
    this(servingSize, servings, 0);
  }

  public NutritionFactsTelescopingConstructorPattern(int servingSize, int servings, int calories) {
    this(servingSize, servings, calories, 0);
  }

  public NutritionFactsTelescopingConstructorPattern(int servingSize, int servings, int calories, int fat) {
    this(servingSize, servings, calories, fat, 0);
  }

  public NutritionFactsTelescopingConstructorPattern(int servingSize, int servings, int calories, int fat, int sodium) {
    this(servingSize, servings, calories, fat, sodium, 0);
  }

  public NutritionFactsTelescopingConstructorPattern(int servingSize, int servings, int calories, int fat, int sodium,
      int carbohydrate) {
    this.servingSize = servingSize;
    this.servings = servings;
    this.calories = calories;
    this.fat = fat;
    this.sodium = sodium;
    this.carbohydrate = carbohydrate;
  }
}
