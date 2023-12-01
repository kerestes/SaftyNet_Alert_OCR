# <center>SaftyNet Alert - Api Project</center>

 Bienvenue dans la documentation de l'API SaftyNet Alert, une interface de programmation d'application (API) REST conçue pour faciliter la gestion des informations liées à la sécurité personnelle. L'API offre trois endpoints principaux pour gérer les informations relatives aux personnes, aux casernes de pompiers et aux dossiers médicaux.

***

## <center> OBJETS </center>
**PERSONE** 

L'objet person est composé par :
- Id (id)
- Prénom (firstName)
- Nom (lastName)
- Mail (email)
- Téléphone (phone)
- Address (Objet)

**ADDRESS**

L'objet address est composé par :
- Id (id)
- Address (address)
- Ville (city)
- Code Postal (zip)
- Firestation (Objet)

**FIRESTATION**

L'objet firestation est composé par :
- Id (id)
- Nom (name)

**Medical Record**

L'objet medicalrecorde composé par :

- Id (id)
- Prénom (firstName)
- Nom (lastName)
- Data de naissance (birthday)

**Medicine**

L'objet medicine composé par :

- Id (id)
- Nom (name)
- Dosage par mg (dosage_mg)

**Allergie**

- Id (id)
- Nom (name)

***

## <center> Endpoint : /person </center>

Cet endpoint est utilisé pour gérer les informations relatives aux personnes enregistrées dans le système. Les opérations suivantes sont disponibles :

- **POST** -> "/person" : Ajoute une nouvelle personne au système. Le corps de la requête doit contenir les informations nécessaires telles que le nom, le prénom, l'adresse, le numéro de téléphone et le mail.

- **PUT** -> /person : Met à jour les informations d'une personne existante. Le corps de la requête doit contenir les champs à mettre à jour, ainsi que l'id de la personne.

- **PUT** -> /person/{id} : Au lieu de mettre l'id dans le corp de la requête, il est aussi possible d'utiliser cet endpoint avec l'id dans l'URI de la requête.  

- **DELETE** -> /person/{id} : Supprime une personne du système en utilisant son identifiant.

***

### <center> Examples </center>

***

<font color=blue>**POST** -></font> "/person"

### <font color=green>Bonne requête </font>

Pour que la requête marche bien, il faut informer toutes les données nécessaires (prénom, nom, téléphone, mail, adresse)

```
    http://localhost:8080/person
    
    {
        "firstName":"Alexandre",
        "lastName":"Kerestes",
        "phone":"1234567890",
        "email":"alexandrekerestes@exemplo.fr",
        "address":{
            "id":1
        }
    }
```
Réponse :

Status: 200 OK

```
    {
        "id": 24,
        "firstName": "Alexandre",
        "lastName": "Kerestes",
        "address": {
            "id": 1,
            "address": null,
            "city": null,
            "zip": null,
            "firestation": null
        },
        "phone": "1234567890",
        "email": "alexandrekerestes@exemplo.fr"
    }
```
Il retourne les informations de l'adresse vide, mais en vrai, elles sont bien à jour dans le système.

### <font color=red>Mauvaise requête </font>

Si on fait une requête sans au moins un des attributs clés de l'objet, la requête va échouer et retourner vide.

```agsl
    http://localhost:8080/person
    
    {
        "firstName":"Alexandre",
        "lastName":"Kerestes",
        "phone":"1234567890",
        "address":{
            "id":1
        }
    }
```

Réponse :

Status: 200 OK

```
    RÉPONSE VIDE
```
***

Si on fait une requête sans bien informer l'id de l'address le système nous retourne une erreur 500.

```agsl
    http://localhost:8080/person
    
    {
        "firstName":"Alexandre",
        "lastName":"Kerestes",
        "phone":"1234567890",
        "email":"alexandrekerestes@exemplo.fr",
        "address":{}
    }
```
Réponse :

Status: 500 Internal Server Error

```
    {
        "timestamp": "2023-12-01T06:28:24.542+00:00",
        "status": 500,
        "error": "Internal Server Error",
        "path": "/person"
    }
```

***

<font color=blue>**PUT** -></font> "/person"

### <font color=green>Bonne requête </font>

Il faut informe l'id de personne concernée, ainsi que les attributs à changer.

```agsl
    http://localhost:8080/person
    
    {
        "id":1,
        "firstName":"Alexandre"
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 1,
        "firstName": "Alexandre",
        "lastName": "Boyd",
        "address": {
            "id": 1,
            "address": "1509 Culver St",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 3,
                "name": "Fire Station 3"
            }
        },
        "phone": "8418746512",
        "email": "jaboyd@email.com"
    }
```

### <font color=red>Mauvaise requête </font>

S'il n'y a pas d'id, il retourne une réponse vide.

