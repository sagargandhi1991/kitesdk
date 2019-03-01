package ly.kite.cliqueshare;

import java.util.List;

public class BoardName {

    private String boardname;
    private List<BoardImageModel> mImages;

    public String getBoardname() {
        return boardname;
    }

    public void setBoardname(String boardname) {
        this.boardname = boardname;
    }

    public List<BoardImageModel> getmImages() {
        return mImages;
    }

    public void setmImages(List<BoardImageModel> mImages) {
        this.mImages = mImages;
    }

    @Override
    public String toString() {
        return boardname;
    }
}
