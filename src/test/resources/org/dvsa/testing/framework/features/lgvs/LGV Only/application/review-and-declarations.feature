@lgv
Feature: Review and Declarations page text is modified for lgv only applications but not for non-LGV Only

  Scenario: LGV application declarations page displays the modified declarations as required
    Given I have a "GB" lgv only application
    When i navigate to the application review and declarations page
    Then the review and declaration page should display the modified lgv only text


  Scenario: LGV application snapshot displays the modified declarations as required
    Given I have a "GB" lgv only application
    When i navigate to the snapshot on the review and declarations page
    Then the review and declaration page should display the modified lgv only text
    And i close and refocus the tab

  Scenario: Goods SI application declarations page displays the modified declarations as required
    Given I have a "goods" "standard_international" application
    When i navigate to the application review and declarations page
    Then the review and declaration page should display original unmodified declarations text

  Scenario: Goods SI application snapshot displays the modified declarations as required
    Given I have a "goods" "standard_international" application
    When i navigate to the snapshot on the review and declarations page
    Then the review and declaration page should display original unmodified declarations text
    And i close and refocus the tab

  Scenario: Goods SN application declarations page displays the modified declarations as required
    Given I have a "goods" "standard_national" application
    When i navigate to the application review and declarations page
    Then the review and declaration page should display original unmodified declarations text

  Scenario: Goods SN application snapshot displays the modified declarations as required
    Given I have a "goods" "standard_national" application
    When i navigate to the snapshot on the review and declarations page
    Then the review and declaration page should display original unmodified declarations text
    And i close and refocus the tab
