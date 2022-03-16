# DFEProject
Final project for the DFE software bootcamp run by QA

## Overview:

This is an API for creating a catalogue of monsters for a hypothetical game, in which one can create a monster, build a team, then run some sort of combat.

### Running the program:

To run this programme you need to first create a local database called "monster", then ensure that in the application.properties file within src/main/resources, spring.datasource.url= points to this database.

Also ensure that the username and password match what you use for your database.

Then, with maven installed, on your command line directed to the DFEProject file, run "mvn clean" then "mvn package".

Once that is compiled, direct your command line to the target folder within project and run "java -jar project-0.0.1-SNAPSHOT.jar" to run the file. You can then interact with this programme via Postman using the commands listed below.

### Monsters

Each monster has 5 attributes: Name, Description, Type, Attack, Health and Abilities.

Name and Description are plain text used to distinguish and add flavour to each monster.

Abilities are comprised of an ID, a name and a description. In the hypothetical game they would alter how a monster acts in combat. Abilities are treated as seperate entities which can be created, read, edited and deleted.

Type is a plain text value converted into an enum and determines not only the kind of monster being created but also determines the base attack and base health (the minimum values of attack and health) for each monster, as well as automatically giving the monster an ability.

Attack and Health are both integer values that would be the main aspect of the combat element, when creating a monster, one inputs the value by which they wish to increase that monster's base value (for instance if the monster had a base attack of 2, and input 3 for attack on creation, the resulting monster would have an attack of 5).

When creating a monster, there is a limited number of 'build points' which determines the maximum amount of Attack, health and abilities each monster can have beyond the values given to it by its type. Each point of attack and defense cost 1 build point (unless they are part of that monster's type's base attack or base health)

The default 'maximum build points' is 10, but this can be edited in the "skillPoints" class within the enum folder of the project. 

Additionally by default, each ability beyond what is provided by type costs 3 build points, but this can also be edited within the "skillPoints" class.

Abilities cannot be added simply within the create method, but existing monsters can have abilities added or removed if they do not exceed the maximum number of build points.

#### Monster commands:

###### Create:   
POST  
http://localhost:8080/mon/create  
input body: JSON  
fields:  
{  
   "name": "[name]",  
    "attack": [attack value],  
    "health": [health value],  
    "type": "[Type]",  
    "description": "[description]"  
}  
This JSON represents an "unbuilt" monster (one does not have type benefits added), and on creation each monster will be "built" which means that the attack and health will be increased according to the monster's type, and any innate abilities will be added.

###### Get All:  
GET  
http://localhost:8080/mon/getAll  
input body: none  

This returns all monsters stored within the database.

###### Get By ID:  
GET  
http://localhost:8080/mon/get/{id}  
input body: none  
Returns a single monster with the id given in the URL

###### Get Type:  
GET  
http://localhost:8080/mon/getType/{type}  
input body: none  
Returns all monsters which belong to the type given in the url. It is not case-sensitive but will throw an error if there is not an existing type that matches the input

###### Get Name:  
GET  
http://localhost:8080/mon/getName/{name}  
input body: none  
Returns all monsters whose names include the input within the   url.

###### Compare:  
GET  
http://localhost:8080/mon/compare/{Monster ID 1}/{Monster ID 2}   
input body: none  
Returns a plain text comparison of the monsters with the IDs specified in the URL.

###### Update:  
PUT  
http://localhost:8080/mon/update/{id}  
input body: JSON  
fields:  
{  
   "name": "[name]",  
    "attack": [attack],  
    "health": [health],  
    "typeStr": "[type]",   
    "description": "[description}",  
    "built": [true/false}  
}  

Updates the monster with the given id with the information provided in the request body. If built is set to true, the information will stay as provided. If built is false, the monster will automatically be 'built' by increasing the monster's attack and defense in accordance to the monster's types' base attack and health. Additionally any innate abilities will be added.

