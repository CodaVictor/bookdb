package cz.upce.fei.bookdb_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.upce.fei.bookdb_backend.dto.CategoryResponseDtoV1;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1024)
    private String description;

    // Book-category
    @OneToMany(mappedBy = "category", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Book> books = Collections.emptyList();
    // -----------------------

    public CategoryResponseDtoV1 toDto() {
        return new CategoryResponseDtoV1(
                getId(),
                getName(),
                getDescription()
        );
    }
}
