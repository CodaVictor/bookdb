# # Aplikace BookDB

Tento projekt je Spring Boot aplikace s názvem "BookDB", která slouží k správě knih. Níže najdete informace o tom, jak spustit projekt a jak vytvořit databázi pomocí Docker Compose.


# Spuštění projektu

1.  Nejprve si ujistěte, že máte nainstalovaný Docker na svém systému.
2.  Naklonujte si tento repozitář na svůj lokální systém.
3.  Otevřete terminál a přejděte do složky s projektem.
4.  V kořenové složce projektu spusťte příkaz  `docker-compose up -d`, který spustí kontejnery s aplikací a PostgreSQL databází.
5.  Po úspěšném spuštění můžete přistupovat k API na adrese  [http://localhost:9000/api/v1](http://localhost:9000/api/v1).

## Vytvoření databáze pomocí Docker Compose
Pro vytvoření databáze pro projekt "BookDB" pomocí Docker Compose postupujte podle následujících kroků:

1.  Ujistěte se, že máte nainstalovaný Docker na svém systému.
2.  Vytvořte v kořenu projektu soubor s názvem `docker-compose.yaml` a otevřete ho v textovém editoru.
3.  Do souboru `docker-compose.yaml` přidejte následující obsah:
>
    version: "3.9"  
    services:  
      db:  
        image: postgres  
        restart: always  
        environment:  
          POSTGRES_DB: <název DB>
          POSTGRES_USER: <název uživatele>  
          POSTGRES_PASSWORD: <heslo uživatele>  
        ports:  
          - 5433:5432

- Tento soubor definuje službu s názvem `db`, která používá databázi `postgres` a nastavuje potřebné prostředí pro vytvoření databáze. Databáze bude dostupná na portu 5432 nebo 5433.

4.  Uložte soubor `docker-compose.yaml`.
5.  Otevřete příkazový řádek nebo terminál ve složce, kde se nachází soubor `docker-compose.yaml`.
6. Spusťte následující příkaz pro spuštění Docker Compose:
>
	docker-compose up -d
	
- Tímto se vytvoří kontejner s PostgreSQL databází na základě specifikací v souboru `docker-compose.yaml`.
7. Po úspěšném spuštění můžete přistoupit k databázi pomocí libovolného nástroje pro správu databází.

## Spuštění projektu s Gradle

Pokud chcete spustit projekt "BookDB" s pomocí Gradle, postupujte podle následujících kroků:

1.  Otevřete projekt ve svém oblíbeném integrovaném vývojovém prostředí (IDE).
2.  Ve složce projektu spusťte příkazový řádek nebo terminál.
3.  Spusťte následující příkaz pro spuštění aplikace:

>
	gradle bootRun
- Tímto jste úspěšně spustili projekt "BookDB" a můžete začít používat jeho funkce pro správu knih.

# Koncové body (endpoints)
Následující popis vám poskytne přehled o jednotlivých endpointech, které jsou v aplikaci dostupné. Aplikace umožňuje prostřednictvím HTTP požadavků přidávat, číst, upravovat a mazat data.

## Knihy
|URL             					  |HTTP Metoda |Popis						  		|
|-------------------------------------|------------|------------------------------------|
|`/api/v1/books?page=x&pageSize=y`	  |GET         |Stránkovaně vrátí knihy.       		|
|`/api/v1/books/{bookID}`			  |GET         |Vrátí knihu s daným ID.       		|
|`/api/v1/books`		 			  |POST        |Uloží novou knihu.            		|
|`/api/v1/books/{bookId}`			  |PUT		   |Upraví stávající knihu s daným ID.	|
|`/api/v1/books/{bookId}`			  |DELETE	   |Smaže knihu s daným ID.				|


## Recenze
|URL             					  |HTTP Metoda |Popis						  			|
|-------------------------------------|------------|----------------------------------------|
|`/api/v1/books/{bookId}/reviews`	  |GET         |Vrátí všechny recenze knihy s daným ID. |
|`/api/v1/reviews/{bookId}`	     	  |POST        |Uloží novou recenzi ke knize s ID.      |
|`/api/v1/reviews/{bookId}/{reviewId}`|PUT		   |Upraví stávající recenzi s ID a daným ID knihy.		|
|`/api/v1/reviews/{bookId}/{reviewId}`|DELETE	   |Smaže recenzi s ID a knihu a daným ID knihy.					|


## Autoři
|URL             			 |HTTP Metoda |Popis						  		   |
|----------------------------|------------|----------------------------------------|
|`/api/v1/authors`	  	     |GET         |Vrátí všechny autory knih. 			   |
|`/api/v1/authors/{authorId}`|GET         |Vrátí autora s daným ID. 			   |
|`/api/v1/authors`			 |POST        |Vytvoří nového autora.                  |
|`/api/v1/authors/{authorId}`|PUT		  |Upraví stávajícího autora s daným ID.   |
|`/api/v1/authors/{authorId}`|DELETE	  |Smaže autora s daným ID.				   |


## Kategorie
|URL             			 	  |HTTP Metoda |Popis						  		    |
|---------------------------------|------------|----------------------------------------|
|`/api/v1/categories`	  	      |GET         |Vrátí všechny kategorie knih. 		    |
|`/api/v1/categories/{categoryId}`|GET         |Vrátí kategorii s daným ID. 			|
|`/api/v1/categories`             |POST        |Vytvoří novou kategorii.                |
|`/api/v1/categories/{categoryId}`|PUT		   |Upraví stávající kategorii s daným ID.|
|`/api/v1/categories/{categoryId}`|DELETE	   |Smaže kategorii s daným ID.				|


## Žánry
|URL             			|HTTP Metoda |Popis						  		      |
|---------------------------|------------|----------------------------------------|
|`/api/v1/genres`	  	    |GET         |Vrátí všechny kategorie žánry knih.     |
|`/api/v1/genres/{genresId}`|GET         |Vrátí žánr daným ID. 					  |
|`/api/v1/genres`           |POST        |Vytvoří nový žánr.                      |
|`/api/v1/genres/{genresId}`|PUT		 |Upraví stávající žánr s daným ID.       |
|`/api/v1/genres/{genresId}`|DELETE	     |Smaže žánr s daným ID.				  |


## Vydavatelé
|URL             			|HTTP Metoda        |Popis						  		      |
|---------------------------|-------------------|-----------------------------------------|
|`/api/v1/publishers`	  	       |GET         |Vrátí všechny vydavatele knih.           |
|`/api/v1/publishers/{publisherId}`|GET         |Vrátí vydavatele daným ID. 	          |
|`/api/v1/publishers`              |POST        |Vytvoří nového vydavatele.               |
|`/api/v1/publishers/{publisherId}`|PUT		    |Upraví stávajícího vydavatele s daným ID.|
|`/api/v1/publishers/{publisherId}`|DELETE	    |Smaže vydavatele s daným ID.		   	  |