###### Change stats:  
PATCH  
http://localhost:8080/mon/change{Stat}/{amount}  
input body: none  

by replacing {stat} with either Attack or Health, one can change the amount of health or attack a monster has by an amount specified in the url. By inputting a positive number, you can increase the value, and by inputting a negative number you can decrease it. You cannot increase health or attack beyond what the maximum build points allows, nor can you reduce that monster's attack or health to lower than that monster's base attack or base health.

###### Add ability:  
POST  
http://localhost:8080/mon/addAbility/{id}/{Ability name}  
input body: none  
  
Adds the ability that matches the inputted "Ability name" to the monster with the specified ID. You cannot add abilities to a monster if it already has that ability.

###### Remove Ability:  
DELETE  
http://localhost:8080/mon/removeAbility/{id}/{Ability name}  
input body: none  

Removes the ability that matches the inputted "Ability name" from the monster with the specified ID. You can only remove abilities that the monster already has, and you cannot remove abilities provided by the monster's type.

Delete:  
DELETE  
http://localhost:8080/mon/delete/{id}  
input body: none  

Deletes the monster with the given ID.


#### Ability commands:  
 
###### Create:    
POST   
http://localhost:8080/ability/create  
input body: JSON  
fields:  
{   
	"name": "[name]"  
	"description": "[description]"  
}  
  
Creates a new Ability with the given name and description

Get######  All:  
GET  
http://localhost:8080/ability/getAll  
input body: none  

Returns all Abilities in the database.

###### Find innate:  
GET  
http://localhost:8080/ability/findInnate/{type}  
input body: none  

For the type inputted in the URL, outputs the ability that is provided by that type.

###### Find ID:  
GET  
http://localhost:8080/ability/findID/{id}  
input body: none  

Returns the ability with the ID inputted in the url.

###### Find Name:  
GET   
http://localhost:8080/ability/findName/{name}  
input body: none  

returns the ability which has a name that matches the input.

###### Update:  
PUT  
http://localhost:8080/ability/update/{id}  
input body: JSON  
fields:  
{  
	"name": "[name]"  
	"description": "[description]"  
}  
 
Updates the Ability with the given ID, with the provided fields.

