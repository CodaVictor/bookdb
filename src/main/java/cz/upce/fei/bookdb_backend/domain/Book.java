package cz.upce.fei.bookdb_backend.domain;

import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Integer subtitle;

    @Column
    private String isbn;

    @Column
    private String language;

    @Column
    private LocalDateTime publicationDate;

    @Column
    private Integer pageCount;

    @Column(length = 1024)
    private String description;

    @Column
    private String filename;

    // Book-reviews
    @OneToMany(mappedBy = "book", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<Review> reviews = Collections.emptyList();
    // -----------------------

    // Book-publisher
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    @JsonIgnore
    private Publisher publisher;
    // -----------------------

    // Books-authors
    @ManyToMany(mappedBy = "books")
    @EqualsAndHashCode.Exclude
    private List<Author> authors = Collections.emptyList();
    // -----------------------

    // Book-category
    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @JsonIgnore
    private Category category;
    // -----------------------

    // Book-genre
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @ToString.Exclude
    @JsonIgnore
    private Genre genre;
    // -----------------------

    public BookResponseDtoV1 toDto() {
        return new BookResponseDtoV1(
                getId(),
                getTitle(),
                getSubtitle(),
                getIsbn(),
                getLanguage(),
                getPublicationDate(),
                getPageCount(),
                getDescription(),
                0,null
        );
    }
}