```agsl
    {
        "firstName":"Alexandre"
    }   
```

Réponse :

Status: 200 OK
```
    RÉPONSE VIDE
```

***

<font color=blue>**PUT** -></font> "/person/{id}"

### <font color=green>Bonne requête </font>

Dans ce cas, il ne faut pas informer l'id. Mais s'il est present dans la requête le système prendra en compte l'id de l'URI.

```agsl
    http://localhost:8080/person/2
    
    {
        "id":1,
        "lastName":"kerestes"
    }
    
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 2,
        "firstName": "Jacob",
        "lastName": "kerestes",
        "address": {
            "id": 1,
            "address": "1509 Culver St",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 3,
                "name": "Fire Station 3"
            }
        },
        "phone": "8418746513",
        "email": "drk@email.com"
    }
```

### <font color=red>Mauvaise requête </font>

Si l'id n'existe pas, il retourne une réponse vide.

```agsl
    http://localhost:8080/person/200
    
    {
        "firstName":"Alexandre"
    }   
```

Réponse :

Status: 200 OK
```
    RÉPONSE VIDE
```

***

<font color=blue>**DELETE** -></font> "/person/{id}"

### <font color=green>Bonne requête </font>

Il suffit d'informer l'id dans l'URI.

```agsl
    http://localhost:8080/person/1
```

Réponse :

Status: 200 OK
```
    RÉPONSE VIDE
```

### <font color=red>Mauvaise requête </font>

Si l'id n'existe pas, il retourne une réponse toujours vide.

***

## <center> Endpoint : /medicalrecord </center>

Cet endpoint est utilisé pour gérer les informations relatives aux côntroles medicaux enregistrées dans le système. Les opérations suivantes sont disponibles :

- **POST** -> "/medicalrecord" : Ajoute un nouveau contrôle medical au système. Le corps de la requête doit contenir les informations nécessaires telles que le nom, le prénom de la personne concernée, ainsi que la liste de medicament et/ou d'allergies (au cas où).

- **PUT** -> /medicalrecord : Met à jour les informations d'un contrôle medical existant. Le corps de la requête doit contenir les champs à mettre à jour, ainsi que l'id du contrôle medical. Les allergies et les medicament ne sont pas mis à jour sur ce endpoint

- **PUT** -> /medicalrecord/{id} : Au lieu de mettre l'id dans le corp de la requête, il est aussi possible d'utiliser cet endpoint avec l'id dans l'URI de la requête.

- **DELETE** -> /medicalrecord/{id} : Supprime un contrôle medical du système en utilisant son identifiant.

***

### <center> Examples </center>

***

<font color=blue>**POST** -></font> "/medicalrecord"

### <font color=green>Bonne requête </font>

Il n'est pas obligatoire informer les allergies et/ou les medicament du patient.
Dans les cas ou les champs medicine et allergie sont vide, le système fait l'ajout d'une liste vide.

```agsl
    http://localhost:8080/medicalrecord
    
    {
        "firstName":"Alexandre",
        "lastName":"Kerestes",
        "birthday":"1987-11-24"
    }
```
Réponse :

Status: 200 OK

```agsl
    {
        "id": 24,
        "firstName": "Alexandre",
        "lastName": "Kerestes",
        "birthday": "1987-11-24T00:00:00.000+00:00",
        "allergies": [],
        "medicines": []
    }
```

***

Il est égalment possible d'informer les allergies et les medicines par le biais de ses ids. Cependant, il faut les informer dans un outre objet appellé medicineId.

```agsl
    http://localhost:8080/medicalrecord
    
    {
        "firstName":"Alexandre",
        "lastName":"Kerestes",
        "birthday":"1987-11-24",
        "allergies":[
            {"id":1}, 
            {"id":2}
        ],
        "medicines":[
            {"medicineId":
                {"id":3}
            }, 
            {"medicineId":
                {"id":4}
            }
        ]
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 24,
        "firstName": "Alexandre",
        "lastName": "Kerestes",
        "birthday": "1987-11-24T00:00:00.000+00:00",
        "allergies": [
            {
                "id": 1,
                "name": null
            },
            {
                "id": 2,
                "name": null
            }
        ],
        "medicines": [
            {
                "quantity": 1,
                "medicineId": {
                    "id": 3,
                    "name": null,
                    "dosage_mg": 0
                },
                "medicalrecordId": 24
            },
            {
                "quantity": 1,
                "medicineId": {
                    "id": 4,
                    "name": null,
                    "dosage_mg": 0
                },
                "medicalrecordId": 24
            }
        ]
    }
```

Il retourne des informations vides par rapport aux allergies et aux médicaments, néanmoins, les données ont été insérées avec succèss 

### <font color=red>Mauvaise requête </font>

S'il manque des infromation de base, le système renvoie une réponse vide.