NOTE: A last minute error shows this does not work. (It does not return anything (not even an error message) and has no effect. This will be sorted in a future version. 

###### Delete:  
DELETE  
http://localhost:8080/ability/delete/{id}  
input body: none  

Deletes the ability with the given id, unless that Ability is innate to an existing type.

### Types:

| Type name   | Base Attack | Base Health | Innate Ability |
|-------------|-------------|-------------|----------------|
| Abomination |      2      |      5      |  Retaliate     |
| Beast       |      3      |      4      |  Momentum      |
| Celestial   |      4      |      3      |  Perfectionist |
| Construct   |      2      |      5      |  Sturdy        |
| Demon       |      5      |      2      |  Draining      |
| Dragon      |      5      |      2      |  Critical      |
| Elemental   |      6      |      1      |  Reflect       |
| Fae         |      4      |      3      |  Evade         |
| Nature      |      3      |      4      |  Regenerate    |
| Undead      |      1      |      6      |  Revive        |

## Writeup:

### Why are we doing this?

This programme is made as a simple way to create a catalogue of monsters for a hypothetical game. By using this programme, one can make your own monsters and give them statistics and abilities. In the future this programme is planned to be extended to include a "battle" function which is able to use these abilities and statistics to simulate combat between individual monsters or teams of monsters in an "auto-battler" format.

### How I expected the challenge to go.

I initially thought I would be able to add more functionality and add the battling mechanic into this version though this proved to be too ambitious given the time and my current experience. 

I had hoped to complete the main classes and testing within the first few days, but unexpected complications and errors caused this to take significantly longer. For instance I had originally thought of using subclasses for the different types, though due to how the rest of the project was set up, this did not coincide with how the database and springboot would interpret the data, so I had to rework 'types' into enums, which would be utilised both as an enum field within the monster class and a string field of the type's name which could be stored in the database and converted into an enum when that row in the table is selected.

Additionally, within testing, I was not as prepared for utilising jupiter and springboot tests which led to me having to learn more about these aspects as I worked. This made the testing process take significantly longer as well as causing multiple reworks to take place over the course of development.

Amongst other issues, this project provedto be significantly more complicated than I first anticipated.

### What went well?/ What didn't go as planned?

Due to how many features had to be reworked and changed throughout the project, it is difficult to describe what went well, though some standout cases were the 'compare' method and the change of types to an enum. The "compare" function, was expected to be very complicated and cause many issues, but upon coding and implementing it, I found that it worked as intended with minimal changes, as it mainly just read from two different objects and uses a couple of if-else statements, once the method reads the objects as intended, the rest works smoothly. As for the type enums, eve though it was not my original plan, it did allow for consistent references and the ability to utilise base attack and base health, without having to change the monster class itself.

As for what did not go as planned: the main pitfalls i found were related to implementing abilities. Originally I planned on making these enums as well, but this offered little flexibility and complicated how aspects were interacting. This led to me making abilities into entities with CRUD functionality. Though this meant I needed to create a many-to-many relationship of monsters and abilities. Originally I manually made a linking table between the two, though this led to infinite recursion when one is called, and invalid responses within postman when using JSON backreference. From this I switched to using the @Many-to-many annotation in springboot, as well as the use of DTOs to avoid recursion. 
Since abilities are now entites, they need to be instantiated upon start-up. This also caused issues due to springboot not reading the SQL statements in my data-sql and data-schema.sql files as intended, leading to them not instantiating properly. The current solution was to utilise the create-drop option for hibernate.ddl.auto because there were issues recognising the "ignore" modifier on the sql commands to insert the base abilities, though this is not ideal as it limits the level of persistence found in the project.

The final setback was, even though all the tests regrding it work, when postman is used to update an ability, no message is given in return as well as no status response. Due to lack of time though, this was unable to be fixed in time.

### Possible improvements for future revisions of the project

The most important imporvement is making sure the update function for ability works. Due to time limitations this was not achieved in time for submission but will be edited after this project is submitted.

For future versions of this project, I would like to rework the code to allow the database to initialise the base abilities if they are not present without utilising create-drop for ddl-auto so that monsters and abilities can be stored in the database, and not deleted upon the programme stopping and running again. 

Additionally I would like to implement a feature that allows one to create teams of monsters, being able to designate an order for each one. This would most likely utilise a many to many relationship, though this could be avoided by specifying positions of each monster as seperate fields instead of using a list.

Another feature would to add a user function, where one can differentiate between monsters created by different people, see all the monsters created by one user, and maybe add a type preference to affect interactions within the combat mechanic.

The biggest feature that I would like to add for future versions is the combat mechanic. This mechanic would assign each monster a health value equal to their health statistic, then both monsters would lower the other's value by a modifier of the monster's attack value. When one of the monsters' health value is <1 then the other would win, if both health values are reduced to <1 simultaneously then it would be a draw. This would be where types and abilities would have an impact, as they would alter how the health value is altered by an attack.

I would also like to add a way for users to create new types if they wish, as well as creating a way for some types to be stronger against other types.

The last addition I would presently like to add is a front-end way to interact with the programme to improve how easily an individual may interact with the programme.

[Jira Board](https://evilplan.atlassian.net/jira/software/projects/DP/boards/3)

Screenshots:
Abilities persisted in mysql  
[Persisted abilities](https://ibb.co/D8KvMNT)  

Link table persisted in mysql
[Persisted link table](https://ibb.co/KbyYqjQ)

Monsters persisted in mysql
[Persisted monsters table](https://ibb.co/GWt7KF4)

Test coverage:
[test coverage](https://ibb.co/LJgFct6)

Create mon screenshot:
[create capture](https://ibb.co/Wgy4V4J)
