package cz.upce.fei.bookdb_backend.filter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookParameters {
    private Integer page;
    private Integer pageSize;
    private String orderBy;
    private String orderParameter;
    private List<Long> category;
    private List<Long> publisher;
    private List<Long> genre;
}
