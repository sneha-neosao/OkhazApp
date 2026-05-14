package ae.okhaz.boss.Model;

/**
 * Created by Avinash on 27,November,2020
 */
public class Categories {
    String  categoryName,categorySName;

    public Categories(){

    }

    public Categories(String categoryName, String categorySName) {
        this.categoryName = categoryName;
        this.categorySName = categorySName;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categoryName='" + categoryName + '\'' +
                ", categorySName='" + categorySName + '\'' +
                '}';
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySName() {
        return categorySName;
    }

    public void setCategorySName(String categorySName) {
        this.categorySName = categorySName;
    }
}
