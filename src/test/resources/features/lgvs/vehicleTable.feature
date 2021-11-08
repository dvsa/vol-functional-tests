@lgv
Feature: Authority page checks

  Scenario: Table displays the right number of lgvs and hgvs
    Given I have "2" "goods" "standard_international" licences with "3" HGVs and "2" LGVs with a vehicleAuthorities of "5" and "5"
    And I navigate to manage vehicle page on a licence