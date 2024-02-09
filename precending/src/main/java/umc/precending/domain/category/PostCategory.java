package umc.precending.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor
public class PostCategory {
    private final String BASIC_CATEGORY = "EMPTY";
    private final static int CATEGORY_MAX_SIZE = 2;

    @Column(name = "first_category", nullable = false)
    private String firstCategory;
    @Column(name = "second_category", nullable = false)
    private String secondCategory;

    public PostCategory(List<Category> categoryList) {
        if(categoryList.size() < CATEGORY_MAX_SIZE) {
            this.firstCategory = categoryList.get(0).getCategoryName();
            this.secondCategory = BASIC_CATEGORY;
            return;
        }

        this.firstCategory = categoryList.get(0).getCategoryName();
        this.secondCategory = categoryList.get(1).getCategoryName();
    }

    public List<String> getCategoryList() {
        List<String> categoryList = new ArrayList<>();

        categoryList.add(firstCategory);
        if(!secondCategory.equals(BASIC_CATEGORY)) categoryList.add(secondCategory);

        return categoryList;
    }
}
