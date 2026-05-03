# Návod: Príprava Torque.properties pre Jenkins

## Krok 1: Vytvor svoj Torque.properties

Skopíruj `conf/Torque.properties.example` a uprav ho podľa svojej databázy:

```bash
cp conf/Torque.properties.example /path/to/your/Torque.properties
```

## Krok 2: Ulož ho na Jenkins server

### Pre Docker Desktop na Windows:

```powershell
# Vytvor adresár v Jenkins kontajneri
docker exec jenkins mkdir -p /var/jenkins_home/configs

# Skopíruj súbor do kontajnera
docker cp D:\path\to\your\Torque.properties jenkins:/var/jenkins_home/configs/Torque.properties

# Over že súbor je tam
docker exec jenkins cat /var/jenkins_home/configs/Torque.properties
```

### Pre Linux Jenkins server:

```bash
# Vytvor adresár
mkdir -p /var/jenkins_home/configs

# Skopíruj súbor
cp /path/to/your/Torque.properties /var/jenkins_home/configs/Torque.properties

# Nastav správne práva
chmod 644 /var/jenkins_home/configs/Torque.properties
```

## Krok 3: Over cestu v Jenkinsfile

Skontroluj že cesta v `Jenkinsfile` sedí:

```groovy
environment {
    TORQUE_CONFIG_PATH = '/var/jenkins_home/configs/Torque.properties'
}
```

## Prepínanie medzi databázami:

Keď potrebuješ zmeniť databázu:

1. Uprav `/var/jenkins_home/configs/Torque.properties` priamo na serveri
2. Alebo skopíruj nový súbor:
   ```powershell
   docker cp D:\configs\Torque-test.properties jenkins:/var/jenkins_home/configs/Torque.properties
   ```
3. Spusti pipeline znova - použije aktuálny súbor

## Príklad Torque.properties s viacerými DB:

```properties
# === AKTÍVNA DATABÁZA ===
# ZSRDEV - CUD
torque.dsfactory.APP.connection.url = jdbc:log4jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = dev-oracle19.intra.ditec.sk)(PORT = 1521)) (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = zsrdev.intra.ditec.sk)))
torque.dsfactory.APP.connection.user = cud
torque.dsfactory.APP.connection.password = cud

# === ZAKOMENTOVANÉ (NEAKTÍVNE) ===
# SSD - CUD
#torque.dsfactory.APP.connection.url = jdbc:log4jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=kistest.intra.ditec.sk)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=ssd.intra.ditec.sk)))
#torque.dsfactory.APP.connection.user = cud
#torque.dsfactory.APP.connection.password = cud

# CUDT - CUD
#torque.dsfactory.APP.connection.url = jdbc:log4jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=rhlkistdbcud)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SID=CUDT)))
#torque.dsfactory.APP.connection.user = cud
#torque.dsfactory.APP.connection.password = CUD2022
```

Na prepnutie: odkomentuj požadovanú DB a zakomentuj aktuálnu.
