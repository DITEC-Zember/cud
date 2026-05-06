# Integračné testy pre CudWS

## Popis

Tento adresár obsahuje integračné testy pre CudWS webovú službu.

## Predpoklady

1. **Aplikácia musí bežať** - Webová služba musí byť dostupná na `http://localhost:8081/cud/CudWS`
2. **Databáza musí byť dostupná** - Aplikácia musí mať prístup k databáze
3. **Maven** - Pre spustenie testov je potrebný Maven

## Príprava

### 1. Nastavenie prihlasovacích údajov

Upravte triedu `CudWSIntegrationTest.java` a nastavte platné prihlasovacie údaje v metóde `createTestAuthInfo()`:

```java
private AuthInfoWS createTestAuthInfo() {
    AuthInfoWS authWS = new AuthInfoWS();
    authWS.accountName = "vase_uzivatelske_meno";  // <-- UPRAVTE
    return authWS;
}
```

**Poznámka:** Triedy `AuthInfoWS` a `Page` používajú public fieldy namiesto setter metód.

### 2. Spustenie aplikácie

Pred spustením testov je potrebné spustiť aplikáciu:

```bash
# Vytvorenie WAR súboru
mvn clean package

# Nasadenie do Tomcat alebo iného aplikačného servera
# Uistite sa, že aplikácia beží na porte 8081
```

## Spustenie testov

### Spustenie všetkých testov

```bash
mvn test
```

### Spustenie konkrétneho testu

```bash
mvn test -Dtest=CudWSIntegrationTest
```

### Spustenie konkrétnej testovacej metódy

```bash
mvn test -Dtest=CudWSIntegrationTest#testCiselnikList_BasicCall
```

## CI/CD Integrácia

Testy sa automaticky spúšťajú v Jenkins pipeline:

- **Build & Test stage**: Maven `package` s vylúčením integračných testov (len unit testy)
- **Unit Tests stage**: Explicitne spustí `mvn test` s vylúčením integračných testov
- **Integration Tests stage**: 
  - Nasadí aplikáciu do Tomcat
  - Spustí smoke testy (CudWSAvailabilityTest) ktoré overujú dostupnosť WSDL
  - **Spustí plné integračné testy (CudWSIntegrationTest)** s konfigurovateľnými credentials

### Konfigurácia credentials pre Jenkins

V `Jenkinsfile` nastavte:
```groovy
environment {
    TEST_ACCOUNT_ID = '136'  // Platný account ID
}
```

Testy sa spustia s:
```bash
mvn test -Dtest=CudWSIntegrationTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl -DaccountId=136
```

Pre zobrazenie výsledkov testov v Jenkins UI, prejdite na:
- Build detail → Test Results

### Spustenie testov s vlastnou URL a credentials

Testy podporujú konfiguráciu cez system properties:

```bash
# Lokálne testovanie
mvn test -Dtest=CudWSIntegrationTest \
  -Dwsdl.url=http://localhost:8081/cud/CudWS?wsdl \
  -DaccountId=136

# Smoke test
mvn test -Dtest=CudWSAvailabilityTest -Dwsdl.url=http://localhost:8081/cud/CudWS?wsdl

# V Jenkins (port 8080 v Docker kontajneri)
mvn test -Dtest=*IntegrationTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl -DaccountId=136
```

## Testové scenáre

### 1. testCiselnikList_BasicCall
Základný test, ktorý volá metódu `ciselnikList` bez špecifických filtrov.

**Poznámka:** Tento test vyžaduje bežiacu aplikáciu a je vylúčený z bežného `mvn test`.

### 2. testCiselnikList_WithPagination
Test paginácie - overuje, že služba správne spracováva stránkovanie (maximálne 10 záznamov).

**Poznámka:** Tento test vyžaduje bežiacu aplikáciu a je vylúčený z bežného `mvn test`.

### 3. testCiselnikList_WithFilter
Test filtrovania - overuje, že služba správne aplikuje filtre.

**Poznámka:** Tento test vyžaduje bežiacu aplikáciu a je vylúčený z bežného `mvn test`.

## Vylúčenie integračných testov

Integračné testy (CudWSIntegrationTest) vyžadujú bežiacu aplikáciu, takže sú **automaticky vylúčené** pomocou Maven Surefire Plugin konfigurácie v pom.xml.

Pri týchto príkazoch sa **NESPÚŠŤAJÚ** integračné testy:

```bash
mvn test          # Len unit testy
mvn package       # Build + len unit testy
mvn clean install # Build + len unit testy
```

Pre explicitné vylúčenie (nie je nutné, ale možné):
```bash
mvn test -Dtest=!*IntegrationTest
```

### Spustenie integračných testov

**Predpoklad:** Aplikácia musí bežať na localhost:8081

```bash
# Spustiť LEN integračné testy
mvn test -Dtest=*IntegrationTest

# Spustiť konkrétny integračný test
mvn test -Dtest=CudWSIntegrationTest

# Spustiť všetky testy vrátane integračných (POZOR: integračné zlyhajú ak aplikácia nebeží)
mvn test -Dtest=*
```

### Poznámka pre Maven Surefire Plugin

V `pom.xml` je nakonfigurované:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>**/*IntegrationTest.java</exclude>
        </excludes>
    </configuration>
</plugin>
```

Toto zabezpečí, že integračné testy sa automaticky vylúčia z bežného `mvn test`.

## Riešenie problémov

### "Connection refused"
- Overte, že aplikácia skutočne beží na `http://localhost:8081/cud/CudWS`
- Skontrolujte, či je Tomcat (alebo iný aplikačný server) spustený
- Overte, že port 8081 nie je blokovaný

### "Authentication failed"
- Skontrolujte prihlasovacie údaje v metóde `createTestAuthInfo()`
- Overte, že používateľ existuje v databáze a má potrebné oprávnenia

### "WSDL not found"
- Overte WSDL URL: `http://localhost:8081/cud/CudWS?wsdl`
- Otvorte URL vo webovom prehliadači a skontrolujte, či sa WSDL zobrazuje

## Konfigurácia

Ak potrebujete zmeniť URL webovej služby, upravte konštantu v `CudWSIntegrationTest.java`:

```java
private static final String WSDL_URL = "http://localhost:8081/cud/CudWS?wsdl";
```

## Poznámky

- Testy sú **integračné**, čo znamená, že testujú skutočnú webovú službu bežiacu na serveri
- Testy **NIE SÚ** unit testy - vyžadujú bežiacu aplikáciu a databázu
- Pre rýchle testovanie počas vývoja zvážte vytvorenie mock testov
