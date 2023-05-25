package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequestDtoV1 {
    @NotBlank
    @Length(max = 255)
    private String firstName;

    @Length(max = 255)
    private String lastName;

    private LocalDateTime birthdate;

    @Length(max = 1024)
    private String description;
}
