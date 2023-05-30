package cz.upce.fei.bookdb_backend.component;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Role;
import cz.upce.fei.bookdb_backend.dto.*;
import cz.upce.fei.bookdb_backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseRunner implements ApplicationRunner {

    private final BookService bookService;
    private final AppUserService appUserService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        /*
        appUserService.saveRole(new Role(null, "ROLE_USER"));
        appUserService.saveRole(new Role(null, "ROLE_EDITOR"));
        appUserService.saveRole(new Role(null, "ROLE_ADMIN"));

        appUserService.saveUser(new AppUser(null, "Viktor", "Homolka",
                "st55772@upce.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));
        appUserService.saveUser(new AppUser(null, "Julius", "Nanoland",
                "user1@email.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));
        appUserService.saveUser(new AppUser(null, "Karel", "Slezina",
                "user2@email.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));
        appUserService.saveUser(new AppUser(null, "Jindřich", "Votruba",
                "user3@email.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));
        appUserService.saveUser(new AppUser(null, "Antonín", "Kopecký",
                "user4@email.cz", "heslo", Collections.emptyList(), new ArrayList<Role>()));

        appUserService.addRoleToUser("st55772@upce.cz", "ROLE_ADMIN");
        appUserService.addRoleToUser("st55772@upce.cz", "ROLE_EDITOR");
        appUserService.addRoleToUser("user1@email.cz", "ROLE_USER");
        appUserService.addRoleToUser("user2@email.cz", "ROLE_USER");
        appUserService.addRoleToUser("user3@email.cz", "ROLE_EDITOR");
        appUserService.addRoleToUser("user4@email.cz", "ROLE_EDITOR");

        genreService.create(new GenreRequestDtoV1("Beletrie", null));
        genreService.create(new GenreRequestDtoV1("Poezie", null));
        genreService.create(new GenreRequestDtoV1("Historická literatura", null));
        genreService.create(new GenreRequestDtoV1("Cizojazyčná literatura", null));
        genreService.create(new GenreRequestDtoV1("Odborná literatura", null));
        genreService.create(new GenreRequestDtoV1("Naučná literatura", null));
        genreService.create(new GenreRequestDtoV1("Cestování", null));
        genreService.create(new GenreRequestDtoV1("Kuchařky", null));
        genreService.create(new GenreRequestDtoV1("Učebnice", null));

        publisherService.create(new PublisherRequestDtoV1("Grada"));
        publisherService.create(new PublisherRequestDtoV1("Argo"));
        publisherService.create(new PublisherRequestDtoV1("Naše vojsko"));
        publisherService.create(new PublisherRequestDtoV1("Odeon"));
        publisherService.create(new PublisherRequestDtoV1("Radix"));
        publisherService.create(new PublisherRequestDtoV1("OneHotBook"));
        publisherService.create(new PublisherRequestDtoV1("Tympanum"));
        publisherService.create(new PublisherRequestDtoV1("Radioservis"));
        publisherService.create(new PublisherRequestDtoV1("Plus"));
        publisherService.create(new PublisherRequestDtoV1("F341"));
        publisherService.create(new PublisherRequestDtoV1("Ikar"));
        publisherService.create(new PublisherRequestDtoV1("BB art"));
        publisherService.create(new PublisherRequestDtoV1("Artur"));

        categoryService.create(new CategoryRequestDtoV1("Papírové knihy", null));
        categoryService.create(new CategoryRequestDtoV1("E-knihy", null));
        categoryService.create(new CategoryRequestDtoV1("Audioknihy", null));

        authorService.create(new AuthorRequestDtoV1("Edgar Alllan", "Poe", LocalDate.of(1809,1, 19).atStartOfDay(),
                "Edgar Allan Poe byl americký romantický básník, prozaik, literární teoretik a esejista. Byl autorem zpravidla fantastických a mystických příběhů a zakladatelem detektivního a hororového žánru. Dokázal mistrovsky zachytit stav osoby, která příběh vypráví."));
        authorService.create(new AuthorRequestDtoV1("Ernest", "Hemingway", LocalDate.of(1899,7, 26).atStartOfDay(),
                "Ernest Miller Hemingway byl americký spisovatel. Je považován za čelného představitele tzv. ztracené generace. V roce 1953 byl oceněn Pulitzerovou cenou, o rok později pak získal Nobelovu cenu za literaturu. Hemingway byl mistrem krátkých forem v próze a jeho úsporný styl se stal pojmem v moderní světové literatuře."));
        authorService.create(new AuthorRequestDtoV1("Erich Maria", "Remarque", LocalDate.of(1898,6, 22).atStartOfDay(),
                "Erich Maria Remarque byl německý pacifistický spisovatel, jenž napsal mnoho děl o hrůzách války. Jeho nejznámějším románem je Na západní frontě klid, kniha o německých vojácích v první světové válce, jež později posloužila za předlohu oscarového filmu."));
        authorService.create(new AuthorRequestDtoV1("Honoré", "de Balzac", LocalDate.of(1799,5, 20).atStartOfDay(),
                "Honoré de Balzac [onoré d balzak], byl francouzský spisovatel, představitel realismu a romantismu, novinář a kritik. Bývá považován za zakladatele kriticko-realistického románu. Balzacovým magnum opus je cyklus devadesáti sedmi románů a povídek pod souborným názvem Lidská komedie."));
        authorService.create(new AuthorRequestDtoV1("Joseph", "Heller", LocalDate.of(1923,5, 1).atStartOfDay(),
                "Joseph Heller byl americký spisovatel. Psal satirická díla, zejména novely a dramata. J. Heller pocházel z Brooklynu v New Yorku, jeho rodiče byli židovští přistěhovalci z Ruska. Ve druhé světové válce sloužil u amerického bombardovacího letectva na Korsice."));
        authorService.create(new AuthorRequestDtoV1("Nikolaj Vasiljevič", "Gogol", LocalDate.of(1809,4, 1).atStartOfDay(),
                "Nikolaj Vasiljevič Gogol byl ruský prozaik a dramatik ukrajinského původu. Byl představitelem ruského romantismu s velmi silnými prvky kritického realismu, za jehož zakladatele je v ruské literatuře považován."));
        */

        /*
        bookService.create(new BookRequestDtoV1("Havran", null, "978-80-87573-17-4", "CZ", LocalDate.of(2015,9, 1).atStartOfDay(), 48, "Nový český překlad Petra Krula, vydaný spolu s anglickým originálem, doprovází 24 ilustrací známého výtvarníka Jiřího Slívy, které dodávají této 170 let staré básni spíše humorný než historicky pochmurný ráz. Kniha je doplněna úvodem překladatele a doslovem výtvarníka. Vyšla v bibliofilské úpravě v omezeném nákladu 280 výtisků, z toho 30 číslovaných s originální grafickou přílohou Jiřího Slívy.", null, 1L, 2L, 5L, List.of(1L)));
        bookService.create(new BookRequestDtoV1("Stařec a moře", null, "8594046710105", "CZ", LocalDate.of(2012,1, 1).atStartOfDay(), 176, "Několikrát zfilmované dílo z roku 1952, jež Hemingwayovi dopomohlo k Nobelově i Pulitzerově ceně, je snad nejslavnější variací hemingwayovského příběhu o hrdnovi troskotajícím, ale neporaženém. Mistrovské dílo opět doplní v češtině dosud nepublikované texty Hemingwaye - žurnalisty, tentokrát o rybolovu. Čtenář tak bude znovu moci takřka nahlédnout spisovateli přes rameno a sledovat jej na jeho tvůrčí cestě od faktografie k beletrii.", null, 1L, 1L, 9L, List.of(2L)));
        bookService.create(new BookRequestDtoV1("Na západní frontě klid", null, "978-80-249-4223-0", "CZ", LocalDate.of(2020,9, 29).atStartOfDay(), 184, "Nejslavnější autorův román z prostředí první světové války vypráví o generaci chlapců, kteří v roce 1914 rovnou ze školních lavic, s vlajícími prapory, nadšením a vojenskou hudbou táhli na jatka války, odkud se vrátili zničení na duchu i na těle, přestože unikli smrtícím granátům. Román je krutou obžalobou a pravdivým obrazem tragédie války, která nezabíjí jednotlivce, ale mrzačí celé generace, nejstrašnější není výbuch granátu, ale zasažené lidské srdce…", null, 1L, 1L, 10L, List.of(3L)));
        bookService.create(new BookRequestDtoV1("Otec Goriot", null, "978-80-277-2138-2", "CZ", LocalDate.of(2023,5, 24).atStartOfDay(), 280, "Otec Goriot pracuje jako dělník v továrně na výrobu těstovin a později se z něho stane obchodník. Daří se mu a finančně je velmi dobře zajištěný. Má dvě dcery, Anastázii a Delfínu, které jsou již vdané. Jedna si vzala bankéře, druhá šlechtice a od otce dostaly velmi tučná věna.", null, 1L, 1L, 11L, List.of(4L)));
        bookService.create(new BookRequestDtoV1("Hlava XXII", null, "80-7341-618-2", "CZ", LocalDate.of(2005,5, 24).atStartOfDay(), 536, "Hellerův nejznámější a nejlepší román, autor vychází z vlastních zkušeností – román je svým způsobem absurdní, ale realistický. Hlavní postavou je poručík letectva Yossarian, který je trochu klaun a trochu blázen (svým způsobem obdoba Haškova Švejka). Dílo nemá jednotnou koncepci, jedná se prostě o vylíčení mnoha absurdních situací.", null, 1L, 1L, 12L, List.of(5L)));
        bookService.create(new BookRequestDtoV1("Revizor", null, "978-80-7483-195-9", "CZ", LocalDate.of(2023,5, 4).atStartOfDay(), 96, "Satirická komedie ruského klasika se baví hloupostí a nevzdělaností úředníků v provinčním ruském městečku, kteří naletí falešnému \"revizorovi\" Chlestakovovi, který nestydatě využívá jejich zkorumpovanost, úplatkářství a podlézavost.", null, 1L, 1L, 13L, List.of(6L)));
        */
    }
}