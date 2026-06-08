# Plan pokrivenosti nastave – Spring Boot & React aplikacija

## Opis projekta
Aplikacija **Plan pokrivenosti nastave** namenjena je evidenciji i upravljanju planom realizacije nastave na fakultetu. Sistem omogućava pregled predmeta, nastavnika i oblika nastave (predavanja, vežbe, laboratorijske vežbe), uz jasno definisane uloge korisnika i administratore.

---


## Tehnologije
### Backend
- Java
- Spring Boot
- Spring Data JPA
- Liquibase
- REST API
- Swagger / OpenAPI

### Frontend
- React
- Axios

### Baza podataka
- Relaciona baza (konfigurisana kroz Liquibase)


---

## Funkcionalnosti aplikacije

### Autentifikacija i autorizacija
- **Registracija novog korisnika**
  - Lozinka mora da sadrži:
    - najmanje **8 karaktera**
    - **jedno veliko slovo**
    - **jedan broj**
    - **jedan specijalan karakter**
- **Prijava (login)** pomoću email adrese i lozinke
- Različite uloge korisnika:
  - **KORISNIK**
  - **ADMINISTRATOR**


---

### Uloge i prava pristupa

#### 👤 Korisnik
- Nakon prijave može:
  - da **vidi tabelu plana pokrivenosti nastave**
  - nema mogućnost izmene ili brisanja podataka

#### 🛠 Administrator
- Nakon prijave može:
  - da vidi **tabelu plana pokrivenosti nastave**
  - da **kreira novi plan pokrivenosti** za predmet
  - da definiše:
    - ko drži **predavanja**
    - ko drži **vežbe**
    - ko drži **laboratorijske vežbe**
  - da briše postojeće zapise
  - da vidi **detalje plana** i
  - da **izmeni nastavnika** za određeni predmet

---

### Ograničenja u sistemu
- Uvedena su **ograničenja broja sati nastave** po obliku nastave
- Sistem ne dozvoljava unos vrednosti koje prelaze definisana pravila

---

## Backend arhitektura
- Kreiran je **jak entitet Plan pokrivenosti nastave**
- Implementiran je **REST Controller** koji sadrži CRUD operacije:
  - Create
  - Read
  - Update
  - Delete
- Endpoint-i su dokumentovani pomoću **Swagger/OpenAPI**

---

## Liquibase
- Liquibase je konfigurisan za:
  - automatsko kreiranje svih potrebnih tabela prilikom pokretanja aplikacije
  - inicijalizaciju baze sa početnim podacima (po potrebi)

---

## Swagger / OpenAPI
- Swagger UI omogućava:
  - testiranje REST API endpoint-a
  - pregled dokumentacije za sve CRUD operacije nad jakim entitetom

---

## Pokretanje projekta

### Backend
1. Pokrenuti Spring Boot aplikaciju
2. Baza i tabele se automatski kreiraju putem Liquibase-a
3. Swagger UI je dostupan na:

http://localhost:8080/nst2025demo/swagger-ui/index.html#/

### Frontend
1. Pokrenuti React aplikaciju
2. Pristupiti aplikaciji kroz browser


---


