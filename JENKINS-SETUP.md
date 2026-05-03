# Jenkins Pipeline - Inštrukcie pre nastavenie

## Vytvorené súbory:
- `Dockerfile` - Vlastný Docker obraz s Tomcat 8.5 + Maven + Java 8
- `Jenkinsfile` - Pipeline s mountovaním Torque.properties z disku
- `.dockerignore` - Vylúči zbytočné súbory z Docker buildu
- `conf/Torque.properties.example` - Príklad konfigurácie

## Nastavenie Torque.properties na Jenkins serveri:

### Krok 1: Ulož Torque.properties na disk

Ulož svoj `Torque.properties` súbor na Jenkins server (alebo na PC kde beží Docker):

```bash
# Vytvor adresár pre konfigurácie
mkdir -p /var/jenkins_home/configs

# Skopíruj tam svoj Torque.properties
cp /path/to/your/Torque.properties /var/jenkins_home/configs/Torque.properties
```

**Pre Windows + Docker Desktop:**
```powershell
# Vytvor adresár v Docker Desktop volume
docker exec jenkins mkdir -p /var/jenkins_home/configs

# Skopíruj súbor do Jenkins kontajnera
docker cp D:\path\to\Torque.properties jenkins:/var/jenkins_home/configs/Torque.properties
```

### Krok 2: Uprav cestu v Jenkinsfile

V `Jenkinsfile`, sekcia `environment`, nastav správnu cestu:
```groovy
environment {
    TORQUE_CONFIG_PATH = '/var/jenkins_home/configs/Torque.properties'
}
```

## Prepínanie medzi databázami:

Keď chceš zmeniť databázu:

1. **Uprav Torque.properties** na disku (odkomentuj/zakomentuj potrebné sekcie)
2. **Spusti pipeline znova** - automaticky použije nový súbor

Príklad Torque.properties s viacerými DB:
```properties
# ZSRDEV - CUD (aktívna)
torque.dsfactory.APP.connection.url = jdbc:log4jdbc:oracle:thin:@(...)
torque.dsfactory.APP.connection.user = cud
torque.dsfactory.APP.connection.password = cud

# SSD - CUD (zakomentované)
#torque.dsfactory.APP.connection.url = jdbc:log4jdbc:oracle:thin:@(...)
#torque.dsfactory.APP.connection.user = cud
#torque.dsfactory.APP.connection.password = cud
```

## Ako to funguje:

1. **Build & Test stage:** 
   - Zbuilduje sa Docker obraz z `Dockerfile`
   - Spustí sa `mvn clean package`
   - Vytvorí sa `.war` súbor

2. **Integration Tests stage:**
   - Namountuje sa `Torque.properties` z tvojho disku do kontajnera
   - Nasadí sa `.war` do Tomcatu
   - Spustí sa Tomcat s pripojením na DB podľa `Torque.properties`
   - Spustia sa integračné testy

## Výhody tohto riešenia:

✅ **Jedna konfigurácia** - Torque.properties je len na 1 mieste  
✅ **Bezpečnosť** - heslo nie je v Git repozitári  
✅ **Flexibilita** - jednoducho prepínaš medzi DB úpravou súboru  
✅ **Rýchlosť** - nemusíš rebuildiť Docker obraz pri zmene DB

## Testovanie lokálne (bez Jenkins):

### Možnosť 1: Build + deploy v jednom kroku

```powershell
# Build Docker image
docker build -t cud .

# Spusti kontajner, zbuilduj projekt a nasaď do Tomcatu
docker run -p 8080:8080 \
  -v D:/zsr/githubclone/cud:/app \
  -v D:/zsr/githubclone/cud/JavaSource/Torque.properties:/usr/local/tomcat/conf/Torque.properties:ro \
  --name cud cud \
  bash -c "cd /app && mvn clean package && cp target/*.war /usr/local/tomcat/webapps/cud.war && catalina.sh run"
```

### Možnosť 2: Zbuilduj lokálne, potom deploy (odporúčané)

```powershell
# 1. Zbuilduj projekt lokálne (BEZ Torque.properties v kóde)
mvn clean package

# 2. Build Docker image s custom entrypoint
docker build -t cud .

# 3. Spusti kontajner s WAR a EXTERNÝM Torque.properties
docker run -d -p 8081:8080 -v D:/zsr/githubclone/cud/target/cud-1.0.0.war:/usr/local/tomcat/webapps/cud.war -v D:/zsr/githubclone/cud/JavaSource/Torque.properties:/config/Torque.properties:ro --name cud cud

# 4. Sleduj logy - uvidíš ako sa Torque.properties konfiguruje
docker logs -f cud
```

**Ako to funguje:**
1. Docker image obsahuje custom **entrypoint script**
2. Pri spustení kontajnera Tomcat rozbalí WAR súbor
3. Entrypoint script počká na rozbalenie
4. Potom skopíruje `/config/Torque.properties` do `WEB-INF/classes/`
5. Reštartuje Tomcat aby načítal konfiguráciu

**Prepnutie databázy:**
1. Uprav `JavaSource/Torque.properties` na Windows disku
2. Reštartuj kontajner: `docker restart cud`
3. Entrypoint automaticky znova skopíruje aktuálnu verziu

**Aplikácia bude dostupná na:** http://localhost:8080/cud

**Zastavenie a vymazanie kontajnera:**
```powershell
docker stop cud
docker rm cud
```
