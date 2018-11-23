package com.example.android.maximfialko.data;

public enum Category {
    HOME("home"),
    OPINION("opinion"),
    WORLD("world"),
    NATIONAL("national"),
    POLITICS("politics"),
    UPSHOT("upshot"),
    BUSINESS("business"),
    TECHNOLOGY("technology"),
    SCIENCE("science"),
    HEALTH("health"),
    ARTS("arts"),
    BOOKS("books"),
    MOVIES("movies"),
    THEATER("theater"),
    SUNDAYREVIEW("sundayreview"),
    FASHION("fashion"),
    TMAGAZINE("tmagazine"),
    FOOD("food"),
    TRAVEL("travel"),
    MAGAZINE("magazine"),
    REALESTATE("realestate"),
    AUTOMOBILES("automobiles"),
    OBITUARIES("obituaries"),
    INSIDER("insider");

    final String category;

    Category(String ctgr) {
        category = ctgr;
    }

    public String getCategory() {
        return category;
    }

}