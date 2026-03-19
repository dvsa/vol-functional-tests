@printAndSign
@int_regression
@grant_under_consideration
@FullRegression
@localsmoke
@smoke

Feature: Grant under consideration application

  Scenario Outline:  Grant a licence
    Given I have a "<vehicle_type>" "<type_of_licence>" application which is under consideration
    When I grant licence
    Then the licence should be granted
    And i create an admin and url search for my application
    And the "<document_type>" document should be generated
    Examples:
      | vehicle_type | type_of_licence        | document_type |
      | goods        | standard_international | GV Licence    |
      | public       | standard_national      | PSV Licence   |

  Scenario: Validation on Grant Application button for PSV (no advert required)
    Given I have a "public" "standard_national" application which is under consideration
    And i create an admin and url search for my application
    Then I select the fee tab and pay the outstanding fees
    When I click the Grant Application button
    Then validation should be checked and the application should be granted if valid


