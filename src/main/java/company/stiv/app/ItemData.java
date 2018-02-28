package company.stiv.app;

/**
 * Created by Stiv on 28/02/2018.
 */

public class ItemData {


    private final String text;
    private final Integer imageId;

    public ItemData(String text, Integer imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public Integer getImageId() {
        return imageId;
    }
}
