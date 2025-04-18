@EBSR
Feature: import EBSR for English, Welsh and Scottish Areas

  @ss_regression @FullRegression @printAndSign
  Scenario Outline: Short notice import EBSR in self-serve
    Given as a "admin" I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
    Examples:
      | Area       | Days |
      | north_east | 41   |
      | scotland   | 41   |

  @ss_regression @FullRegression @printAndSign
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

  @ebsrsmoketest @localsmoke
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

#  The upload will be Successful but it's only from accessing the bus registration or checking the created task that you can see if the files were generated.
  @accessibility
  Scenario: Scan for accessibility violations
    Given as a "admin" I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
    Then no issues should be present on the page

  @localAuthorityUser
  Scenario: Local Authority User Checks Bus Registrations
    When I log in as a Local Authority User
    Then I should be able to view to bus registration details
    And I should also be able to view EBSR uploads