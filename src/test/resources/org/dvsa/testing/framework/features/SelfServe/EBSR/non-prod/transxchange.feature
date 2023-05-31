Feature: Test happy and unhappy paths in TransXchange functionality.

  @TransXchange
  Scenario: Generate OAuth/Authorisation token, send valid XML request and check response
    Given I generate an OAuth token
    When I send a POST request to the API gateway with valid XML
    Then the response status code should be 200

  @TransXchange
  Scenario: Generate OAuth/Authorisation token, send a Invalid XML request and check response as Bad request
    Given I generate an OAuth token
    When I send a POST request to the API gateway with invalid XML
    Then the response status code should be 400

  @TransXchange
  Scenario: Send an Unauthorised request and check response as Unauthorised.
    Given I do not generate an OAuth token
    When I send an unauthorised POST request to the API gateway with any XML
    Then the response status code should be 401

  @TransXchangeOperatorXmlValid
  Scenario Outline: Generate OAuth token, upload valid operator XML, send valid pdf XML request and check queue for failure response
    Given I generate an OAuth token
    When I upload valid "<type>" operator xml into the bucket
    When I send a POST request to the API gateway with valid "<type>" XML
    Then the response status code should be 200
    Examples:
      | type      |
      | timetable |

  @TransXchangeOperatorXmlInvalid
  Scenario Outline: Generate OAuth token, upload invalid operator XML, send valid pdf XML request and check queue for failure response
    Given I generate an OAuth token
    When I upload invalid "<problem>" operator xml into the bucket
    When I send a POST request to the API gateway with valid XML for missing operators
    Then the response status code should be 200
#    Then I read a message off the queue and verify it looks right for the "<problem>"
    Examples:
      | problem          |
      | missingOperators |
