package com.jaago.jaago;

public class Post {
    private String Title;
    private String Desc;
    private String Article;
    private String Img;

    public Post(){}

    public Post(String Title, String Desc, String Article, String Img) {
        this.Title = Title;
        this.Desc = Desc;
        this.Article = Article;
        this.Img = Img;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public String getArticle() {
        return Article;
    }

    public String getImg() {
        return Img;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setArticle(String article) {
        Article = article;
    }

    public void setImg(String img) {
        Img = img;
    }
}
