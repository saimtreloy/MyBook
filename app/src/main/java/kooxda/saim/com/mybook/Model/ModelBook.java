package kooxda.saim.com.mybook.Model;

/**
 * Created by NREL on 6/10/18.
 */

public class ModelBook {
    String id, category_name, cover;

    public ModelBook(String id, String category_name, String cover) {
        this.id = id;
        this.category_name = category_name;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCover() {
        return cover;
    }
}
