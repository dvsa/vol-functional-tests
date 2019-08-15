@ss_regression

Feature: Self Serve users can make changes to their valid licence.

  Background:
    Given i have a valid "goods" "sn" licence

  Scenario: A serve serve user changes their business details
    And i have logged in to self serve
    When i make changes to the business details page
    Then the changes to the business details page are made

