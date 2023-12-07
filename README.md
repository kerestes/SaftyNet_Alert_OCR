# <center>SaftyNet Alert - Api Project</center>

 Bienvenue dans la documentation de l'API SaftyNet Alert, une interface de programmation d'application (API) REST conçue pour faciliter la gestion des informations liées à la sécurité personnelle. L'API offre trois endpoints principaux pour gérer les informations relatives aux personnes, aux casernes de pompiers et aux dossiers médicaux.

***

## <center> OBJETS </center>
**PERSONE** 

L'objet person est composé par :
- Id (id)
- Prénom (firstName)
- Nom (lastName)
- Birthday (birthday)
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

- **GET** -> "/personInfo?lastName=<`lastName`>" : Retourne la personne avec le nom renseigné. Si plusieurs personnes portent le même nom, le système va retourner une liste de personnes.

- **GET** -> "/communityEmail?city=<`city`>" : Retourne la liste de mail par ville.

- **GET** -> "/childAlert?address=<`address`>" : Il retourne une liste des habitants de l'adresse, s'il y a des enfants.

- **POST** -> "/person" : Ajoute une nouvelle personne au système. Le corps de la requête doit contenir les informations nécessaires telles que le nom, le prénom, l'adresse, le numéro de téléphone et le mail.

- **PUT** -> /person : Met à jour les informations d'une personne existante. Le corps de la requête doit contenir les champs à mettre à jour, ainsi que l'id de la personne.

- **PUT** -> /person/{id} : Au lieu de mettre l'id dans le corp de la requête, il est aussi possible d'utiliser cet endpoint avec l'id dans l'URI de la requête.  

- **DELETE** -> /person/{id} : Supprime une personne du système en utilisant son identifiant.

***

### <center> Examples </center>

***

<font color=blue>**GET** -></font> "/personInfo?lastName=<`lastName`>"

### <font color=green>Bonne requête </font>

Il suffit d'informer le nom, ou une partie du nom.

```agsl
    localhost:8080/personInfo?lastName=pete
```

Réponse :

Status: 200 OK

```agsl
    [
        {
            "id": 9,
            "address": "908 73rd St",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 1,
                "name": "Fire Station 1"
            },
            "persons": [
                {
                    "id": 16,
                    "firstName": "Jamie",
                    "lastName": "PETERS",
                    "age": 41,
                    "phone": "8418747462",
                    "email": "jpeter@email.com",
                    "allergies": [],
                    "medicines": []
                }
            ]
        },
        {
            "id": 6,
            "address": "112 Steppes Pl",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 4,
                "name": "Fire Station 4"
            },
            "persons": [
                {
                    "id": 17,
                    "firstName": "Ron",
                    "lastName": "PETERS",
                    "age": 58,
                    "phone": "8418748888",
                    "email": "jpeter@email.com",
                    "allergies": [],
                    "medicines": []
                }
            ]
        }
    ]
```

### <font color=red>Mauvaise requête </font>

Si le nom n'existe pas le système retourne vide

***

<font color=blue>**GET** -></font> "/communityEmail?city=<`city`>"

### <font color=green>Bonne requête </font>

Il suffit d'informer le nom de la ville ou une partie du nom.

```agsl
    localhost:8080/communityEmail?city=Culv
```

Réponse :

Status: 200 OK

```agsl
    {
        "Emails from Culver": [
            "jaboyd@email.com",
            "drk@email.com",
            "tenz@email.com",
            "clivfd@ymail.com",
            "tcoop@ymail.com",
            "jpeter@email.com",
            "aly@imail.com",
            "lily@email.com",
            "soph@email.com",
            "ward@email.com",
            "zarc@email.com",
            "reg@email.com",
            "bstel@email.com",
            "ssanw@email.com",
            "gramps@email.com"
        ]
    }
```

### <font color=red>Mauvaise requête </font>

Si le nom de la ville n'existe pas le système retourne vide.

***

<font color=blue>**GET** -></font> "/childAlert?address=<`address`>"

### <font color=green>Bonne requête </font>

Il faut informer le numéro ou le nom (ou une partie du nom) de la rue, avenue, boulevard, etc.

```agsl
    localhost:8080/childAlert?address=dow
```

Réponse :

Status: 200 OK

