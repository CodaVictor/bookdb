package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDtoV1 {
    private Long id;

    private String title;

    private String subtitle;

    private String isbn;

    private String language;

    private LocalDateTime publicationDate;

    private Integer pageCount;

    private String description;

    private PublisherResponseDtoV1 publisher;

    private CategoryResponseDtoV1 category;

    private GenreResponseDtoV1 genre;

    private List<AuthorResponseDtoV1> authors;

    //
    private long reviewCount;

    private Float rating;
}
