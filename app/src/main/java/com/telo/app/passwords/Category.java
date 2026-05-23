package com.telo.app.passwords;

public class Category {

    private String id;
    private String name;
    private String iconRes;
    private String colorHex;
    private int    sortOrder;
    private long   createdAt;

    public Category() {
        this.createdAt = System.currentTimeMillis();
    }

    public Category(String id, String name, String iconRes,
                    String colorHex, int sortOrder) {
        this.id        = id;
        this.name      = name;
        this.iconRes   = iconRes;
        this.colorHex  = colorHex;
        this.sortOrder = sortOrder;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId()        { return id; }
    public String getName()      { return name; }
    public String getIconRes()   { return iconRes; }
    public String getColorHex()  { return colorHex; }
    public int getSortOrder()    { return sortOrder; }
    public long getCreatedAt()   { return createdAt; }

    public void setId(String id)            { this.id = id; }
    public void setName(String name)        { this.name = name; }
    public void setIconRes(String iconRes)  { this.iconRes = iconRes; }
    public void setColorHex(String color)   { this.colorHex = color; }
    public void setSortOrder(int order)     { this.sortOrder = order; }
    public void setCreatedAt(long time)     { this.createdAt = time; }
}