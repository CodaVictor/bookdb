package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreRequestDtoV1 {

    @NotNull
    @Length(max = 255)
    private String genreName;

    @Length(max = 1024)
    private String description;
}
