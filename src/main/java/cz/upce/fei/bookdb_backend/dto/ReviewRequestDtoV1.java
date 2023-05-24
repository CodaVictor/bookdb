package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDtoV1 {

    private String text;

    @NotNull
    private Float rating;

    @NotNull
    private LocalDateTime creationDateTime;

    @NotNull
    private Long bookId;

    @NotNull
    private Long userId;
}
