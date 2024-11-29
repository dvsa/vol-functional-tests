@EBSR
Feature: import EBSR for English, Welsh and Scottish Areas

  @ss_regression @FullRegression @printAndSign
  Scenario Outline: Short notice import EBSR in self-serve (Resource-A)
    Given as a "admin" I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
    Examples:
      | Area       | Days |
      | north_east | 41   |
      | scotland   | 41   |
      | wales      | 55   |
      | east       | 41   |
      | west       | 41   |

  @ss_regression @FullRegression @printAndSign @consultant
  Scenario Outline: import EBSR in self-serve (Resource-B)
    Given as a "<user_type>" I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should not be displayed in selfserve
    And Documents are generated
    Examples:
      | user_type  | Area       | Days |
      | admin      | north_east | 41   |
      | consultant | scotland   | 41   |
      | admin      | wales      | 55   |
      | consultant | east       | 41   |
      | admin      | west       | 41   |

  @ss_regression @FullRegression @printAndSign @consultant
  Scenario Outline: import EBSR for curtailed and suspended licence in self-serve (Resource-A)
    Given as a "<user_type>" I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    And the licence status is "<LicenceStatus>"
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
    Examples:
      | user_type  | Area       | Days | LicenceStatus |
      | admin      | north_east | 41   | curtail       |
      | consultant | wales      | 55   | suspend       |

  @ec2_smoke
  Scenario: Short notice import EBSR in self-serve smoke test (Resource-B)
    Given as a "admin" I have a psv application with traffic area "west" and enforcement area "west" which has been granted
#    When i trigger the ebsr process queue
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated

  @ebsrsmoketest @localsmoke
  Scenario: import EBSR in self-serve smoke test (Resource-B)
    Given as a "admin" I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "42" days notice
    Then A short notice flag should not be displayed in selfserve
    And Documents are generated

  @ec2_smoke
  Scenario: import EBSR for curtailed and suspended licence in self-serve smoke test (Resource-B)
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