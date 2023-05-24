package cz.upce.fei.bookdb_backend.component;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Role;
import cz.upce.fei.bookdb_backend.repository.BookRepository;
import cz.upce.fei.bookdb_backend.service.AppUserService;
import cz.upce.fei.bookdb_backend.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Component
@Slf4j
public class DatabaseRunner implements ApplicationRunner {

    private final BookService bookService;
    private final AppUserService appUserService;

    public DatabaseRunner(BookService bookService, AppUserService appUserService) {
        this.bookService = bookService;
        this.appUserService = appUserService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        /*
        Book book1 = new Book();
        book1.setTitle("Velká kolotoč");
        bookService.create(book1);

        Book book2 = new Book();
        book2.setTitle("Labyrint");
        bookService.create(book2);

        bookService.findAll(null, Pageable.unpaged()).forEach(book -> log.info(book.toString()));


        appUserService.saveRole(new Role(null, "ROLE_USER"));
        appUserService.saveRole(new Role(null, "ROLE_EDITOR"));
        appUserService.saveRole(new Role(null, "ROLE_ADMIN"));
        */

        /*
        appUserService.saveUser(new AppUser(null, "Viktor", "Homolka",
                "st55772@upce.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));
        appUserService.saveUser(new AppUser(null, "Julius", "Nanoland",
                "jn@email.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));
        appUserService.saveUser(new AppUser(null, "Karel", "Slezina",
                "ks@email.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));

        appUserService.addRoleToUser("st55772@upce.cz", "ROLE_ADMIN");
        appUserService.addRoleToUser("jn@email.cz", "ROLE_EDITOR");
        appUserService.addRoleToUser("ks@email.cz", "ROLE_USER");
        */

        /*
        appUserService.saveUser(new AppUser(null, "Test", "Testamon",
                "tt@email.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));
        appUserService.addRoleToUser("tt@email.cz", "ROLE_ADMIN");
        */
    }
}