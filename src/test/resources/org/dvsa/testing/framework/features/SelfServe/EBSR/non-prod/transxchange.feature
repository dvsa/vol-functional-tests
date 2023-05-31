Feature: Test happy and unhappy paths in TransXchange functionality.

  # PDF request validator tests
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

  # Bus operator xml validator tests
  @TransXchange
  Scenario Outline: Generate OAuth token, upload invalid operator XML, send valid pdf XML request and check queue for failure response
    Given I generate an OAuth token
    When I upload invalid "<problem>" operator xml into the bucket
    When I send a POST request to the API gateway with valid XML for missing operators
    Then the response status code should be 200
    Then I read a message off the queue and verify it looks right for the "<problem>"
    Examples:
      | problem          |
      | missingOperators |

  @TransXchange
  Scenario: Generate OAuth token, send valid pdf XML request but not don't upload bus operator xml and check queue for failure response
    Given I generate an OAuth token
    When I send a POST request to the API gateway with valid "fileNotFound" XML
    Then the response status code should be 200
    And I read a message off the queue and verify it looks right for the "fileNotFound"

  # PDF generator lambda tests
  # Will be uncommented once the lambdas' are wired up to SNS correctly
#  @TransXchangeOperatorXmlValid
#  Scenario Outline: Generate OAuth token, upload valid operator XML, send valid pdf XML request
#    Given I generate an OAuth token
#    When I upload valid "<type>" operator xml into the bucket
#    When I send a POST request to the API gateway with valid "<type>" XML
#    Then the response status code should be 200
#    Examples:
#      | type      |
#      | timetable |
