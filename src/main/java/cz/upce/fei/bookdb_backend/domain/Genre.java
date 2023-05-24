package cz.upce.fei.bookdb_backend.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String genreName;

    @Column(length = 1024)
    private String description;

    // Book-genre
    @OneToMany(mappedBy = "genre", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<Book> books = Collections.emptyList();
    // -----------------------
}
