package study.project.whereareyou.NavigationDrawer;

/**
 * Created by Administrator on 26/10/2015.
 */
public class NavigationDrawerItem {
    String title;
    int imageResourceId;
    public NavigationDrawerItem() {
    }

    public NavigationDrawerItem(String title, int imageResourceId) {
        this.title = title;
        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
