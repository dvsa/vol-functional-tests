@SS
@user
Feature: Set up users for accessibility testing

  @multiple_licences
  Scenario Outline: PSV account with 16 different licences and 3 vehicles
    Given I have all "OperatorType" "LicenceType" traffic area licences
    When i have logged in to self serve
    Then the licence should be created and granted

    Examples:
      | OperatorType | LicenceType            |
      | goods        | standard_national      |
      | public       | standard_international |

  @tm_accounts
  Scenario Outline: PSV account with 16 different licences with external tm
    Given I have all "<OperatorType>" "<LicenceType>" Traffic Areas applications with an external TM
    Then accounts should be created

    Examples:
      | OperatorType | LicenceType            |
      | goods        | standard_national      |
      | public       | standard_international |