```agsl
    http://localhost:8080/medicalrecord
    
    {
        "firstName":"Alexandre",
        "birthday":"1987-11-24"
    }
```

Réponse :

Status: 200 OK
```
    RÉPONSE VIDE
```

***

Une mauvaise formulation de la requête par rapport aux médicaments va générer une erreur.

```agsl
    http://localhost:8080/medicalrecord
    
    {
        "firstName":"Alexandre",
        "lastName":"Kerestes",
        "birthday":"1987-11-24",
        "allergies":[
            {"id":1}, 
            {"id":2}
        ],
        "medicines":[
            {"id":3},
            {"id":4}
        ]
    }
```

Réponse :

Status: 500 Internal Server Error

```
    {
        "timestamp": "2023-12-01T05:55:50.750+00:00",
        "status": 500,
        "error": "Internal Server Error",
        "path": "/medicalrecord"
    }
```

***

<font color=blue>**PUT** -></font> "/medicalrecord"

### <font color=green>Bonne requête </font>

Il suffit d'informer l'id du contrôle medical, ainsi que les données à changer. Ce pas possible de mettre à jour les information des allergies et/ou des médicaments sur cet endpoint.

```agsl
    http://localhost:8080/medicalrecord
    
    {
        "id":5,
        "firstName":"Jean",
        "birthday":"1987-11-24"
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 5,
        "firstName": "Jean",
        "lastName": "Boyd",
        "birthday": "1987-11-24T00:00:00.000+00:00",
        "allergies": [
            {
                "id": 6,
                "name": "xilliathal"
            }
        ],
        "medicines": [
            {
                "quantity": 1,
                "medicineId": {
                    "id": 10,
                    "name": "tetracyclaz",
                    "dosage_mg": 650
                },
                "medicalrecordId": 5
            }
        ]
    }
```

***

Si on essaie de mettre à jour des informations des allergies ou des médicaments, le système va ignorer cette partie de la requête.

```agsl
    http://localhost:8080/medicalrecord
    
    {
        "id":5,
        "firstName":"Antoine",    
        "birthday":"1999-09-30",
        "allergies":[
            {"id":3}
        ],
        "medicines":[
            {"medicineId":{
                    "id":3
                }
            }
        ]
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 5,
        "firstName": "Antoine",
        "lastName": "Boyd",
        "birthday": "1999-09-30T00:00:00.000+00:00",
        "allergies": [
            {
                "id": 6,
                "name": "xilliathal"
            }
        ],
        "medicines": [
            {
                "quantity": 1,
                "medicineId": {
                    "id": 10,
                    "name": "tetracyclaz",
                    "dosage_mg": 650
                },
                "medicalrecordId": 5
            }
        ]
    }
```

### <font color=red>Mauvaise requête </font>

Si on essaie de faire un mis à jour sans id, le système va retourner une réponse vide. 

```agsl
    http://localhost:8080/medicalrecord
    
    {
        "firstName":"Antoine",
        "lastName":"Rodrigues",
        "birthday":"1999-09-30"
    }
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

***

<font color=blue>**PUT** -></font> "/medicalrecord/{id}"

### <font color=green>Bonne requête </font>

Si on essaie d'informer l'id dans le corp de la requête le système utilisera toujours l'id passé par l'URI.

```agsl
    http://localhost:8080/medicalrecord/10
    
    {
        "id": 5,
        "firstName":"Antoine",
        "lastName":"Rodrigues",
        "birthday":"1999-09-30"
    }
```

Réponse :

Status: 200 OK

```agsl
     {
        "id": 10,
        "firstName": "Antoine",
        "lastName": "Rodrigues",
        "birthday": "1999-09-30T00:00:00.000+00:00",
        "allergies": [
            {
                "id": 5,
                "name": "shellfish"
            }
        ],
        "medicines": [
            {
                "quantity": 3,
                "medicineId": {
                    "id": 5,
                    "name": "hydrapermazol",
                    "dosage_mg": 100
                },
                "medicalrecordId": 10
            },
            {
                "quantity": 1,
                "medicineId": {
                    "id": 4,
                    "name": "dodoxadin",
                    "dosage_mg": 30
                },
                "medicalrecordId": 10
            }
        ]
    }
```

### <font color=red>Mauvaise requête </font>

Si on se trompe dans l'écriture des noms des attibuts, le sistem ne les prendra pas en compte.

```agsl
    http://localhost:8080/medicalrecord/4
    
    {
        "prenom":"Antoine",
        "surnome":"Rodrigues",
        "naissance":"1999-09-30"
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 4,
        "firstName": "Roger",
        "lastName": "Boyd",
        "birthday": "2017-06-08T22:00:00.000+00:00",
        "allergies": [],
        "medicines": []
    }
