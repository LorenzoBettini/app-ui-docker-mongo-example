Feature: Student View
  Specifications of the behavior of the Student View

  Scenario: The initial state of the view
    Given The database contains the students with the following values
      | 1 | first student  |
      | 2 | second student |
    When The Student View is shown
    Then The list contains elements with the following values
      | 1 | first student  |
      | 2 | second student |
