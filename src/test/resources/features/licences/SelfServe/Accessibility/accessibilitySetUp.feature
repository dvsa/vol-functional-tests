@SS
@user
Feature: Set up users for accessibility testing

  @multiple_licences
  Scenario Outline: PSV and Goods account with 16 different licences and 3 vehicles
    Given I have all "<OperatorType>" "<LicenceType>" traffic area licences
    When i have logged in to self serve
    Then i write the licence login information to a file for use of user research

    Examples:
      | OperatorType | LicenceType            |
      | goods        | standard_national      |
      | public       | standard_international |

  @tm_accounts
  Scenario Outline: PSV and Goods  account with 16 different licences with external tm (requires api fix)
    Given I have all "<OperatorType>" "<LicenceType>" Traffic Areas applications with an external TM
    Then accounts should be created

    Examples:
      | OperatorType | LicenceType            |
      | goods        | standard_national      |
      | public       | standard_international |