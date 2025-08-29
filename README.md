# 🚂🚂 AnorakAPI 🚂🚂
This repository is an implementation for a train API according to the below specification.
It uses Spring Boot, Gradle, and a Firebase data repository to create a API instance.

## GET /train
Returns all current trains in the data repository with their ids, names, colours, and train numbers.

## GET /train/{id}
Returns the specific data of the chosen train id.

## GET /train/{id}/sightings
Returns a list of all sightings of that specific train, with station and timestamp metadata.

## GET /station
Returns all current stations in the data repository with their ids and names.

## POST /sightings
When posting a valid list of sightings in a format such as:
```
[
  {
    "train": { "name": "Express 1", "colour": "Red", "trainNumber": "12345" },
    "station": { "name": "London Euston" },
    "timestamp": "2025-08-25T17:35:42Z"
  },
  {
    "train": { "name": "Express 2", "colour": "Blue", "trainNumber": "67890" },
    "station": { "name": "King's Cross" },
    "timestamp": "2025-08-25T18:00:00Z"
  },
  ...
]
```
All information about the train, station, and the current sighting will be recorded onto the firebase data repository if it does not already exist. Existing stations and trains will be name-matched. It will not accept incorrect formatting.

## API Spec

```yaml
openapi: '3.0.0'
info:
  version: 1.0.0
  title: Anorak API

paths:
  /train:
    get:
      summary: Get a list of trains
      operationId: listTrains
      responses:
        '200':
          description: List of all the trains
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Trains'
  /train/{id}:
    get:
      summary: Get a train
      operationId: getTrain
      responses:
        '200':
          description: Train data
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Train'
        '404':
          description: Train not found
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'
  /train/{id}/sightings:
    get:
      description: Get all the sightings of a train
      operationId: Get sightings
      responses:
        '200':
          description: List of sightings
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Sightings'

  /sightings:
    post:
      summary: Save sightings
      description: >
        We will receive a list of 1 or more sightings from the day. 

        Each one will contain a train, a station and time.

        The key logic is:

        1. If the train does not had an id, search the database for the train number. If the train does not exist, add it.
        2. If the station does not exist, add it. (note: All UK stations have unique names)
        3. Save the sighting

        If there is a schema validation error we should return an error without saving anything.
        If there is an error in some of the records, we should save what we can and return a 500 error with a list of the errors

      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Sightings'
      responses:
        '201':
          description: Sightings with all the ids
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Sightings'
        '400':
          description: Schema validation
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'
        '500':
          description: Insert error
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'

components:
  schemas:
    Train:
      type: object
      required:
        - name
        - colour
        - trainNumber
      properties:
        id:
          type: string
          description: Auto generated ID
          example: FSE34-fSFes2
        name:
          type: string
          description: Name of the train
          example: Thomas
          minLength: 2
          maxLength: 100
        colour:
          type: string
          description: Train colour
          example: Blue
        trainNumber:
          type: string
          description: Unique number of the train
          example: T1192A
    Trains:
      type: array
      items:
        $ref: '#/components/schemas/Train'
    Station:
      type: object
      required:
        - name
      properties:
        id:
          type: string
          description: Auto generated ID
        name:
          type: string
          description: name of the station
          example: Liverpool Street
    Sighting:
      type: object
      required:
        - station
        - train
        - timestamp
      properties:
        id:
          type: string
          description: Auto-generated ID
        station:
          $ref: '#/components/schemas/Station'
        train:
          $ref: '#/components/schemas/Train'
        timestamp:
          type: string
          description: ISO formatted DateTime
    Sightings:
      type: array
      items:
        $ref: '#/components/schemas/Sighting'
    Error:
      type: object
      required:
        - code
        - message
      properties:
        code:
          type: string
          example: E001
        message:
          type: string
          example: Train name cannot be empty
        errors:
          type: array
```



