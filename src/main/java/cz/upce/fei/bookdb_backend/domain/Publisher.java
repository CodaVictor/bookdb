package cz.upce.fei.bookdb_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.upce.fei.bookdb_backend.dto.PublisherResponseDtoV1;
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
    @JsonIgnore
    private List<Book> books = Collections.emptyList();
    // -----------------------

    public PublisherResponseDtoV1 toDto() {
        return new PublisherResponseDtoV1(
                getId(),
                getName()
        );
    }
}
