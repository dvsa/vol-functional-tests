@eupa_regression
Feature: Licence checks on submit

  @EXTERNAL @OLCS-22701 @annual_ecmt_apgg_euro5_or_euro6
  Scenario Outline: Able to apply for an ECMT permit with allowed licence type on that are valid on external and without ECMT permit applications already
    Given I have a "goods" "<type>" licence
    And I am on the VOL self-serve site
    And I have began applying for an ECMT Permit
    When I fill in the permits form
    Then I expect my application to be submitted

    Examples:
      | type                   |
      | standard_international |
      | standard_national      |
      | restricted             |

  @INTERNAL @OLCS-22701 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario Outline: Able to apply for an ECMT permit with allowed licence type on that are valid on internal and without ECMT permit applications already
    Given I have a "goods" "<type>" licence
    And i create an admin and url search for my licence
    And I apply for an ECMT APGG Euro5 or Euro 6 application
    And submit my ECMT permit application
    Then my permit application is under consideration

    Examples:
      | type                   |
      | standard_international |
      | standard_national      |
      | restricted             |

  @EXTERNAL @OLCS-22701 @annual_ecmt_apgg_euro5_or_euro6
  Scenario Outline: Able to apply for an ECMT permit with allowed licence type on that are curtailed on external and without ECMT permit applications already
    Given i have a valid "goods" "<type>" licence
    And the licence status is "curtail"
    And I am on the VOL self-serve site
    And I have began applying for an ECMT Permit
    When I fill in the permits form
    Then I expect my application to be submitted

    Examples:
      | type                   |
      | standard_international |
      | standard_national      |
      | restricted             |

  @INTERNAL @OLCS-22701 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario Outline: Able to apply for an ECMT permit with allowed licence type on that are curtailed on internal and without ECMT permit applications already
    Given I have a "goods" "<type>" licence
    And the licence status is "curtail"
    And i create an admin and url search for my licence
    And I apply for an ECMT APGG Euro5 or Euro 6 application
    And submit my ECMT permit application
    Then my permit application is under consideration

    Examples:
      | type                   |
      | standard_international |
      | standard_national      |
      | restricted             |

  @EXTERNAL @OLCS-22701 @annual_ecmt_apgg_euro5_or_euro6
  Scenario Outline: Able to apply for an ECMT permit with allowed licence type on that are suspended on external and without ECMT permit applications already
    Given I have a "goods" "<type>" licence
    And the licence status is "suspend"
    And I am on the VOL self-serve site
    And I have began applying for an ECMT Permit
    When I fill in the permits form
    Then I expect my application to be submitted

    Examples:
      | type                   |
      | standard_international |
      | standard_national      |
      | restricted             |

  @INTERNAL @OLCS-22701 @internal_annual_ecmt_apgg_euro5_or_euro6
  Scenario Outline: Able to apply for an ECMT permit with allowed licence type on that are suspended on internal and without ECMT permit applications already
    Given I have a "goods" "<type>" licence
    And the licence status is "suspend"
    And i create an admin and url search for my licence
    When I apply for an ECMT APGG Euro5 or Euro 6 application
    And submit my ECMT permit application
    Then my permit application is under consideration

    Examples:
      | type                   |
      | standard_international |
      | standard_national      |
      | restricted             |