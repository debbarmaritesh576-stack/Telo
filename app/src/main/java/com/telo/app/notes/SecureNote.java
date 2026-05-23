package com.telo.app.notes;

import java.util.UUID;

public class SecureNote {

    private String  id;
    private String  title;
    private String  content;
    private String  categoryId;
    private boolean isFavorite;
    private boolean isPinned;
    private String  colorHex;
    private long    createdAt;
    private long    updatedAt;

    public SecureNote() {
        this.id        = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.colorHex  = "#FFFFFF";
    }

    public String getId()          { return id; }
    public String getTitle()       { return title; }
    public String getContent()     { return content; }
    public String getCategoryId()  { return categoryId; }
    public boolean isFavorite()    { return isFavorite; }
    public boolean isPinned()      { return isPinned; }
    public String getColorHex()    { return colorHex; }
    public long getCreatedAt()     { return createdAt; }
    public long getUpdatedAt()     { return updatedAt; }

    public void setId(String id)               { this.id = id; }
    public void setTitle(String title)         { this.title = title; }
    public void setContent(String content)     { this.content = content; }
    public void setCategoryId(String cat)      { this.categoryId = cat; }
    public void setFavorite(boolean fav)       { this.isFavorite = fav; }
    public void setPinned(boolean pinned)      { this.isPinned = pinned; }
    public void setColorHex(String colorHex)   { this.colorHex = colorHex; }
    public void setCreatedAt(long createdAt)   { this.createdAt = createdAt; }
    public void setUpdatedAt(long updatedAt)   { this.updatedAt = updatedAt; }

    public void touch() {
        this.updatedAt = System.currentTimeMillis();
    }
}