```agsl
   {
        "id": 8,
        "address": "892 Downing Ct",
        "city": "Culver",
        "zip": "97451",
        "firestation": {
            "id": 2,
            "name": "Fire Station 2"
        },
        "minor": [
            {
                "id": 14,
                "firstName": "Zach",
                "lastName": "ZEMICKS",
                "age": 6,
                "phone": "8418747512",
                "email": "zarc@email.com"
            }
        ],
        "major": [
            {
                "id": 12,
                "firstName": "Sophia",
                "lastName": "ZEMICKS",
                "age": 35,
                "phone": "8418747878",
                "email": "soph@email.com"
            },
            {
                "id": 13,
                "firstName": "Warren",
                "lastName": "ZEMICKS",
                "age": 38,
                "phone": "8418747512",
                "email": "ward@email.com"
            }
        ]
    }
```

### <font color=red>Mauvaise requête </font>

S'il n'y a pas d'enfant sur l'adresse ou si l'adresse n'existe pas le système retourne vide.

***

<font color=blue>**POST** -></font> "/person"

### <font color=green>Bonne requête </font>

Pour que la requête marche bien, il faut informer toutes les données nécessaires (prénom, nom, téléphone, mail, adresse)

```
    http://localhost:8080/person
    
    {
        "firstName":"Alexandre",
        "lastName":"Kerestes",
        "birthday":"1987-11-24",
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
        "lastName": "KERESTES",
        "birthday": "1987-11-24T00:00:00.000+00:00",
        "phone": "1234567890",
        "email": "alexandrekerestes@exemplo.fr",
        "allergies": [],
        "medicines": []
    }
```
Il ne retourne pas les informations de l'adresse, mais en vrai, elles sont bien à jour dans le système.

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
        "lastName": "BOYD",
        "birthday": "1984-03-05T23:00:00.000+00:00",
        "phone": "8418746512",
        "email": "jaboyd@email.com",
        "allergies": [
            {
                "id": 3,
                "name": "nillacilan"
            }
        ],
        "medicines": [
            {
                "quantity": 1,
                "medicineId": {
                    "id": 3,
                    "name": "aznol",
                    "dosage_mg": 350
                },
                "personId": 1
            },
            {
                "quantity": 1,
                "medicineId": {
                    "id": 5,
                    "name": "hydrapermazol",
                    "dosage_mg": 100
                },
                "personId": 1
            }
        ]
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
        "lastName": "KERESTES",
        "birthday": "1989-03-05T23:00:00.000+00:00",
        "phone": "8418746513",
        "email": "drk@email.com",
        "allergies": [],
        "medicines": [
            {
                "quantity": 2,
                "medicineId": {
                    "id": 9,
                    "name": "pharmacol",
                    "dosage_mg": 2500
                },
                "personId": 2
            },
            {
                "quantity": 1,
                "medicineId": {
                    "id": 11,
                    "name": "terazine",
                    "dosage_mg": 10
                },
                "personId": 2
            },
            {
                "quantity": 1,
                "medicineId": {
                    "id": 8,
                    "name": "noznazol",
                    "dosage_mg": 250
                },
                "personId": 2
            }
        ]
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

- **GET** -> "/fire?address=<`address`>" : Il retourne la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne de pompiers la desservant

- **PUT** -> "/allergy/{personId}" : Il ajoute une allergie à une personne

- **PUT** -> "/medicine/{personId}" : Il ajoute un médicament à une personne.

- **DELETE** -> "/medicine/{personId}/{medicineId}" : Il supprime un médicament de la liste de médicaments d'une personne.

- **DELETE** -> "/allergy/{personId}/{allergyId}" : Il supprime une allergie de la liste de médicaments d'une personne.

***

### <center> Examples </center>

***

<font color=blue>**GET** -></font> "/fire?address=<`address`>"

### <font color=green>Bonne requête </font>

Le système retourne une liste des habitants de l'adresse s'il existe.

```agsl
    localhost:8080/fire?address=Downing
```

Réponse :

200 OK

