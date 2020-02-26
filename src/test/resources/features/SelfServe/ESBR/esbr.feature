@SS
@ESBR
@ss_regression
Feature: ESBR for English, Welsh and Scottish Areas

  @short
  Scenario Outline: Short notice ESBR in self-serve
    Given I have a psv application with traffic area "<TrafficArea>" and enforcement area "<EnforcementArea>" which has been granted
    When I upload an esbr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    Examples:
      | TrafficArea | EnforcementArea | Days |
      | B           | EA-B            | 41   |
      | M           | EA-A            | 41   |
      | G           | EA-E            | 55   |
      | F           | EA-F            | 41   |
      | H           | EA-H            | 41   |

  Scenario Outline: ESBR in self-serve
    Given I have a psv application with traffic area "<TrafficArea>" and enforcement area "<EnforcementArea>" which has been granted
    When I upload an esbr file with "<Days>" days notice
    Then A short notice flag should not be displayed in selfserve
    Examples:
      | TrafficArea | EnforcementArea | Days |
      | B           | EA-B            | 42   |
      | M           | EA-A            | 42   |
      | G           | EA-E            | 56   |
      | F           | EA-F            | 42   |
      | H           | EA-H            | 42   |

  Scenario Outline: ESBR for curtailed and suspended licence in self-serve
    Given I have a psv application with traffic area "<TrafficArea>" and enforcement area "<EnforcementArea>" which has been granted
    And the licence status is "<LicenceStatus>"
    When I upload an esbr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    Examples:
      | TrafficArea | EnforcementArea | Days | LicenceStatus |
      | B           | EA-B            | 41   | curtail       |
      | G           | EA-E            | 55   | suspend       |