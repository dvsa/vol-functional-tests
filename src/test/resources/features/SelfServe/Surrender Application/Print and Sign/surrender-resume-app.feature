@OLCS-22275
@SS
@Surrenders-resume
@ss_regression
Feature: User should be able to continue where they left off

  Scenario Outline: Update correspondence address and resume surrender journey
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    And i have started a surrender
    When i update my address details on my licence
    Then continue with application link is displayed
    And user is taken to information change page on clicking continue application
    And the new correspondence details are displayed on correspondence page

    Examples:
      | OperatorType | LicenceType |
      | public       | si          |

  Scenario Outline: Remove disc from licence and resume surrender journey
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i remove a disc to my licence
    Then continue with application link is displayed
    And user is taken to information change page on clicking continue application

    Examples:
      | OperatorType | LicenceType |
      | public       | si          |

  Scenario Outline: Leave correspondence page back to correspondence page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the surrenders review contact details page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to review contact page on clicking continue application

    Examples:
      | OperatorType | LicenceType |
      | public       | sn          |
      | goods        | si          |

  Scenario Outline: Leave current discs page and return back to current discs page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the surrenders current discs page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the surrenders current discs on clicking continue application

    Examples:
      | OperatorType | LicenceType |
      | public       | sn          |
      | goods        | si          |

    @lastfew
  Scenario Outline: Leave operator licence page and return back to operator licence page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the operator licence page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the operator licence page on clicking continue application

    Examples:
      | OperatorType | LicenceType |
      | public       | sn          |
      | goods        | si          |

  Scenario:  Leave community licence page and return back to community licence page
    Given i have a valid "goods" "si" licence
    When i have started a surrender
    And i am on the community licence page
    When i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the community licence page on clicking continue application

  Scenario Outline: Return back to disc and doc review page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the disc and doc review page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the disc and doc review page on clicking continue application

    Examples:
      | OperatorType | LicenceType |
      | public       | sn          |
      | goods        | si          |

  Scenario Outline: Leave destroy disc page and navigate back to disc and doc review page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the destroy disc page
    When i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the disc and doc review page on clicking continue application

    Examples:
      | OperatorType | LicenceType |
      | public       | sn          |
      | goods        | si          |

  Scenario Outline: Leave declaration page and navigate back to disc and doc review page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the declaration page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the disc and doc review page on clicking continue application

    Examples:
      | OperatorType | LicenceType |
      | public       | sn          |
      | goods        | si          |