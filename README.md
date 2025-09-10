
# Fulköpings Bibliotek

Detta projekt är ett Java-baserat system för Fulköpings bibliotek som hanterar lånehantering, bokreservationer, användarprofil, och mycket mer. Systemet använder en MySQL-databas för att lagra data och är skapat för att vara enkelt att använda.

##  Systemfunktioner

### Programmet gör det möjligt för användare att:
1. Söka efter böcker 
- Generellt eller genom specifika fält som titel och författare.
2. Låna och lämna tillbaka böcker 
- Böcker kan lånas i 30 dagar.
- En bok kan bara vara utlånad till en användare åt gången.
3. Se sin lånehistorik 
- Användare kan se vilka böcker de tidigare har lånat, inklusive datum för lånets början och avslut.
4. Se sin nuvarande lånestatus
- Användare kan se nuvarande böcker de har lånat. Datum för start och returneringsdatum
5. Profilhantering
- Registrera sig, logga in och hantera sin profil.
6. Reservera lånade böcker
- Reservationer är giltiga i 30 dagar.


### Säkerhet
- Lösenord lagras med BCrypt hashing.
- Alla SQL-anrop använder Prepared Statements för att skydda mot SQL-injektion.


## Installation & Användning

#### Krav:

- Java 17 eller senare
- MySQL-server installerad och konfigurerad.
- En IDE (t.ex. Intellij IDEA) eller Maven.

## Hur man kör programmet

### Installation

1. Klona detta repot: https://github.com/MH-GRIT/fulk-pings-bibliotek-LukasNorrman.git
2. Navigera till serverprojektets rotmapp.
3. Importera databasen.
- Öppna GIT Bash.
- Kör SQL-filen "Bibliotek.sql"
4. Anpassa databasanslutningen om det behövs.
5. Bygg och kör programmet.

### Användning 

1. Starta applikationen .
2. Registrera dig.
3. Utforska funktioner som sökning, lån och reservation.
4. Roa Dig!

