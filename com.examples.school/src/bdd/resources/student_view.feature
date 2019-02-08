Feature: Student View
  Specifications of the behavior of the Student View

  Scenario: The initial state of the view
    Given The database contains a student with id "1" and name "first student"
    When The Student View is shown
    Then The list contains an element with id "1" and name "first student"
