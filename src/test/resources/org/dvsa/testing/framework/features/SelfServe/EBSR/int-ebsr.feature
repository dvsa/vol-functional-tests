@INT-SMOKE
Feature: import EBSR for English, Welsh and Scottish Areas

  Background:
    Given i have a self serve account

  Scenario: Short notice import EBSR in self-serve smoke test
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
