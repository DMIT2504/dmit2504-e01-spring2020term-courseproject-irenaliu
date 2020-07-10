package ca.nait.dmit2504.foodlookup.foodapi;

public class Food {
    private String foodId;

    private String label;

    private Nutrient mNutrient;

    private String category;

    private String categoryLabel;

    private String image;

    public void setFoodId(String foodId){
        this.foodId = foodId;
    }
    public String getFoodId(){
        return this.foodId;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getLabel(){
        return this.label;
    }
    public void setNutrient(Nutrient nutrient){
        this.mNutrient = nutrient;
    }
    public Nutrient getNutrient(){
        return this.mNutrient;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getCategory(){
        return this.category;
    }
    public void setCategoryLabel(String categoryLabel){
        this.categoryLabel = categoryLabel;
    }
    public String getCategoryLabel(){
        return this.categoryLabel;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
}
