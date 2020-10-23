#@INT
@Docman
  
Feature: All docman functionality tests

  Scenario: Check link is being generated for publication
    Given i have logged in to internal
    And i navigate to the admin publications page
    When i generate "17" publications and check their docman link