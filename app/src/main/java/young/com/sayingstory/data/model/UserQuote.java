package young.com.sayingstory.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UserQuote implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("user_id")
    @Expose
    public String userId;

    @SerializedName("quote_text")
    @Expose
    public String quoteText;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    public int backgroundImgNumber;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
