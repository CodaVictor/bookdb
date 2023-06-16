package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDtoV1 {

    @NotBlank(message = "Category name is required.")
    @Length(max = 255)
    private String name;

    @Length(max = 1024)
    private String description;
}
