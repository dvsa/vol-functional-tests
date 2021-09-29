@INT @int_regression @publications
Feature: Generate publications in internal for all licence types

  Scenario: Generate and publish Publications for different licence types
    When i navigate to the admin publications page
    Then i generate and publish all "17" publications