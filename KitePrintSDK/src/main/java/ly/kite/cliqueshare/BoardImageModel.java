package ly.kite.cliqueshare;

public class BoardImageModel {

    private String url;
    private boolean isSelected = false;

    public BoardImageModel(String text) {
        this.url = text;
    }

    public String getImageUrl() {
        return url;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
