package client.utils;

import commons.Category;
import jakarta.inject.Inject;

import java.util.ResourceBundle;

public class CategoryUtils {

    private final LanguageService languages;

    @Inject
    public CategoryUtils(LanguageService languages) {
        this.languages = languages;
    }

    public String format(Category category){
        ResourceBundle b = languages.bundle();
        if(category == null) return "";
        if(!b.getString("common.category."+category).isEmpty()){
            return b.getString("common.category."+category);
        }
        return category.toString();
    }
}
