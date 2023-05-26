package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDtoV1 {

    @Length(max = 2048)
    private String text;

    @NotNull
    private Float rating;
}
