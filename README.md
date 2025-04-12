# Metro Service - Metro Transportation System

The Metro Service is a comprehensive system that manages three core aspects of metro operations: station management, fare calculation, and passenger journey tracking. The Station Service handles CRUD operations for metro stations, ensuring unique station names and maintaining referential integrity with other services. The Fare Service establishes and manages distance-based pricing rules between station pairs, including activation/deactivation of routes and distance queries. The Timing Service tracks passenger journeys through check-in/check-out timestamps, enforces penalty rules for overstays (>90 minutes), and integrates with external User and Payment services to validate travel IDs and process penalties. Together, these services form a complete metro management ecosystem that coordinates physical infrastructure (stations), financial transactions (fares), and passenger movement analytics (timings), with built-in validation, error handling, and service-to-service communication for operational consistency.

## Contributors

* [@Sonupatel](https://github.com/Sonupatel15)
* [@Harsha](https://github.com/harsha188-codes)

## DB Schema

**Metro Station Table**

![metro stations table](https://github.com/user-attachments/assets/d4b4f9f8-be0c-47fc-8fc6-71d00a233be9)

**Station Manager Table**

![station managers table](https://github.com/user-attachments/assets/bf67481a-988c-46b5-9d16-e37bca6d331f)

**Fare Chart Table**

![fare chart table](https://github.com/user-attachments/assets/3acc00dd-e51f-48dd-b73e-8fccecd5f17e)

**Timing Table**

![timing table](https://github.com/user-attachments/assets/353e3cb3-4da7-4fac-b635-0c6d9472cf7b)

## Station Management

### Create New Metro Station

* **Method:** POST
* **Endpoint:** /api/metro-stations
* **Description:** Adds a new metro station
* **Body:**

    ```json
    {
      "stationName": "Rajajinagar"
    }
    ```

* **Response:**

    ![Create New Metro Station Response](https://github.com/user-attachments/assets/57ec0003-667d-437a-aab3-6600bce9ea65)

### List All Metro Stations

* **Method:** GET
* **Endpoint:** /api/metro-stations
* **Description:** Returns all metro stations
* **Response:**

    ![List All Metro Stations Response](https://github.com/user-attachments/assets/a9e4fb0c-97ce-4cd1-8791-ac51c9472ef1)

### Get Metro Station Details

* **Method:** GET
* **Endpoint:** /api/metro-stations/{id}
* **Description:** Retrieves details of a specific metro station
* **Response:**

    ![Get Metro Station Details Response](https://github.com/user-attachments/assets/b6968240-12a6-4bdf-aec9-edd0730e8fb0)

### Update Metro Station

* **Method:** PUT
* **Endpoint:** /api/metro-stations/{id}
* **Description:** Updates the name of an existing metro station
* **Body:**

    ![Update Metro Station Body](https://github.com/user-attachments/assets/c6ee1337-e70c-4223-b9c0-dbff053d4c71)

### Delete Metro Station

* **Method:** DELETE
* **Endpoint:** /api/metro-stations/{id}
* **Description:** Removes a metro station
* **Response:**

    ![Delete Metro Station Response](https://github.com/user-attachments/assets/ae509d55-e4dc-44ec-b43e-da3e643d9497)

## Fare Chart Management

### Add Fare Entry

* **Method:** POST
* **Endpoint:** /api/fare-chart
* **Description:** Creates a new fare/distance entry between two stations
* **Body:**

    ```json
    {
      "fromStationId": 11,
      "toStationId": 2,
      "distance": 12,
      "isActive": true
    }
    ```

* **Response:**

    ![Add Fare Entry Response](https://github.com/user-attachments/assets/947f1653-bf72-4340-b186-f281b26f3c6f)

### Get Distance Between Stations

* **Method:** GET
* **Endpoint:** /api/fare-chart/distance?fromStationId={fromStationId}&toStationId={toStationId}
* **Description:** Returns the distance between two active stations
* **Response:**

    ![Get Distance Between Stations Response](https://github.com/user-attachments/assets/8a31d771-54ff-419d-9064-9e659fc1092f)

### Update Fare Status

* **Method:** PUT
* **Endpoint:** /api/fare-chart/update-status
* **Description:** Activates/deactivates a fare route
* **Body:**

    ```json
    {
      "fromStationId": 11,
      "toStationId": 2,
      "isActive": false
    }
    ```

* **Response:**

    ![Update Fare Status Response](https://github.com/user-attachments/assets/cde77b38-43bb-4b6e-a2ae-dcb7bd86be4e)

## Journey Timing Management

### Check In

* **Method:** POST
* **Endpoint:** /api/timings/checkin
* **Description:** Records passenger entry at a station
* **Body:**

    ```json
    {
      "travelId": "ef9ff112-d701-4940-babc-5c3c0ad432d1"
    }
    ```

* **Response:**

    ![Check In Response](https://github.com/user-attachments/assets/4943b21f-125e-4b2e-a677-f9cf8f370f61)

### Check Out

* **Method:** POST
* **Endpoint:** /api/timings/checkout
* **Description:** Records passenger exit and calculates journey duration
* **Body:**

    ```json
    {
      "travelId": "ef9ff112-d701-4940-babc-5c3c0ad432d1"
    }
    ```

* **Response:**

    ![Check Out Response](https://github.com/user-attachments/assets/14688228-45f4-430c-b157-77d77b33448a)

### Get Timing Details

* **Method:** GET
* **Endpoint:** /api/timings/{timingId}
* **Description:** Retrieves complete timing record
* **Response:**

    ![Get Timing Details Response](https://github.com/user-attachments/assets/db078ac9-2950-4c17-ac74-9b8b65dbfac0)
