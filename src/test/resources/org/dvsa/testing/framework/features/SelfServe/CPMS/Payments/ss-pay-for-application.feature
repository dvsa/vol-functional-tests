@ss-pay-app
@ss_regression @FullRegression
Feature: Self Serve Apply for licence

  @application_fees @printAndSign
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

  @stored_cards @printAndSign @localsmoke @flaky
#  Scenario Outline: Saved card payment
#    Given i have a "<operatorType>" "<licenceType>" "GB" application in traffic area
#      | north_west |
#      | north_east |
#    And i choose to print and sign
#    When i pay for my application
#    Then the application should be submitted
#    And i pay my second application with my saved card details
#    Then the application should be submitted

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |

   @NI_application @smoke @smoke
   Scenario Outline: Create and pay NI application fees
    Given i have a "<operatorType>" "<licenceType>" "NI" application in traffic area
      | northern_ireland |
    And i choose to print and sign
    When i pay for my application
    Then the application should be submitted

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |