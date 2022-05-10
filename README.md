# settlers-deckbuilder-back-end
back-end for imperial settlers deck builder app

It consists of three main controllers with separate URLs:

- UserController (.../api/user/) handles users related actions I.e. log ins registering new accounts and deleteing(deactivating) active accounts
- DeckController (.../api/deck/) handles deck related actions I.e CRUD operations and privacy settings on each edck. also handles adding/deleting cards from/to decks
- CardController (.../api/card/) handles card related actions I.i CRUD operations on cards with exception of those given above

Security is based on JWT tokens
List of endpoints is available after building project at swagger default local URL (http://localhost:8080/swagger-ui.html#)

Used Java Spring with lombok for easier data managment.
Data storage is handled by postgreSQL 12.2.

Unit testing was done with use of Junit 5 and Mockito.
