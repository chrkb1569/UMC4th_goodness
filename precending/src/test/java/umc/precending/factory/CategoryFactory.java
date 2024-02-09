package umc.precending.factory;

import umc.precending.domain.category.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static umc.precending.domain.category.Category.*;

public enum CategoryFactory {
    CATEGORY_VALUE_1(CATEGORY_1.getCategoryName()),
    CATEGORY_VALUE_2(CATEGORY_2.getCategoryName()),
    CATEGORY_VALUE_3(CATEGORY_3.getCategoryName()),
    CATEGORY_VALUE_4(CATEGORY_4.getCategoryName()),
    CATEGORY_VALUE_5(CATEGORY_5.getCategoryName())
    ;
    private String categoryName;

    CategoryFactory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public static List<Category> getRandomCategoryList() {
        final int firstIndex = 0;
        final int secondIndex = 1;

        List<Integer> randomNumber = new ArrayList<>();

        for(int number = 0; number < values().length; number++) {
            randomNumber.add(number);
        }

        Collections.shuffle(randomNumber);

        Category firstCategory = convertToCategory(randomNumber.get(firstIndex));
        Category secondCategory = convertToCategory(randomNumber.get(secondIndex));

        return List.of(firstCategory, secondCategory);
    }

    public static Category getRandomCategory() {
        Random random = new Random();
        int index = random.nextInt() % values().length;

        return convertToCategory(index);
    }

    private static Category convertToCategory(int index) {
        String key = getConvertKey(index);
        return Category.getCategoryByName(key);
    }

    private static String getConvertKey(int index) {
        return values()[index].getCategoryName();
    }
}
