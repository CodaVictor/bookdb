package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDtoV1 {

    @NotBlank(message = "Title is required.")
    @Length(max = 255)
    private String title;

    @Length(max = 255)
    private String subtitle;

    @Length(max = 255)
    private String isbn;

    @Length(max = 255)
    private String language;

    private LocalDateTime publicationDate;

    private Integer pageCount;

    @Length(max = 1024)
    private String description;

    @Length(max = 255)
    private String filename;

    //
    private Long category;

    private Long genre;

    private Long publisher;
    private List<Long> authors;
}
