@lgv
Feature: Operating Centre and Authorisations page checks and validations

  Background:
    Given I have a "goods" "standard_international" application
    And i have logged in to self serve
    And i navigate to the application operating centres and authorisations page

  Scenario: UK Community Authorisations don't exceed HGV and LGV Authorisations combined
    When i enter a combined hgv and lgv authorisation and a higher community authorisation
    Then the community authorisation exceeding lgv authorisation error appears

