@publications @FullRegression @printAndSign
Feature: Generate publications in internal for all licence types

  Scenario: Generate and publish Publications for different licence types
    Given i have an internal admin user
    When i have logged in to internal as "admin"
    And i navigate to the admin publications page
    And i generate and publish all 17 publications

    #Needs more investigating on how we can publish as part of the regression