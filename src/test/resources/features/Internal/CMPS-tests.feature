@CMPS-tests
Feature: CMPS client processes payments correctly

  Background:
    Given i have a valid "goods" "sn" licence

  Scenario: Create a variation and decrease vehicle count
    When a selfserve user finishes a variation for decreasing the vehicle authority count
    And i sign the declaration on the review and declarations page
    And I grant licence
    Then i check on internal that the payment has processed

