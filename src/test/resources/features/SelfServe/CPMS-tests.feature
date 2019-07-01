@CPMS-tests
Feature: CPMS client processes payments correctly

  Background:
    Given i have a valid "goods" "sn" licence

#  Scenario: Create a variation and decrease vehicle count
#    When a selfserve user finishes a variation for decreasing the vehicle authority count
#    And i sign the declaration on the review and declarations page
#    And I grant licence
#    Then i check on internal that the payment has processed

  Scenario: Create a variation and increase authorisation count
    And a selfserve user creates a variation and increases the vehicle authority count
    And i pay for my application
    And i create admin and url search for my variation
    Then the "Variation Fee for application" fee should be paid


  Scenario: Create a variation and add operating centre
    And a selfserve user creates a variation and adds an operating centre

