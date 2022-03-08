# DFEProject
Final project for the DFE software bootcamp run by QA

Development Log:

Started off with the idea of a 'Pokedex' like store of different monsters that allows a user to create, read, update, delete, as well as compare and fight them with other monsters.

The original idea was to have 10 different 'types' of monster which affects how they act in battle as well as some aspects of creation.
This was going to be achieved via subclasses with a super of 'monster' though this proved unfeasible due to how springboot would interact with the classes.

Instead by using an enum of type, the same effects are hoped to be achieved in the backend, without disrupting interactions with the API or database.