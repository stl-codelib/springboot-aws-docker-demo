# Spring Boot 4 + Maven + Docker + GitHub + CI/CD + AWS

Dieses Beispiel zeigt dir die komplette Strecke:

1. Projekt in Eclipse erstellen oder importieren
2. Spring Boot 4 lokal starten
3. Mit Docker containerisieren
4. Mit Git versionieren
5. Nach GitHub pushen
6. Mit GitHub Actions Build, Test und Deployment ausführen
7. Nach AWS Elastic Beanstalk deployen

---

## 1. Voraussetzungen

Installiere lokal:

- Eclipse IDE for Enterprise Java and Web Developers
- Java 21
- Maven 3.9+
- Docker Desktop
- Git
- GitHub Account
- AWS Account

---

## 2. Projekt in Eclipse importieren

### Variante A: ZIP entpacken und importieren

1. ZIP-Datei entpacken
2. Eclipse öffnen
3. `File -> Import -> Existing Maven Projects`
4. Projektordner auswählen
5. `Finish`

### Variante B: Neu anlegen und Dateien übernehmen

1. `File -> New -> Spring Starter Project`
2. Name: `springboot-aws-docker-demo`
3. Type: Maven
4. Java: 21
5. Danach die Dateien aus diesem Beispiel übernehmen

---

## 3. Projektstruktur

```text
springboot-aws-docker-demo/
├── .github/workflows/ci-cd.yml
├── .ebextensions/healthcheck.config
├── src/main/java/com/example/demo/DemoApplication.java
├── src/main/java/com/example/demo/controller/HelloController.java
├── src/main/resources/application.properties
├── src/test/java/com/example/demo/DemoApplicationTests.java
├── .gitignore
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 4. Lokal mit Maven starten

Im Projektordner:

```bash
mvn clean package
mvn spring-boot:run
```

Danach im Browser testen:

- `http://localhost:8080/`
- `http://localhost:8080/api/hello`
- `http://localhost:8080/health`

---

## 5. In Eclipse starten

1. Rechtsklick auf `DemoApplication.java`
2. `Run As -> Java Application`

Oder, falls Spring Tools installiert sind:

1. Rechtsklick auf Projekt
2. `Run As -> Spring Boot App`

---

## 6. Docker lokal testen

Image bauen:

```bash
docker build -t springboot-aws-docker-demo:1.0 .
```

Container starten:

```bash
docker run -p 8080:8080 springboot-aws-docker-demo:1.0
```

Wieder testen:

- `http://localhost:8080/`
- `http://localhost:8080/api/hello`
- `http://localhost:8080/health`

---

## 7. Git lokal initialisieren

```bash
git init
git branch -M main
git add .
git commit -m "Initial Spring Boot 4 Docker project"
```

---

## 8. GitHub Repository anlegen

1. Auf GitHub neues Repository erstellen
2. Name: `springboot-aws-docker-demo`
3. Kein zusätzliches README anlegen, wenn du dieses Projekt direkt pushen willst

Remote setzen und pushen:

```bash
git remote add origin https://github.com/DEIN-USERNAME/springboot-aws-docker-demo.git
git push -u origin main
```

---

## 9. Was macht die GitHub Action?

Die Workflow-Datei `.github/workflows/ci-cd.yml` macht Folgendes:

1. Code auschecken
2. Java 21 konfigurieren
3. Maven Build und Tests ausführen
4. Docker-Image lokal im Runner bauen
5. Deployment-ZIP für Elastic Beanstalk erzeugen
6. Über OIDC bei AWS anmelden
7. Deployment nach Elastic Beanstalk ausführen

---

## 10. AWS Elastic Beanstalk vorbereiten

### 10.1 Application und Environment anlegen

In AWS:

1. Elastic Beanstalk öffnen
2. `Create application`
3. Application name: `springboot-aws-docker-demo`
4. Environment name: `springboot-aws-docker-demo-env`
5. Platform: Docker
6. Region: z. B. `eu-central-1`

---

## 11. GitHub Actions per OIDC mit AWS verbinden

### 11.1 OIDC Provider anlegen

In AWS IAM:

1. `Identity providers`
2. `Add provider`
3. Provider type: `OpenID Connect`
4. Provider URL: `https://token.actions.githubusercontent.com`
5. Audience: `sts.amazonaws.com`

### 11.2 Rolle erstellen

Erstelle eine IAM-Rolle für GitHub Actions.

Beispiel für die Trust Policy:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Federated": "arn:aws:iam::123456789012:oidc-provider/token.actions.githubusercontent.com"
      },
      "Action": "sts:AssumeRoleWithWebIdentity",
      "Condition": {
        "StringEquals": {
          "token.actions.githubusercontent.com:aud": "sts.amazonaws.com"
        },
        "StringLike": {
          "token.actions.githubusercontent.com:sub": "repo:DEIN-USERNAME/springboot-aws-docker-demo:ref:refs/heads/main"
        }
      }
    }
  ]
}
```

### 11.3 Wichtige Anpassung im Workflow

Diese Zeile musst du im Workflow anpassen:

```yaml
role-to-assume: arn:aws:iam::123456789012:role/GitHubActionsElasticBeanstalkRole
```

Ersetze:

- `123456789012` durch deine AWS Account ID
- `GitHubActionsElasticBeanstalkRole` durch deinen Rollennamen

---

## 12. Deployment starten

Wenn AWS und GitHub vorbereitet sind:

```bash
git add .
git commit -m "Add CI/CD and AWS deployment"
git push origin main
```

Dann in GitHub unter `Actions` den Workflow verfolgen.

---

## 13. Anwendung in AWS testen

Nach erfolgreichem Deployment:

- Elastic Beanstalk Environment öffnen
- Die Environment-URL aufrufen

Dann prüfen:

- `/`
- `/api/hello`
- `/health`

---

## 14. Typische Fehler

### Fehler: Maven Build schlägt fehl

Prüfen:

```bash
mvn clean verify
```

### Fehler: Docker Build schlägt fehl

Prüfen:

```bash
docker build -t test-image .
```

### Fehler: AWS Deployment schlägt fehl

Prüfen:

- Sind `application_name` und `environment_name` korrekt?
- Ist die Region korrekt?
- Ist die IAM-Rolle richtig konfiguriert?
- Ist die ARN im Workflow angepasst?

### Fehler: App ist deployed, aber nicht erreichbar

Prüfen:

- Container-Port ist 8080
- Healthcheck zeigt auf `/health`
- Die App startet im Container ohne Fehler

---

## 15. Nächste sinnvolle Ausbaustufen

Wenn das Grundbeispiel läuft, kannst du erweitern um:

- Spring Boot Actuator mit mehr Endpunkten
- PostgreSQL / AWS RDS
- Profiles (`dev`, `prod`)
- Docker Compose für lokale Services
- Deployment nach ECS statt Elastic Beanstalk

---

## 16. Hinweise zu Platzhaltern

Passe diese Werte an deine Umgebung an:

- `DEIN-USERNAME`
- AWS Account ID
- IAM-Rollenname
- AWS Region
- Elastic-Beanstalk Application Name
- Elastic-Beanstalk Environment Name

---

## 17. Schnelltest

Wenn du nur den lokalen Teil prüfen willst:

```bash
mvn clean package
docker build -t springboot-aws-docker-demo .
docker run -p 8080:8080 springboot-aws-docker-demo
```

Danach Browser öffnen:

- `http://localhost:8080/api/hello`

Wenn das funktioniert, ist dein lokaler Teil korrekt.
