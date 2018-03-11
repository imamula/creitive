package ivan.mamula.creitive.Utils.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivan Mamula on 3/10/18.
 * mamula82@gmail.com
 * Skype id: mamula82
 * +381(0)642035511
 */
public class BlogModel {
    @SerializedName("content")
    @Expose
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
