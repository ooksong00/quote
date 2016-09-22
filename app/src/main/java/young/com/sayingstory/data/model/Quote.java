package young.com.sayingstory.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.annotation.meta.Exclusive;

@Generated("org.jsonschema2pojo")
public class Quote implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("author")
    @Expose
    public String author;

    @SerializedName("quote_text")
    @Expose
    public String quoteText;

    @SerializedName("is_visible")
    @Expose
    public String isVisible;

    @SerializedName("created_at")
    @Exclusive
    public String createdAt;

    public int backgroundImgNumber;
    public boolean isEnterByPush;

    public boolean getIsEnterByPush() {
        return isEnterByPush;
    }

    public void setIsEnterByPush(boolean isEnterByPush) {
        this.isEnterByPush = isEnterByPush;
    }

    public int getBackgroundImgNumber() {
        return backgroundImgNumber;
    }

    public void setBackgroundImgNumber(int backgroundImgNumber) {
        this.backgroundImgNumber = backgroundImgNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
