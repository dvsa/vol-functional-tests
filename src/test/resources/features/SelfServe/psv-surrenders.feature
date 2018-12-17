@OLCS-21939
Feature:Digital Surrender - PSV Surrender licence

  Background:
    Given i have a valid "public" licence
    And i choose to surrender my licence
    When i am on the review contact details page

  Scenario: PSV review details page
    Then the correct licence details should be displayed
    And the correct correspondence details should be displayed
    And the correct contact details should be displayed

  Scenario: Update correspondence details
    And i update my correspondence address
    Then the new correspondence details should be displayed on the review page

  Scenario: Check current discs
    And discs have been added to my licence
    And i navigate to the current discs page
    Then the number of disc should match the vehicles registered on the licence
