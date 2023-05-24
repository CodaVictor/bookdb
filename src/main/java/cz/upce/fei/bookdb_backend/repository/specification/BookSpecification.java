package cz.upce.fei.bookdb_backend.repository.specification;

import cz.upce.fei.bookdb_backend.domain.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class BookSpecification {
    public static Specification<Book> hasPublisherId(Long languageId) {
        return (root, query, builder) -> builder.equal(root.get("publisher").get("id"), languageId);
    }

    public static Specification<Book> hasGenreId(Long genreId) {
        return (root, query, builder) -> builder.equal(root.get("genre").get("id"), genreId);
    }

    public static Specification<Book> hasCategoryId(Long categoryId) {
        return (root, query, builder) -> builder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Book> takeReviewRating() {
        return (root, query, builder) -> {
            root.fetch("reviews", JoinType.LEFT); // Načtení asociace "reviews"
            return builder.isNotNull(root.get("reviews").get("rating"));
        };
    }
}
