Survey Lab - Tasks:
	Create a RESTful Spring Boot application which supports creating Polls and voting in them.
	The application should work with a relational database, preferrably MySQL. Querries should not depend on the underlying database.
	For development and testing purposes Swagger api documentation and UI will be necessary. Swagger should be available only in dev and qa mode.
	
	==================================
	The main entities include: 
	
	User
	id, createdOn (not null), username (unique), password (not null), firstName (not null), lastName (not null), collection of owned polls,
	collection of poll options the user has voted for
	
	Poll 
	id, createdOn (not null), text (not null, max 250 symbols), user (owner), completed flag (default false), multiple choice flag (default false),
	expiry date (default 10 days from createdOn date, not null), collection of poll options (min 2, initial order should be preserved)
	
	PollOption
	id, createdOn (not null), text (not null), number of votes (default 0), parent poll (not null), collection of users who voted for the given option
	Note you can choose between using unidirectional or bidirectional mapping between the entities.
	==================================

	The REST API should expose endpoints for the follwoing functionalities:
	Incoming data should be validated
	
	1. Create/register user
	2. Users should be able to create polls
	3. Retrieve Poll by id
	4. Retrieve all the polls owned by a user
	5. Users should be able to vote in polls for one or more options
	==================================
	
	Database should be crated with an SQL script, compatible with H2
	Use DTOs for receiving and sending data	
	Use custom exceptions to handle exceptional situations
		NotFoundException -> should be mapped to HTTP404
		ValidationException -> should be mapped to HTTP400
	The results of operations should be logged (use different log levels for the differnt types of data that is being logged)
	==================================
	
	Create integration tests for the controller and repository layers. Use in memory database for testing the repository layer.
	Create unit tests for the service layer.
	Tests must be independant.
	Overall code coverage should be minimum 80%

	Use the git flow model -> commits should include working code, each new feature should be implemented in a separate branch,
	pull requests should include only changes needed for implementing the current feature or fixing the current bug.
	
	==================================
	Аdd support for in-memory database with mock data - 2 days
                            - it should be possible to start the application in different modes - with h2 in memory db or with mysql/mariadb without changing the source code
                            - on startup if the schema and tables do not exist they should be created automatically
                            - mock data should be inserted for testing puroposes (h2 only)
                            
    ==================================
    Add Liquibase ~ 2 days
    add liquibase dependencies
                - configure liquibase
                - add create schema scripts
                - add insert mock data scripts only for profiles which use h2 database

	==================================
	Migration to Sping Data – 2 days
                - Replace existing repositories with interfaces which extend JpaRepository
                - update existing queries
                - all controller, service and repository methods which return a collection should accept Pageable as parameter and return Page
                - all service and repository methods which return a single Entity object should return Optional

	==================================
	
	==================================
	
	==================================