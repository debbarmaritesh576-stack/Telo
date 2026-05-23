package com.telo.app.passwords;

public enum CategoryType {

    ALL(      "all",      "ic_category_all",      "#6200EE"),
    HOME(     "home",     "ic_category_home",     "#FF5722"),
    WORK(     "work",     "ic_category_work",     "#2196F3"),
    PERSONAL( "personal", "ic_category_personal", "#4CAF50"),
    BANKING(  "banking",  "ic_category_banking",  "#FF9800"),
    SOCIAL(   "social",   "ic_category_social",   "#E91E63");

    private final String id;
    private final String iconRes;
    private final String colorHex;

    CategoryType(String id, String iconRes, String colorHex) {
        this.id       = id;
        this.iconRes  = iconRes;
        this.colorHex = colorHex;
    }

    public String getId()       { return id; }
    public String getIconRes()  { return iconRes; }
    public String getColorHex(){ return colorHex; }

    public Category toCategory() {
        Category c = new Category();
        c.setId(id);
        c.setName(name().charAt(0) + name().substring(1).toLowerCase());
        c.setIconRes(iconRes);
        c.setColorHex(colorHex);
        c.setSortOrder(ordinal());
        return c;
    }
}