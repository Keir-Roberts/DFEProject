# DFEProject
Final project for the DFE software bootcamp run by QA

Overview:

This is an API for creating a catalogue of monsters for a hypothetical game, in which one can create a monster, build a team, then run some sort of combat.

Monsters

Each monster has 5 attributes: Name, Description, Type, Attack, Health and Abilities.

Name and Description are plain text used to distinguish and add flavour to each monster.

Abilities are comprised of an ID, a name and a description. In the hypothetical game they would alter how a monster acts in combat. Abilities are treated as seperate entities which can be created, read, edited and deleted.

Type is a plain text value converted into an enum and determines not only the kind of monster being created but also determines the base attack and base health (the minimum values of attack and health) for each monster, as well as automatically giving the monster an ability.

Attack and Health are both integer values that would be the main aspect of the combat element, when creating a monster, one inputs the value by which they wish to increase that monster's base value (for instance if the monster had a base attack of 2, and input 3 for attack on creation, the resulting monster would have an attack of 5).

When creating a monster, there is a limited number of 'build points' which determines the maximum amount of Attack, health and abilities each monster can have beyond the values given to it by its type. Each point of attack and defense cost 1 build point (unless they are part of that monster's type's base attack or base health)

The default 'maximum build points' is 10, but this can be edited in the "skillPoints" class within the enum folder of the project. 

Additionally by default, each ability beyond what is provided by type costs 3 build points, but this can also be edited within the "skillPoints" class.

Abilities cannot be added simply within the create method, but existing monsters can have abilities added or removed if they do not exceed the maximum number of build points.

Monster commands:

Create: 
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




Get All:
GET
http://localhost:8080/mon/getAll
input body: none

This returns all monsters stored within the database.

Get By ID:
GET
http://localhost:8080/mon/get/{id}
input body: none
Returns a single monster with the id given in the URL

Get Type:
GET
http://localhost:8080/mon/getType/{type}
input body: none
Returns all monsters which belong to the type given in the url. It is not case-sensitive but will throw an error if there is not an existing type that matches the input

Get Name:
GET
http://localhost:8080/mon/getName/{name}
input body: none
Returns all monsters whose names include the input within the url.

Compare:
GET
http://localhost:8080/mon/compare/{Monster ID 1}/{Monster ID 2}
input body: none

returns a plain text comparison of the monsters with the IDs specified in the URL.

Update:
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

updates the monster with the given id with the information provided in the request body. If built is set to true, the information will be stay as provided. If built is false, the monster will automatically be 'built' by increasing the monster's attack and defense in accordance to the monster's types' base attack and health. Additionally any innate abilities will be added.

Change stats:
PATCH
http://localhost:8080/mon/change{Stat}/{amount}
input body: none

by replacing {stat} with either Attack or Health, one can change the amount of health or attack a monster has by an amount specified in the url. By inputting a positive number, you can increase the value, and by inputting a negative number you can decrease it. You cannot increase health or attack beyond what the maximum build points allows, nor can you reduce that monster's attack or health to lower than that monster's base attack or base health.

Add ability:
POST
http://localhost:8080/mon/addAbility/{id}/{Ability name}
input body: none

Adds the ability that matches the inputted "Ability name" to the monster with the specified ID. You cannot add abilities to a monster if it already has that ability.

Remove Ability:
DELETE
http://localhost:8080/mon/removeAbility/{id}/{Ability name}
input body: none

Removes the ability that matches the inputted "Ability name" from the monster with the specified ID. You can only remove abilities that the monster already has, and you cannot remove abilities provided by the monster's type.

Delete:
DELETE
http://localhost:8080/mon/delete/{id}

Deletes the monster with the given ID.



Development Log:

Started off with the idea of a 'Pokedex' like store of different monsters that allows a user to create, read, update, delete, as well as compare and fight them with other monsters.

The original idea was to have 10 different 'types' of monster which affects how they act in battle as well as some aspects of creation.
This was going to be achieved via subclasses with a super of 'monster' though this proved unfeasible due to how springboot would interact with the classes.

Instead by using an enum of type, the same effects are hoped to be achieved in the backend, without disrupting interactions with the API or database.