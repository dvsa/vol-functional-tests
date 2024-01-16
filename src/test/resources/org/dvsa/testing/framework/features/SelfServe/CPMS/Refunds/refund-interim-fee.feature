@OLCS-23724
@ss-interim-refund
Feature: Refund fee paid on new application

  @ss_regression @printAndSign @interim-refused-refund
  Scenario Outline: Interim fee has been paid and licence has been refused
    Given i have an interim "<OperatorType>" "<LicenceType>" application
    When the interim fee has been paid
    And the application has been refused
    Then the interim fee should be refunded

    Examples:
      | OperatorType | LicenceType            |
      | goods        | standard_international |
      | goods        | standard_national      |

  Scenario Outline: Interim fee has been paid and licence has been withdrawn
    Given i have an interim "<OperatorType>" "<LicenceType>" application
    When the interim fee has been paid
    And the application has been withdrawn
    Then the interim fee should be refunded

    Examples:
      | OperatorType | LicenceType       |
      | goods        | standard_national |

  @CPMS_tests
  Scenario Outline: Interim fee has been paid and licence has been granted
    Given i have an interim "<OperatorType>" "<LicenceType>" application
    When the interim fee has been paid
    And the licence is granted
    Then the interim fee should be refunded

    Examples:
      | OperatorType | LicenceType       |
      | goods        | standard_national |

  Scenario Outline: Interim fee has been paid and granted and licence has been granted
    Given i have an interim "<OperatorType>" "<LicenceType>" application
    When the interim fee has been paid
    And the interim is granted
    And the licence is granted
    Then the interim fee should not be refunded

    Examples:
      | OperatorType | LicenceType       |
      | goods        | standard_national |

  Scenario: Interim fee has been paid and variation application has been refused
    Given i have a valid "goods" "standard_national" licence
    And i increase my vehicle authority count
    When i pay for the interim application
    And the variation application has been refused
    Then the interim fee should be refunded

  Scenario: Interim fee has been paid and variation application has been withdrawn
    Given i have a valid "goods" "standard_international" licence
    And i increase my vehicle authority count
    When i pay for the interim application
    And the variation application has been withdrawn
    Then the interim fee should be refunded

  @ss_regression
  Scenario: Interim fee has been paid and granted
    Given i have a valid "goods" "standard_international" licence
    And i increase my vehicle authority count
    When i pay for the interim application
    And the variation interim is granted
    Then the interim fee should not be refunded
    And the interim fee has been paid
    And the "GV Interim Direction" document should be generated
