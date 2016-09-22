package young.com.sayingstory.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class QuoteComment {

    @SerializedName("comment")
    @Expose
    String comment;

    @SerializedName("user_img")
    @Expose
    int userImg;

    @SerializedName("created_at")
    @Expose
    String createdAt;

    @SerializedName("user_id")
    @Expose
    String userId;

    public QuoteComment(String comment, int userImg, String createdAt, String userId) {
        this.comment = comment;
        this.userImg = userImg;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setTitle(String title) {
        this.comment = title;
    }

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userId) {
        this.userImg = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
