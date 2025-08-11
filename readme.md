# Person Records Pattern Matching Service

A Spring Boot service that receives person records and determines if any stored person satisfies a specific family pattern.

## Pattern Requirements
1. Has a partner
2. Has exactly 3 children and all 3 have that same partner listed as mother or father
3. At least one of those children is under 18

## API Endpoints
- `POST /api/v1/people` - Add or update a person record
    - Returns HTTP 200 if pattern is satisfied
    - Returns HTTP 444 if pattern is not satisfied

## Key Features
- **Bidirectional Integrity**: Automatically maintains consistent relationships
- **Real-time Pattern Checking**: Evaluates pattern after each update
- **Thread-safe**: Uses ConcurrentHashMap for concurrent access
- **Comprehensive Testing**: Unit and integration tests included

## Running the Application
```bash
mvn spring-boot:run
```

## Running test
```bash
mvn test
```

## Project Structure
- `PersonController` - REST API endpoints
- `PersonService` - Business logic and pattern matching
- `PersonRepository` - Stores Persons
- `Person` - Domain model
- Comprehensive test suites for both unit and integration testing

## Performance Considerations
- In-memory storage with ConcurrentHashMap for thread safety

## If I head 10 more hours
I would add:
- Build pipeline
- Add tracing
- Add monitoring
- Add logs
- Add persistence outside off the spring application either a graph or sql database
- Smarter logic for maintaining relationships
- Some more cleanup
- Better integration tests