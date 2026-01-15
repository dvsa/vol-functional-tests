@EBSR
Feature: import EBSR for English, Welsh and Scottish Areas

  @FullRegression @printAndSign
  Scenario Outline: Short notice import EBSR in self-serve
    Given as a "admin" I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
    Examples:
      | Area       | Days |
      | north_east | 41   |
      | scotland   | 41   |

  @FullRegression @printAndSign
  Scenario Outline: import EBSR in self-serve
    Given as a "admin" I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should not be displayed in selfserve
    And Documents are generated
    Examples:
      | Area       | Days |
      | wales      | 56   |
      | east       | 42   |
      | west       | 42   |

  @ss_regression @FullRegression @printAndSign
  Scenario: import EBSR for consultant
    Given as a "consultant" I have a psv application with traffic area "wales" and enforcement area "wales" which has been granted
    When I upload an ebsr file with "55" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated

  Scenario: Short notice import EBSR in self-serve smoke test
    Given as a "admin" I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated

  @ebsrsmoketest @localsmoke @ss_regression
  Scenario: import EBSR in self-serve smoke test
    Given as a "admin" I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "42" days notice
    Then A short notice flag should not be displayed in selfserve
    And Documents are generated

  Scenario: import EBSR for curtailed and suspended licence in self-serve smoke test
    Given as a "admin" I have a psv application with traffic area "north_east" and enforcement area "north_east" which has been granted
    And the licence status is "curtail"
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated

  @localAuthorityUser
  Scenario: Local Authority User Checks Bus Registrations
    When I log in as a Local Authority User
    Then I should be able to view to bus registration details