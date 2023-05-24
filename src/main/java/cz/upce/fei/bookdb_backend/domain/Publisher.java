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
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Book-publisher
    @OneToMany(mappedBy = "publisher", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<Book> books = Collections.emptyList();
    // -----------------------
}
