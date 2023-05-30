package cz.upce.fei.bookdb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookFilterParameters {


    private String orderBy;
    @Pattern(regexp = "ASC|DESC", message = "The value must be 'ASC' or 'DESC'.")
    private String orderDirection;
    private List<Long> category;
    private List<Long> publisher;
    private List<Long> genre;
}
