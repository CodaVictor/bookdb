package cz.upce.fei.bookdb_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.upce.fei.bookdb_backend.dto.AuthorResponseDtoV1;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private LocalDateTime birthdate;

    @Column(length = 1024)
    private String description;

    // Books-authors
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<Book> books = Collections.emptyList();
    // -----------------------

    public AuthorResponseDtoV1 toDto() {
        return new AuthorResponseDtoV1(
                getId(),
                getFirstName(),
                getLastName(),
                getBirthdate(),
                getDescription()
        );
    }
}
