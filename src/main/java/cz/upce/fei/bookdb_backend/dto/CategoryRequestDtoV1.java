package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDtoV1 {

    @Length(max = 255)
    private String name;

    @Length(max = 1024)
    private String description;
}
