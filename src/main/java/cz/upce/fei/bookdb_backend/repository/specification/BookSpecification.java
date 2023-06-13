package cz.upce.fei.bookdb_backend.repository.specification;

import cz.upce.fei.bookdb_backend.domain.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.util.List;

public class BookSpecification {
    public static Specification<Book> hasCategoryId(List<Long> categories) {
        return (root, query, builder) -> root.get("category").get("id").in(categories);
    }

    public static Specification<Book> hasPublisherId(List<Long> publishers) {
        return (root, query, builder) -> root.get("publisher").get("id").in(publishers);
    }

    public static Specification<Book> hasGenreId(List<Long> genres) {
        return (root, query, builder) -> root.get("genre").get("id").in(genres);
    }

    public static Specification<Book> takeReviewRating() {
        return (root, query, builder) -> {
            root.fetch("reviews", JoinType.LEFT); // Načtení asociace "reviews"
            return builder.isNotNull(root.get("reviews").get("rating"));
        };
    }
}
