package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequestDtoV1 {

    @Length(max = 255)
    @NotBlank
    private String firstName;

    @Length(max = 255)
    @NotBlank
    private String lastName;

    @Length(max = 320)
    @NotBlank
    private String email;

    @Length(max = 1024)
    @NotBlank
    private String password;

    private List<Long> role;
}
