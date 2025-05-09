@OLCS-22275
@Surrenders-resume
Feature: User should be able to continue where they left off

  @ss_regression @FullRegression
  Scenario Outline: Update correspondence address and resume surrender journey
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    And i have started a surrender
    When i update my address details on my licence
    Then continue with application link is displayed
    And user is taken to information change page on clicking continue application
    And the new correspondence details are displayed on correspondence page

    Examples:
      | OperatorType | LicenceType       |
      | public       | standard_national |

#  Scenario Outline: Remove disc from licence and resume surrender journey
#    Given i have a valid "<OperatorType>" "<LicenceType>" licence
#    When i have started a surrender
#    And i remove a disc to my licence
#    Then continue with application link is displayed
#    And user is taken to information change page on clicking continue application
#
#    Examples:
#      | OperatorType | LicenceType            |
#      | public       | standard_international |

#  Careful here. Will need to change the code to incorporate the new DVLA journey.


  Scenario Outline: Leave correspondence page back to correspondence page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the surrenders review contact details page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to review contact page on clicking continue application

    Examples:
      | OperatorType | LicenceType            |
      | public       | standard_national      |
      | goods        | standard_international |


  Scenario Outline: Leave current discs page and return back to current discs page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the surrenders current discs page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the surrenders current discs on clicking continue application

    Examples:
      | OperatorType | LicenceType            |
      | public       | standard_national      |
      | goods        | standard_international |

  @ss_regression @FullRegression
  Scenario Outline: Leave operator licence page and return back to operator licence page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the operator licence page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the operator licence page on clicking continue application

    Examples:
      | OperatorType | LicenceType            |
      | public       | standard_national      |

  @ss_regression @FullRegression
  Scenario:  Leave community licence page and return back to community licence page
    Given i have a valid "goods" "standard_international" licence
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
      | OperatorType | LicenceType            |
      | public       | standard_national      |
      | goods        | standard_international |


  Scenario Outline: Leave destroy disc page and navigate back to disc and doc review page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the destroy disc page
    When i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the disc and doc review page on clicking continue application

    Examples:
      | OperatorType | LicenceType            |
      | public       | standard_national      |
      | goods        | standard_international |


  Scenario Outline: Leave declaration page and navigate back to disc and doc review page
    Given i have a valid "<OperatorType>" "<LicenceType>" licence
    When i have started a surrender
    And i am on the declaration page
    And i leave the surrenders journey
    Then continue with application link is displayed
    And user is taken to the disc and doc review page on clicking continue application

    Examples:
      | OperatorType | LicenceType            |
      | public       | standard_national      |
      | goods        | standard_international |
