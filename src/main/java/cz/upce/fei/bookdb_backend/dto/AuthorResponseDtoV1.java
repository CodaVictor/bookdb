package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDtoV1 {
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDateTime birthdate;

    private String description;
}
