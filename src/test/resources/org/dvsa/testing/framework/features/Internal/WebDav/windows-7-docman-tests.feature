@Docman
  
Feature: All docman functionality tests

  Scenario: Check link is being generated for publication
    Given i have an internal admin user
    And i have logged in to internal as "admin"
    And i navigate to the admin publications page
    When i generate 17 publications and check their docman link