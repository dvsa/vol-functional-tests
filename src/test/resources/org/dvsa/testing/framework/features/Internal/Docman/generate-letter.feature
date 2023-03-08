@INT
@Generate-Letter
@int_regression
Feature: Generate letter pop up should contain letter details

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to internal
    And i url search for my licence

  @smoketest
 Scenario Outline: Check generate letter pop up and email/print letter
    When i generate a letter
    And The pop up should contain letter details
    Then The letter is sent by "<sendOption>"

    Examples:
      | sendOption   |
      | email        |
#      | printAndPost |

  Scenario: Check Propose to revoke letter process
    When i generate a letter of Subcategory In Office Revocation
    And i save the letter clicking the Propose To Revoke button
    Then all copies of the letter have been saved