```agsl
    {
        "id": 8,
        "address": "892 Downing Ct",
        "city": "Culver",
        "zip": "97451",
        "firestation": {
            "id": 2,
            "name": "Fire Station 2"
        },
        "persons": [
            {
                "id": 12,
                "firstName": "Sophia",
                "lastName": "ZEMICKS",
                "age": 35,
                "phone": "8418747878",
                "email": "soph@email.com",
                "allergies": [
                    {
                        "id": 4,
                        "name": "peanut"
                    },
                    {
                        "id": 5,
                        "name": "shellfish"
                    },
                    {
                        "id": 1,
                        "name": "aznol"
                    }
                ],
                "medicines": [
                    {
                        "quantity": 1,
                        "medicineId": {
                            "id": 1,
                            "name": "aznol",
                            "dosage_mg": 60
                        },
                        "personId": 12
                    },
                    {
                        "quantity": 9,
                        "medicineId": {
                            "id": 5,
                            "name": "hydrapermazol",
                            "dosage_mg": 100
                        },
                        "personId": 12
                    },
                    {
                        "quantity": 2,
                        "medicineId": {
                            "id": 9,
                            "name": "pharmacol",
                            "dosage_mg": 2500
                        },
                        "personId": 12
                    },
                    {
                        "quantity": 1,
                        "medicineId": {
                            "id": 12,
                            "name": "terazine",
                            "dosage_mg": 500
                        },
                        "personId": 12
                    }
                ]
            },
            {
                "id": 13,
                "firstName": "Warren",
                "lastName": "ZEMICKS",
                "age": 38,
                "phone": "8418747512",
                "email": "ward@email.com",
                "allergies": [],
                "medicines": []
            },
            {
                "id": 14,
                "firstName": "Zach",
                "lastName": "ZEMICKS",
                "age": 6,
                "phone": "8418747512",
                "email": "zarc@email.com",
                "allergies": [],
                "medicines": []
            }
        ]
    }
```

### <font color=red>Mauvaise requête </font>

Si l'adresse n'existe pas il va retourne une réponse vide.

***

<font color=blue>**PUT** -></font> "/allergy/{personId}"

### <font color=green>Bonne requête </font>

Dans le corp de la requête, il faut informer l'id de l'allergie (allergyId).

```agsl
    http://localhost:8080/allergy/1 
    
    {
        "allergyId":1
    }
```
Réponse :

Status: 200 OK
    
```agsl
    {
        "id": 1,
        "firstName": "John",
        "lastName": "BOYD",
        "birthday": "1984-03-05T23:00:00.000+00:00",
        "phone": "8418746512",
        "email": "jaboyd@email.com",
        "allergies": [
            {
                "id": 3,
                "name": "nillacilan"
            },
            {
                "id": 1,
                "name": "aznol"
            }
        ],
        "medicines": [
            {
                "quantity": 1,
                "medicineId": {
                    "id": 3,
                    "name": "aznol",
                    "dosage_mg": 350
                },
                "personId": 1
            },
            {
                "quantity": 1,
                "medicineId": {
                    "id": 5,
                    "name": "hydrapermazol",
                    "dosage_mg": 100
                },
                "personId": 1
            }
        ]
    }
```

### <font color=red>Mauvaise requête </font>

Si l'id de l'allergie ou l'id de la personne n'est pas enregistrée dans la base des données la réponse sera vide.

***

<font color=blue>**PUT** -></font> "/medicine/{personId}"

### <font color=green>Bonne requête </font>

Il faut informer l'id de la personne sur l'URI et l'id du médicament dans le corp de la requête (medicineId), il est aussi possible d'informer la quantité.

Si la quantité n'est pas informée, la valeur default sera toujours 1.

```agsl
    http://localhost:8080/medicine/1
    
    {
        "medicineId":2
    }
```

Réponse :

Status: 200 OK

```agsl
    {
    "id": 1,
    "firstName": "John",
    "lastName": "BOYD",
    "birthday": "1984-03-05T23:00:00.000+00:00",
    "phone": "8418746512",
    "email": "jaboyd@email.com",
    "allergies": [
        {
            "id": 3,
            "name": "nillacilan"
        }
    ],
    "medicines": [
        {
            "quantity": 1,
            "medicineId": {
                "id": 3,
                "name": "aznol",
                "dosage_mg": 350
            },
            "personId": 1
        },
        {
            "quantity": 1,
            "medicineId": {
                "id": 5,
                "name": "hydrapermazol",
                "dosage_mg": 100
            },
            "personId": 1
        },
        {
            "quantity": 1,
            "medicineId": {
                "id": 2,
                "name": "aznol",
                "dosage_mg": 200
            },
            "personId": 1
        }
    ]
}
```

