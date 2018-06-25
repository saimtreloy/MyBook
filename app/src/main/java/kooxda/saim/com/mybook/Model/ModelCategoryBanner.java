package kooxda.saim.com.mybook.Model;

/**
 * Created by NREL on 6/25/18.
 */

public class ModelCategoryBanner {
    String id, category_id, cover, category_name;

    public ModelCategoryBanner(String id, String category_id, String cover) {
        this.id = id;
        this.category_id = category_id;
        this.cover = cover;
    }

    public ModelCategoryBanner(String id, String category_id, String cover, String category_name) {
        this.id = id;
        this.category_id = category_id;
        this.cover = cover;
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getCover() {
        return cover;
    }

    public String getCategory_name() {
        return category_name;
    }
}
