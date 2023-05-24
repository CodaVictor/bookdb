package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDtoV1 {
    private Long id;

    private String title;

    private Integer subtitle;

    private String isbn;

    private String language;

    private LocalDateTime publicationDate;

    private Integer pageCount;

    private String description;

    private long reviewCount;
    private Long rating;
}
