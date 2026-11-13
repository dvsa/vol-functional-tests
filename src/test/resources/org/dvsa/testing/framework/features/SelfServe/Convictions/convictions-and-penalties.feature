@ss_regression
@vol763
Feature: Application convictions and penalties declarations

  @application-conviction-required-fields
  Scenario: An application conviction requires its details
    Given i have a "goods" "GB" partial application
    When I open the "application" convictions and penalties declarations
    And I submit an empty application conviction
    Then the application conviction required fields show errors in the summary and inline
      | data[title]        | Choose title                               |
      | data[forename]     | Enter first name                           |
      | data[familyName]   | Enter last name                            |
      | dob_day            | Enter date of conviction                   |
      | data[categoryText] | Enter a value for the field: "Offence"     |
      | data[notes]        | Please give details about the offence      |
      | data[courtFpn]     | Provide the name of the court you attended |
      | data[penalty]      | Tell us what penalty you received          |
    When I cancel the conviction entry
    Then no conviction has been added

  @application-conviction-add
  Scenario: Declare a conviction on an application
    Given i have a "goods" "GB" partial application
    When I open the "application" convictions and penalties declarations
    And I add a conviction to the declarations
      | title      | Mr                    |
      | forename   | Alex                  |
      | familyName | Driver                |
      | offence    | Speeding              |
      | details    | Exceeded speed limit  |
      | court      | Nottingham court      |
      | penalty    | Fine                  |
      | day        | 01                    |
      | month      | 02                    |
      | year       | 2020                  |
    And I save the convictions and penalties declarations
    Then the "application" declarations contain the saved conviction
      | forename   | Alex             |
      | familyName | Driver           |
      | offence    | Speeding         |
      | court      | Nottingham court |
      | penalty    | Fine             |
      | day        | 01               |
      | month      | 02               |
      | year       | 2020             |

  @variation-conviction-add
  Scenario: Declare a conviction on a licence variation
    Given i have a valid "goods" "restricted" licence
    When i upgrade my licence type to Standard National
    And I open the "variation" convictions and penalties declarations
    And I add a conviction to the declarations
      | title      | Mr                    |
      | forename   | Alex                  |
      | familyName | Driver                |
      | offence    | Speeding              |
      | details    | Exceeded speed limit  |
      | court      | Nottingham court      |
      | penalty    | Fine                  |
      | day        | 01                    |
      | month      | 02                    |
      | year       | 2020                  |
    And I save the convictions and penalties declarations
    Then the "variation" declarations contain the saved conviction
      | forename   | Alex             |
      | familyName | Driver           |
      | offence    | Speeding         |
      | court      | Nottingham court |
      | penalty    | Fine             |
      | day        | 01               |
      | month      | 02               |
      | year       | 2020             |

  @application-conviction-edit-delete
  Scenario: Edit and remove an application conviction
    Given i have a "goods" "GB" partial application
    When I open the "application" convictions and penalties declarations
    And I add a conviction to the declarations
      | title      | Mr                    |
      | forename   | Alex                  |
      | familyName | Driver                |
      | offence    | Speeding              |
      | details    | Exceeded speed limit  |
      | court      | Nottingham court      |
      | penalty    | Fine                  |
      | day        | 01                    |
      | month      | 02                    |
      | year       | 2020                  |
    And I change the declared conviction for "Alex Driver" to offence "Driving offence"
    And I save the convictions and penalties declarations
    Then the "application" declarations contain the saved conviction
      | forename   | Alex             |
      | familyName | Driver           |
      | offence    | Driving offence  |
      | court      | Nottingham court |
      | penalty    | Fine             |
      | day        | 01               |
      | month      | 02               |
      | year       | 2020             |
    When I remove the declared conviction for "Alex Driver"
    Then the declared conviction for "Alex Driver" is removed

  @application-no-convictions
  Scenario: Declare no convictions without entering offence details
    Given i have a "goods" "GB" partial application
    When I open the "application" convictions and penalties declarations
    And I declare no convictions
    Then offence entry is hidden
    When I save the convictions and penalties declarations
    Then the "application" declarations retain no convictions without offence entry
