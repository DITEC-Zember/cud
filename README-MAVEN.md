# CUD - Maven Build

## Popis

Tento projekt bol prevedený na Maven build systém s použitím existujúcich knižníc z adresára `WebContent/WEB-INF/lib`.

## Dôležité informácie

### Lokálne knižnice
- Všetky JAR závislosti zostávajú v adresári `WebContent/WEB-INF/lib/`
- Maven **nesťahuje** knižnice z Maven Central ani iných repozitárov
- Všetky závislosti sú definované ako `system` scope s odkazom na lokálne súbory

### Štruktúra projektu
- **Zdrojové kódy**: `JavaSource/` (zostáva nezmenené)
- **Web obsah**: `WebContent/` (zostáva nezmenené)  
- **Knižnice**: `WebContent/WEB-INF/lib/` (zostáva nezmenené)
- **Build výstup**: `target/` (nový adresár vytvorený Mavenom)

## Build príkazy

### Kompilácia projektu
```bash
mvn clean compile
```

### Vytvorenie WAR súboru
```bash
mvn clean package
```

WAR súbor bude vytvorený v `target/cud-1.0.0.war`

### Vyčistenie build výstupov
```bash
mvn clean
```

### Preskočenie testov (ak sa pridajú)
```bash
mvn clean package -DskipTests
```

## Požiadavky

- **Maven**: verzia 3.6 alebo vyššia
- **Java JDK**: verzia 1.8 (Java 8) alebo vyššia
- Všetky JAR súbory musia zostať v `WebContent/WEB-INF/lib/`

## Poznámky

1. Pri presune/premenovanie JAR súborov v `lib/` adresári je potrebné aktualizovať cesty v `pom.xml`
2. Projekt zachováva pôvodnú štruktúru adresárov (nie štandardnú Maven štruktúru `src/main/java`)
3. Všetky JAR závislosti budú automaticky zahrnuté vo vytvorenom WAR súbore
4. Konfiguračné súbory (xml, properties) z `JavaSource/` sa automaticky kopírujú do výstupného WAR súboru

## Riešenie problémov

### Maven nenachádza JAR súbory
Skontrolujte, že:
- Cesta v `<systemPath>` v pom.xml je správna
- JAR súbor skutočne existuje v `WebContent/WEB-INF/lib/`
- Používate správny separator pre váš OS (Maven by mal automaticky konvertovať)

### Encoding chyby pri kompilácii
Projekt používa UTF-8 encoding, ktorý je nastavený v pom.xml. Ak sa vyskytnú problémy:
```bash
mvn clean compile -Dproject.build.sourceEncoding=UTF-8
```
