@EBSR
Feature: import EBSR for English, Welsh and Scottish Areas

  @ss_regression @FullRegression @printAndSign
  Scenario Outline: Short notice import EBSR in self-serve
    Given I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
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

  @ss_regression @FullRegression @printAndSign
  Scenario Outline: import EBSR in self-serve
    Given I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should not be displayed in selfserve
    And Documents are generated
    Examples:
      | Area       | Days |
      | north_east | 42   |
      | scotland   | 42   |
      | wales      | 56   |
      | east       | 42   |
      | west       | 42   |

  @ss_regression @FullRegression @printAndSign
  Scenario Outline: import EBSR for curtailed and suspended licence in self-serve
    Given I have a psv application with traffic area "<Area>" and enforcement area "<Area>" which has been granted
    And the licence status is "<LicenceStatus>"
    When I upload an ebsr file with "<Days>" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
    Examples:
      | Area       | Days | LicenceStatus |
      | north_east | 41   | curtail       |
      | wales      | 55   | suspend       |

  @smoketest
  @reads-and-writes-system-properties
  Scenario: Short notice import EBSR in self-serve smoke test
    Given I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated

  @ebsrsmoketest @localsmoke
  @reads-system-properties
  Scenario: import EBSR in self-serve smoke test
    Given I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "42" days notice
    Then A short notice flag should not be displayed in selfserve
    And Documents are generated

  @smoketest @reads-system-properties
  Scenario: import EBSR for curtailed and suspended licence in self-serve smoke test
    Given I have a psv application with traffic area "north_east" and enforcement area "north_east" which has been granted
    And the licence status is "curtail"
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated

#  The upload will be Successful but it's only from accessing the bus registration or checking the created task that you can see if the files were generated.