***

Si on me la quantité on peut utiliser cette requête pour mettre à jour les données estoquées.

```agsl
    http://localhost:8080/medicine/1
    
    {
        "quantity":4,
        "medicineId":2
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 1,
        "firstName": "John",
        "lastName": "BOYD",
        "birthday": "1984-03-05T23:00:00.000+00:00",
        "phone": "8418746512",
        "email": "jaboyd@email.com",
        "allergies": [
            {
                "id": 3,
                "name": "nillacilan"
            }
        ],
        "medicines": [
            {
                "quantity": 1,
                "medicineId": {
                    "id": 3,
                    "name": "aznol",
                    "dosage_mg": 350
                },
                "personId": 1
            },
            {
                "quantity": 1,
                "medicineId": {
                    "id": 5,
                    "name": "hydrapermazol",
                    "dosage_mg": 100
                },
                "personId": 1
            },
            {
                "quantity": 4,
                "medicineId": {
                    "id": 2,
                    "name": "aznol",
                    "dosage_mg": 200
                },
                "personId": 1
            }
        ]
    }
```

### <font color=red>Mauvaise requête </font>

Si l'id du médicament ou l'id de la personne n'est pas enregistrée dans la base des données la réponse sera vide.

***

<font color=blue>**DELETE** -></font> "/medicine/{personId}/{medicineId}"

### <font color=green>Bonne requête </font>

Il faut informer l'id de la personne et du médicament, ainsi le système effacera le médicament de la liste de médicament de cette personne.

```agsl
    localhost:8080/medicine/1/2
```

Réponse :

Status: 200 OK

```agsl
     RÉPONSE VIDE
```

### <font color=red>Mauvaise requête </font>

Si l'id du médicament ou de la personne n'existe pas le système retourne toujours vide.

***

<font color=blue>**DELETE** -></font> "/medicalrecord/{id}"

### <font color=green>Bonne requête </font>

Pour effacer une donnée, il suffit d'informer son id dans l'URI.

```agsl
    localhost:8080/allergy/1/2
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

### <font color=red>Mauvaise requête </font>

Si l'id de l'allergie ou de la personne n'existe pas le système retourne toujours vide.

***

## <center> Endpoint : /firestation </center>

Cet endpoint est utilisé pour gérer les informations relatives aux casernes de pompier (Firestation) enregistrées dans le système. Les opérations suivantes sont disponibles :

- **GET** -> "/phoneAlert?firestation=<`firestationId`>" : Il retourne la liste de téléphone par l'id de la caserne de pompier.
- **GET** -> "/firestation?stationNumber=<`stationNumber`>" : Il retourne une liste d'adresse et les personnes qui y sont attachées.
- **POST** -> "/firestation" : Ajoute un nouveau firestation au système. Le corps de la requête doit contenir le nom de la caserne.
- **PUT** -> /firestation : Met à jour les informations d'une caserne de pompier existant. Le corps de la requête doit contenir les champs à mettre à jour, ainsi que l'id de la caserne.
- **PUT** -> /firestation/{id} : Au lieu de mettre l'id dans le corp de la requête, il est aussi possible d'utiliser cet endpoint avec l'id dans l'URI de la requête.
- **PUT** -> /firestation/toaddress/{id} : Ajoute ou modifie la caserne par rapport une adresse. Dans le corp de la requête, il faut remplir l'id de l'adresse et da l'URI, il faut remplir l'id de la caserne.
- **DELETE** -> /firestation/{id} : Supprime une caserne de pompier du système en utilisant son identifiant.
- **DELETE** -> /firestation/toaddress/{id} : Supprime une caserne dans une adresse. Il faut informer l'id de l'adresse que sera orphelin.

***

### <center> Examples </center>

***

<font color=blue>**GET** -></font> "/phoneAlert?firestation=<`firestationId`>"

### <font color=green>Bonne requête </font>

Il faut informer l'id de la caserne de pompier.

```agsl
    http://localhost:8080/phoneAlert?firestationId=1
```

RÉPONSE :

200 OK

```agsl
    {
        "Firestation 1": [
            "8418747784",
            "8418747462",
            "8418747458",
            "8418746512",
            "8418748547"
        ]
    }
