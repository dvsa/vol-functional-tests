@SS-TM-Verify
@SS
@OLCS-21298
@ss_regression
Feature: TM/operator checks optional wording has been removed for TM details page

  Background:
    Given I have a "goods" "standard_national" application
    And i navigate to the admin transport managers details page

  Scenario: Radio button not clicked
    Then the optional wording should not be displayed on the page
      | Other Licences       |
      | Other Employments    |
      | previous Convictions |
      | previous Licences    |
    And the section buttons should not be displayed
      | Add other licences            |
      | Add other employment          |
      | Add convictions and penalties |
      | Add licences                  |

  Scenario: Radio button clicked
    When I select yes to all radio buttons
    Then the section buttons should be displayed
      | Add other licences            |
      | Add other employment          |
      | Add convictions and penalties |
      | Add licences                  |

  Scenario Outline: Check section contains
    When I click on the "<button>" button
    Then I should see the "<page>" page
    And page title "<page-title>" should be displayed on page

    Examples:
      | page                           | button                        | page-title                                              |
#      | add-other-licence-applications | Add other licences            | Add other licence details                               |
#      | add-employment                 | Add other employment          | Add other employment details                            |
#      | add-previous-conviction        | Add convictions and penalties | Add Offence                                             |
      | add-previous-licence           | Add licences                  | Add Details of revoked, curtailed or suspended licences |

  Scenario: validation checks when no radio button has been selected
    When the users attempts to save without entering any data
    Then a validation message should be displayed

  Scenario: validation checks on guidance message
    When I click the no radio button for the "owner/director" question
    Then the guidance text should be displayed