Feature: End to End

  Background: User generated token
    Given I am an authorized user

  Scenario: Demo
    Given A list of books are available
    When I add a book to my reading list
    Then The book is added
    And I remove a book from my reading list
    Then The book is removed