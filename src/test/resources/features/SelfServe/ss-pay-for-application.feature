@SS
@ss-pay-app
Feature: Self Serve Apply for goods licence

  Scenario Outline: Create and pay application fees
    Given i have a "<operatorType>" "<licenceType>" application in traffic area
      | b |
    And i choose to print and sign
    When i pay for my application
    Then the application should be submitted

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |
      | public       | standard_national      |

  Scenario Outline: Saved card payment
    Given i have a "<operatorType>" "<licenceType>" application in traffic area
      | b |
      | c |
    And i choose to print and sign
    When i pay for my application
    Then the application should be submitted
    And i choose to pay my second application with my saved card details
    Then the application should be submitted

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |
      | public       | standard_national      |