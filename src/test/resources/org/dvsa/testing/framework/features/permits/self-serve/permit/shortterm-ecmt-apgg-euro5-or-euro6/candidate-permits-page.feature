@EXTERNAL @shortterm_apgg_euro5_or_euro6
Feature: Short term ECMT APGG Euro 5 or Euro 6 Candidate permit page

  Background:
    Given I have a "goods" "standard_international" licence
    And I have a short term application in awaiting fee status

  @OLCS-25090 @OLCS-27781 @OLCS-28226 @OLCS-28276
  Scenario: Candidate permits page is displayed as per the AC
    Then the user is taken to the awaiting fee page
    When I click the view permit restriction link
    Then the user is taken to the allocated candidate permit view page
    And  the details are displayed as expected
    When I select the return to fee overview link
    Then the user is taken to the awaiting fee page