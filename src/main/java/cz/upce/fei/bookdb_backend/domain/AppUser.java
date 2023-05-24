package cz.upce.fei.bookdb_backend.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(length = 1024, nullable = false)
    private String password;

    // Book-reviews
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<Review> reviews = Collections.emptyList();
    // -----------------------

    // AppUser-roles
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles;
}
