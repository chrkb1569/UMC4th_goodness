package umc.precending.domain.post;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PrecedingDate {
    @Column(name = "preceding_year", nullable = false)
    private int year;

    @Column(name = "preceding_month", nullable = false)
    private int month;

    @Column(name = "preceding_date", nullable = false)
    private int date;
}
