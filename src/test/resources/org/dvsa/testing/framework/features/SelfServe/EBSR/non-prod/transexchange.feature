Feature: Validate TransXchange functionalities.
  Description: The purpose of these tests to cover Happy and unhappy flows for TransXchange

  TransXchange pdf request URL: https://649un4tb5j.execute-api.eu-west-1.amazonaws.com/non-prod/pdf-request

  @3771_1.TransXchange @positive @Bala
    Scenario: Generate OAuth/Authorisation token, send valid XML request and check response
    Given I generate an OAuth token
    When I send a POST request to end point '<URL>' with the valid XML'<xml>'
     Then the response status code should be 200

  @3771_2.TransXchange @NegativeTest @Bala
  Scenario: Generate OAuth/Authorisation token, send a Invalid XML request and check response as Bad request
    Given I generate an OAuth token
    When I send a POST Invalid request to end point '<URL>' with the valid XML'<xml>'
    Then the bad request response status code should be 400

  @3771_3.TransXchange @NegativeTest @Bala
  Scenario: Send a No Authorised request and check response as UnAuthorised.
    Given I do not generate an OAuth token
    When I send a unauthorised POST request to end point '<URL>' with the any XML'<xml>'
    Then the unauthorised response status code should be 401

  @3771_4.TransXchange @Bala
  Scenario: Send a unsecured request and check response.
    Given I generate an OAuth token
    When I send a unsecured POST request to end point '<URL>' with the any XML'<xml>'
    Then the connection is REFUSED.