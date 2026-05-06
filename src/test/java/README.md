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

- **Build & Test stage**: Maven `package` automaticky spustí všetky testy
- **Unit Tests stage**: Explicitne spustí `mvn test` a publikuje výsledky
- **Integration Tests stage**: 
  - Nasadí aplikáciu do Tomcat
  - Spustí smoke testy (CudWSAvailabilityTest) ktoré overujú dostupnosť WSDL
  - Pre plné integračné testy by bolo potrebné nastaviť prihlasovacie údaje

Pre zobrazenie výsledkov testov v Jenkins UI, prejdite na:
- Build detail → Test Results

### Spustenie testov s vlastnou URL

Testy podporujú konfiguráciu URL cez system property:

```bash
# Lokálne testovanie
mvn test -Dtest=CudWSAvailabilityTest -Dwsdl.url=http://localhost:8081/cud/CudWS?wsdl

# V Jenkins (port 8080 v Docker kontajneri)
mvn test -Dtest=CudWSAvailabilityTest -Dwsdl.url=http://localhost:8080/cud/CudWS?wsdl
```

## Testové scenáre

### 1. testCiselnikList_BasicCall
Základný test, ktorý volá metódu `ciselnikList` bez špecifických filtrov.

### 2. testCiselnikList_WithPagination
Test paginácie - overuje, že služba správne spracováva stránkovanie (maximálne 10 záznamov).

### 3. testCiselnikList_WithFilter
Test filtrovania - overuje, že služba správne aplikuje filtre.

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
