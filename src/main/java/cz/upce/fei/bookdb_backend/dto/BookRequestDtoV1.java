package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDtoV1 {

    private Long id;

    @NotNull
    private String title;

    private Integer subtitle;

    private String isbn;

    private String language;

    private LocalDateTime publicationDate;

    private Integer pageCount;

    private String description;

    private String filename;

    //
    private Long category;

    private Long genre;

    private Long publisher;
    private List<Long> authors;
}
