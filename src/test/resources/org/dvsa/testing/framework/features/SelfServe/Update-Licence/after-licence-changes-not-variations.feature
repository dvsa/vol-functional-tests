@SS-changes-not-variation
@ss_regression
@FullRegression
@printAndSign

Feature: Self Serve users can make changes to their valid licence.

  Background:
    Given i have a valid "public" "standard_national" licence

  @reads-and-writes-system-properties
  Scenario: A self serve user changes their business details
    When i make changes to the business details page
    Then the changes to the business details page are made

  @reads-system-properties
  Scenario: A self serve user changes their addresses
    When i make changes to the addresses page
    Then the changes to the addresses page are made

  @reads-system-properties
  Scenario: A self serve user changes their vehicles
    When i make changes to the vehicles page
    Then the changes to the vehicles page are made

  @reads-system-properties
  Scenario: A self serve user changes their licence discs (public only)
    When i make changes to the licence discs page
    Then the changes to the licence discs page are made

  @reads-system-properties
  Scenario: A self serve user changes their safety and compliance
    When i make changes to the safety and compliance page
    Then the changes to the safety and compliance page are made