```

### <font color=green>Bonne requête </font>

Si l'id n'existe pas la réponse sera vide.

***

<font color=blue>**GET** -></font> "/firestation?stationNumber=<`stationNumber`>"

### <font color=green>Bonne requête </font>

Il faut informer l'id de la caserne de pompier

```agsl
    http://localhost:8080/firestation?stationNumber=1
```

RÉPONSE :

200 OK

```agsl
    [
        {
            "id": 11,
            "address": "951 LoneTree Rd",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 1,
                "name": "Fire Station 1"
            },
            "minor": [],
            "major": [
                {
                    "id": 23,
                    "firstName": "Eric",
                    "lastName": "CADIGAN",
                    "age": 78,
                    "phone": "8418747458",
                    "email": "gramps@email.com"
                }
            ]
        },
        {
            "id": 10,
            "address": "947 E. Rose Dr",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 1,
                "name": "Fire Station 1"
            },
            "minor": [
                {
                    "id": 21,
                    "firstName": "Kendrik",
                    "lastName": "STELZER",
                    "age": 9,
                    "phone": "8418747784",
                    "email": "bstel@email.com"
                }
            ],
            "major": [
                {
                    "id": 19,
                    "firstName": "Brian",
                    "lastName": "STELZER",
                    "age": 48,
                    "phone": "8418747784",
                    "email": "bstel@email.com"
                },
                {
                    "id": 20,
                    "firstName": "Shawna",
                    "lastName": "STELZER",
                    "age": 43,
                    "phone": "8418747784",
                    "email": "ssanw@email.com"
                }
            ]
        },
        {
            "id": 9,
            "address": "908 73rd St",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 1,
                "name": "Fire Station 1"
            },
            "minor": [],
            "major": [
                {
                    "id": 15,
                    "firstName": "Reginold",
                    "lastName": "WALKER",
                    "age": 44,
                    "phone": "8418748547",
                    "email": "reg@email.com"
                },
                {
                    "id": 16,
                    "firstName": "Jamie",
                    "lastName": "PETERS",
                    "age": 41,
                    "phone": "8418747462",
                    "email": "jpeter@email.com"
                }
            ]
        },
        {
            "id": 4,
            "address": "644 Gershwin Cir",
            "city": "Culver",
            "zip": "97451",
            "firestation": {
                "id": 1,
                "name": "Fire Station 1"
            },
            "minor": [],
            "major": [
                {
                    "id": 8,
                    "firstName": "Peter",
                    "lastName": "DUCAN",
                    "age": 23,
                    "phone": "8418746512",
                    "email": "jaboyd@email.com"
                }
            ]
        }
    ]
```

### <font color=green>Bonne requête </font>

Si l'id n'existe pas la réponse sera vide.

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

### <font color=red>Mauvaise requête </font>

S'il les données nécessaires (name) ne sont pas là, le système retourne erreur 500.

```agsl
    http://localhost:8080/firestation
    
    {
        "names":"Firestation 5"
    }
```

RÉPONSE :

500 Internal Server Error

```agsl
    {
        "timestamp": "2023-12-07T02:59:32.555+00:00",
        "status": 500,
        "error": "Internal Server Error",
        "path": "/firestation"
    }
```

***

<font color=blue>**PUT** -></font> "/firestation"

### <font color=green>Bonne requête </font>

Il faut toujours informer l'id de la caserne.

```agsl
    http://localhost:8080/firestation/

    {
        "id":1,
        "name":"Nouveau Nom - Firestation 1"
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 1,
        "name": "Nouveau Nom - Firestation 1"
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
    http://localhost:8080/firestation/1
    
    {
        "name":"Nouveau Nom - Firestation 1"
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 1,
        "name": "Nouveau Nom - Firestation 1"
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
        "firestationId":1
    }
```

Réponse :

Status: 200 OK

```agsl
    {
        "id": 4,
        "address": "644 Gershwin Cir",
        "city": "Culver",
        "zip": "97451",
        "firestation": {
            "id": 1,
            "name": "Firestation 1"
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
    http://localhost:8080/firestation/toaddress/4
```

Réponse :

Status: 200 OK

```agsl
    RÉPONSE VIDE
```

### <font color=red>Mauvaise requête </font>

Si l'id n'existe pas le système retourne toujours vide.