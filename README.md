# Java Programming Trial Task

The DeliverFee Application is designed to calculate delivery fees based on regional base fees, vehicle types, and weather conditions.

## Features

- H2 database integration for storing weather data and regional base fees.
- Configurable scheduled task for importing weather data.
- Calculation of delivery fees with consideration for weather conditions.
- REST API to request and manage delivery fees.

## Application Workflow

The DeliverFee application is designed to calculate and manage delivery fees based on regional base fees, vehicle types, and varying weather conditions. Upon startup, the `DataInitializer` class initializes the database with predefined base fees and extra fee rules. The `DeliveryFeeService` is the centerpiece of business logic, calculating total delivery fees by combining base fees from the `RegionalBaseFeeRepository` with conditional extra fees obtained from the `ExtraFeeRepository`. It integrates current weather data from the `WeatherDataRepository`, which is updated regularly through XML API requests made every hour at fifteen minutes past.

The `FeesController` and `DeliveryFeeRestController` expose RESTful endpoints, enabling clients to perform CRUD operations on base fees and extra fees, as well as to calculate the delivery fee given a city and a vehicle type. The REST controllers make use of the service layer to encapsulate the business logic, providing a clean separation of concerns. The application is structured with a layered architecture, dividing the code into models (entities), repositories (data access), services (business logic), and controllers (HTTP endpoints), aligning with common OOP practices and design patterns for maintainability and scalability.

### Trial Task
- **Documentation**: Public methods, especially in services and controllers, are documented with JavaDoc comments, providing clear descriptions of their purpose, parameters, and return values. Swagger annotations further document the REST API.
- **Error Handling**: The errors are handled with a global exception handling mechanism using `@RestControllerAdvice`.
- **Test Coverage**: Unit tests cover key functionalities, focusing on service layer logic. Mockito is used to mock dependencies, allowing for isolated testing of components. Tests ensure correctness across various scenarios, including edge cases for weather conditions and vehicle types.
- **Bonus**: Delivery fees and extra fees can be managed through REST interface.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

- Java JDK 17
- Maven
  
### Installation

1. Clone the repo
  ```sh
  git clone https://github.com/your_username_/DeliverFee.git
  ```
2. Navigate to the project directory
  ```sh
  cd DeliverFee
  ```
3. Build the project with Maven
  ```sh
  ./mvnw clean install
  ```
4. Run the application
  ```sh
  ./mvnw spring-boot:run
  ```

## REST Interface
#### Delivery Fee Calculation

##### Calculate Delivery Fee
- **GET** `/api/calculateDeliveryFee?city={city}&vehicleType={vehicleType}`
  - Calculates the delivery fee based on the city and vehicle type provided.
  - Required parameters:
    - `city`: Name of the city where the delivery is to be made.
    - `vehicleType`: Type of the vehicle used for delivery.

#### Regional Base Fees Management

##### Create or Update Regional Base Fee
- **POST** `/api/fees/regionalBaseFee`
  - Creates a new regional base fee entry or updates an existing one.
  - Request body must include:
    - `city`: City to which the base fee applies.
    - `vehicleType`: Vehicle type that the fee applies to.
    - `fee`: The amount of the base fee.

##### Get All Regional Base Fees
- **GET** `/api/fees/regionalBaseFee`
  - Retrieves a list of all regional base fees stored in the system.

##### Get Regional Base Fee by City and Vehicle Type
- **GET** `/api/fees/regionalBaseFee/{city}/{vehicleType}`
  - Fetches the regional base fee for a specific city and vehicle type.

##### Delete Regional Base Fee
- **DELETE** `/api/fees/regionalBaseFee/{city}/{vehicleType}`
  - Removes a regional base fee from the system based on city and vehicle type.

#### Extra Fees Management

##### Create Extra Fee
- **POST** `/api/fees/extraFee`
  - Adds a new extra fee rule to the system.
  - Request body must include:
    - `condition`: The condition that triggers the extra fee.
    - `applicableToVehicleType`: Vehicle types the extra fee applies to.
    - `fee`: The amount of the extra fee.

##### Get All Extra Fees
- **GET** `/api/fees/extraFee`
  - Retrieves all extra fee rules from the system.

##### Get Extra Fee by ID
- **GET** `/api/fees/extraFee/{id}`
  - Fetches a specific extra fee by its unique ID.

##### Update Extra Fee
- **PUT** `/api/fees/extraFee/{id}`
  - Updates an existing extra fee rule based on its ID.
  - Request body must include:
    - `condition`: The new condition for the extra fee.
    - `applicableToVehicleType`: The new vehicle type the extra fee applies to.
    - `fee`: The new fee amount.

##### Delete Extra Fee
- **DELETE** `/api/fees/extraFee/{id}`
  - Removes an extra fee rule from the system using its ID.
