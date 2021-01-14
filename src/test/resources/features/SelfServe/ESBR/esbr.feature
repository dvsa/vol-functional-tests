@SS
@ESBR
@ss_regression
Feature: ESBR for English, Welsh and Scottish Areas

  @short
  Scenario Outline: Short notice ESBR in self-serve
    Given I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    Examples:
      | Area       | Days |
      | north_east | 41   |
      | scotland   | 41   |
      | wales      | 55   |
      | east       | 41   |
      | west       | 41   |

  Scenario Outline: ESBR in self-serve
    Given I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should not be displayed in selfserve
    Examples:
      | Area | Days |
      | north_east | 42   |
      | scotland   | 42   |
      | wales      | 56   |
      | east       | 42   |
      | west       | 42   |

  Scenario Outline: ESBR for curtailed and suspended licence in self-serve
    Given I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    And the licence status is "<LicenceStatus>"
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    Examples:
      | Area        | Days | LicenceStatus |
      | north_east  | 41   | curtail       |
      | wales       | 55   | suspend       |