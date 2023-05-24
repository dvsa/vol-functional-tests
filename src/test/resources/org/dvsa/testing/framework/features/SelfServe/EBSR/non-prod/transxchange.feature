Feature: Validate TransXchange functionalities.
  Description: The purpose of these tests to cover Happy and unhappy flows for TransXchange

  TransXchange pdf request URL: https://649un4tb5j.execute-api.eu-west-1.amazonaws.com/non-prod/pdf-request

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
