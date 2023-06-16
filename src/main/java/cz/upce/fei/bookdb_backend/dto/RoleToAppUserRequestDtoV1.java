package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleToAppUserRequestDtoV1 {

    @NotBlank(message = "Username is required.")
    @Length(max = 320)
    private String userEmail;

    @NotBlank(message = "Role name is required.")
    @Length(max = 255)
    private String roleName;
}
