package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleToAppUserRequestDtoV1 {

    @NotNull
    @Length(max = 320)
    private String userEmail;

    @NotNull
    @Length(max = 255)
    private String roleName;
}
