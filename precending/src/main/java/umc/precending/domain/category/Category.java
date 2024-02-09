package umc.precending.domain.category;

import lombok.Getter;
import umc.precending.exception.category.CategoryNotFoundException;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Category {
    CATEGORY_1("기부"),
    CATEGORY_2("돕기"),
    CATEGORY_3("배려"),
    CATEGORY_4("용기"),
    CATEGORY_5("양보")
    ;

    public final static int MIN_RANGE = 1;
    public final static int MAX_RANGE = 2;
    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public static Category getCategoryByName(String categoryName) {
        Optional<Category> category = Arrays.stream(values())
                .filter(value -> value.getCategoryName().equals(categoryName))
                .findFirst();

        if(category.isEmpty()) throw new CategoryNotFoundException();

        return category.get();
    }
}