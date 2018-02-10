package com.shrikanthravi.smartmirror.data.model;

/**
 * Created by shrikanthravi on 11/12/17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoogleNewsIndia {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("articles")
    @Expose
    private List<Article> articles = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}
