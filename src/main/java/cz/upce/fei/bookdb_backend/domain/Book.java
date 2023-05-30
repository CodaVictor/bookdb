package cz.upce.fei.bookdb_backend.domain;

import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String subtitle;

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
    @ManyToOne()
    @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    private Publisher publisher;
    // -----------------------

    // Books-authors
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @EqualsAndHashCode.Exclude
    private List<Author> authors = Collections.emptyList();
    // -----------------------

    // Book-category
    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;
    // -----------------------

    // Book-genre
    @ManyToOne
    @JoinColumn(name = "genre_id")
    @ToString.Exclude
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
                getPublisher().toDto(),
                getCategory().toDto(),
                getGenre().toDto(),
                getAuthors().stream().map(Author::toDto).collect(Collectors.toList()),
                0,null
        );
    }
}
