@SS
@ss-pay-app
@ss_regression
Feature: Self Serve Apply for licence

  @CPMS_tests
  Scenario Outline: Create and pay application fees
    Given i have a "<operatorType>" "<licenceType>" "GB" application in traffic area
      | north_east |
    And i choose to print and sign
    When i pay for my application
    Then the application should be submitted

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |
      | public       | standard_national      |

  @stored_cards
  Scenario Outline: Saved card payment
    Given i have a "<operatorType>" "<licenceType>" "GB" application in traffic area
      | north_east |
      | north_west |
    And i choose to print and sign
    When i pay for my application
    Then the application should be submitted
    And i choose to pay my second application with my saved card details
    Then the application should be submitted

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |

  @NI_application
  Scenario Outline: Create and pay NI application fees
    Given i have a "<operatorType>" "<licenceType>" "NI" application in traffic area
      | northern_ireland |
    And i choose to print and sign
    When i pay for my application
    Then the application should be submitted

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |