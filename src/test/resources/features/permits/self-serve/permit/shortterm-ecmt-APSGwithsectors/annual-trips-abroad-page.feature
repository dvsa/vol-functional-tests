@Deprecated
Feature: Short term ECMT APSG with sectors Annual Trips Abroad Page

  Background:
    Given I have a "goods" "standard_international" licence
   And   I am on the VOL self-serve site

  @OLCS-25906 @OLCS-28226
  Scenario: Annual Trips Abroad Page details are displayed correctly
    When  I am on short term ECMT annual trips abroad page
    And   the page heading on short term ECMT Annual Trips Abroad page is displayed correctly
    And   the warning message on short term ECMT Annual Trips Abroad Page is displayed correctly
    And   the reference number on Short term ECMT Annual Trips Abroad Page is displayed correctly
    When  I select help calculating your international trips
    Then  I should see the summary text
    When  I select the help calculating international trips again
    Then  I should not see the summary text
    When  I select save and continue without entering any value
    Then  I should see the error message
    When  I select save and return to overview without entering any value
    Then  I should see the error message
    When  I specify a valid input in short term ECMT annual trips abroad page
    And   I save and continue
    Then  the user is navigated to the next page

  @OLCS-25906
  Scenario Outline: Fails validation on short term ECMT while the input value is negative
    When  I am on short term ECMT annual trips abroad page
    And   I specify an invalid <value> of annual trips in short term 2020
    When  I save and continue
    Then  I should get the specific validation message for invalid value
    When  I specify a valid input in short term ECMT annual trips abroad page
    And   I select save and return overview link on annual trips abroad page
    Then  the user is navigated to the short term ECMT overview page with the status as completed

    Examples:
      |value  |
      |-1     |
      |eeee   |

  @OLCS-25906
  Scenario Outline: Fails validation on short term ECMT  while the input value is more than 6 digits
    When  I am on short term ECMT annual trips abroad page
    And  I specify more than 6 digit <value> of annual trips in short term ECMT
    When I save and continue
    Then I should get the specific validation message for invalid input
    And  I go back
    Then I should be taken to the permits dashboard

    Examples:
      |value      |
      |1000000    |
      |9999999    |