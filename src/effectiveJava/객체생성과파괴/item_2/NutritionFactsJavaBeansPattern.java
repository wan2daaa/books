package effectiveJava.객체생성과파괴.item_2;

public class NutritionFactsJavaBeansPattern {

  //필수
  private int servingSize = -1;
  private int servings = -1;
  //선택
  private int calories = 0;
  private int fat = 0;
  private int sodium = 0;
  private int carbohydrate = 0;

  public NutritionFactsJavaBeansPattern() {}

  public void setServingSize(int servingSize) {
    this.servingSize = servingSize;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }

  public void setCalories(int calories) {
    this.calories = calories;
  }

  public void setFat(int fat) {
    this.fat = fat;
  }

  public void setSodium(int sodium) {
    this.sodium = sodium;
  }

  public void setCarbohydrate(int carbohydrate) {
    this.carbohydrate = carbohydrate;
  }

  public static void main(String[] args) {
    NutritionFactsJavaBeansPattern cocaCola = new NutritionFactsJavaBeansPattern();
    cocaCola.setServings(240);
    cocaCola.setServings(8);
    cocaCola.setCalories(100);
    cocaCola.setSodium(35);
    cocaCola.setCarbohydrate(27);
  }
}
