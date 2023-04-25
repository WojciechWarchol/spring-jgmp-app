Feature: Event with id can be retrieved
  Scenario: client makes a call to GET /api/events/byId and gets an Event
    When the client calls /api/events/byId with id 1
    Then the client should receive status code 200
    And the client should receive event with id 1 and title "Music Event"

  Scenario: client makes a call to GET /api/events/byId and there is no Event
    When the client calls /api/events/byId with id 10
    Then the client should receive status code 200
    And the client should receive an empty list

  Scenario: client makes a call to GET /api/events/byTitle and gets a list of Events
    When the client calls /api/events/byTitle with string "Event"
    Then the client should receive status code 200
    And the client should receive a list with 3 elements
    And the client should receive event with id 1 and title "Music Event"
    And the client should receive event with id 2 and title "IT Event"
    And the client should receive event with id 3 and title "Culinary Event"

  Scenario: client makes a call to GET /api/events/byTitle and gets an empty list
    When the client calls /api/events/byTitle with string "ugabuga"
    Then the client should receive status code 200
    And the client should receive an empty list