```

***

<font color=blue>**DELETE** -></font> "/medicalrecord/{id}"

### <font color=green>Bonne requête </font>

Pour effacer une donnée, il suffit d'informer son id dans l'URI.

```agsl
    http://localhost:8080/medicalrecord/4
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

### <font color=red>Mauvaise requête </font>

Si l'id n'existe pas, il retourne une réponse toujours vide.

***

## <center> Endpoint : /firestation </center>

Cet endpoint est utilisé pour gérer les informations relatives aux casernes de pompier (Firestation) enregistrées dans le système. Les opérations suivantes sont disponibles :

- **POST** -> "/firestation" : Ajoute un nouveau firestation au système. Le corps de la requête doit contenir le nom de la caserne.
- **PUT** -> /firestation : Met à jour les informations d'une caserne de pompier existant. Le corps de la requête doit contenir les champs à mettre à jour, ainsi que l'id de la caserne.
- **PUT** -> /firestation/{id} : Au lieu de mettre l'id dans le corp de la requête, il est aussi possible d'utiliser cet endpoint avec l'id dans l'URI de la requête.
- **PUT** -> /firestation/toaddress/{id} : Ajoute ou modifie la caserne par rapport une adresse. Dans le corp de la requête, il faut remplir l'id de l'adresse et da l'URI, il faut remplir l'id de la caserne.
- **DELETE** -> /firestation/{id} : Supprime une caserne de pompier du système en utilisant son identifiant.
- **DELETE** -> /firestation/toaddress/{id} : Supprime une caserne dans une adresse. Il faut informer l'id de l'adresse que sera orphelin.

***

### <center> Examples </center>

***

<font color=blue>**POST** -></font> "/firestation"

### <font color=green>Bonne requête </font>

L'insertion de Firestation ne demande que le nom de la caserne de pompier.

```agsl
    http://localhost:8080/firestation

    {
        "name":"Firestation 5"
    }
```
Réponse :

Status: 200 OK

```agsl
    {
        "id": 5,
        "name": "Firestation 5"
    }
```

***

<font color=blue>**PUT** -></font> "/firestation"

### <font color=green>Bonne requête </font>

Il faut toujours informer l'id de la caserne.

```agsl
    http://localhost:8080/firestation/

    {
        "id":5,
        "name":"Nouveau Nom - Firestation 5"
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 5,
        "name": "Nouveau Nom - Firestation 5"
    }
```

### <font color=red>Mauvaise requête </font>

S'il n'y a pas d'id le système retourne vide.

```agsl
    http://localhost:8080/firestation
    
    {
        "name":"Nouveau Nom - Firestation 5"
    }
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

***

Si l'id n'existe pas le système retourne vide.

```agsl
    http://localhost:8080/firestation
    
    {
        "id":500,
        "name":"Nouveau Nom - Firestation 5"
    }
```
Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

***

<font color=blue>**PUT** -></font> "/firestation/{id}"

### <font color=green>Bonne requête </font>

Dans ce cas, on passe l'id par l'URI

```agsl
    http://localhost:8080/firestation/5
    
    {
        "name":"Nouveau Nom - Firestation 5"
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 5,
        "name": "Nouveau Nom - Firestation 5"
    }
```

### <font color=red>Mauvaise requête </font>

Si l'id n'existe pas le système retourne vide.

***

<font color=blue>**PUT** -></font> "/firestation/toaddress/{id}"

### <font color=green>Bonne requête </font>

L'id dans L'URI correspond à l'id de l'adresse et l'id dans le corps de la requête à la caserne de pompier (Firestation).

```agsl
    http://localhost:8080/firestation/toaddress/4
    
    {
        "fistationId":5
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 5,
        "address": "748 Townings Dr",
        "city": "Culver",
        "zip": "97451",
        "firestation": {
            "id": 4,
            "name": "Fire Station 4"
        }
    }
```

### <font color=red>Mauvaise requête </font>

Si l'id de la caserne ou l'id de l'address n'existe pas la réponse sera vide.

```agsl
    http://localhost:8080/firestation/toaddress/200

    {
        "fistationId":300
    }
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

***

<font color=blue>**DELETE** -></font> "/firestation/{id}"

### <font color=green>Bonne requête </font>

Il suffit d'insérer l'id de la caserne qui sera effacée.

```agsl
    http://localhost:8080/firestation/4
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

### <font color=red>Mauvaise requête </font>

Si l'id n'existe pas le système retourne toujours vide.

***

<font color=blue>**DELETE** -></font> "/firestation/toaddress/{id}"

### <font color=green>Bonne requête </font>

Il faut insérer l'id de l'adresse qui sera orpheline.

```agsl
    http://localhost:8080/firestation/toaddress/2
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

### <font color=red>Mauvaise requête </font>

Si l'id n'existe pas le système retourne toujours vide.

