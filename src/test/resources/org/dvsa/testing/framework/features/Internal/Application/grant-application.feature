@printAndSign
@int_regression
@grant_under_consideration
@FullRegression
@localsmoke

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