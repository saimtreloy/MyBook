package kooxda.saim.com.mybook.Model;

/**
 * Created by NREL on 6/11/18.
 */

public class ModelContent {
    public String id, name, banner, location, type, category, date_time;

    public ModelContent(String id, String name, String banner, String location, String type, String category, String date_time) {
        this.id = id;
        this.name = name;
        this.banner = banner;
        this.location = location;
        this.type = type;
        this.category = category;
        this.date_time = date_time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDate_time() {
        return date_time;
    }
}
