package cz.upce.fei.bookdb_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestParamDtoV1 {

    @Pattern(regexp = "asc|desc", message = "The value must be 'asc' or 'desc'")
    private String orderBy;
    private String orderParameter;
    private List<Long> category;
    private List<Long> publisher;
    private List<Long> genre;
}