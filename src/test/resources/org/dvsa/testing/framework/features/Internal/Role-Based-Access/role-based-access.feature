@int_regression
@readOnly
@FullRegression
Feature: Users should have functionality based on their system role

  Background:
    Given i am on the internal admin login page

  @readonly-business-details
  Scenario Outline: Limited Read only and Read Only users should on be able to view business details
    Given i have logged in to internal as "<User>"
    When i search for and click on my licence "OB1134621"
    Then i cannot make changes to the business details page
    Examples:
      | User                |
      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-addresses
  Scenario Outline: Limited Read only and Read Only users should not be able to view Address details
    Given i have logged in to internal as "<User>"
    When i search for and click on my licence "OB1134621"
    Then i cannot make changes to the addresses page
    Examples:
      | User                |
      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-directors
  Scenario Outline: Limited Read only and Read Only directors name should not be displayed as links
    Given i have logged in to internal as "<User>"
    When i search for and click on my licence "OB1134621"
    Then directors names should not be displayed as links
    Examples:
      | User                |
#      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-oc
  Scenario Outline: Limited Read only and Read Only should not view input fields on operating centres
    Given i have logged in to internal as "<User>"
    When i search for and click on my licence "OB1134621"
    Then operating centre names should not be displayed as links
    Examples:
      | User                |
      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-safety
  Scenario Outline: Limited Read only and Read Only should not be able to view safety inspector names as links
    Given i have logged in to internal as "<User>"
    When i search for and click on my licence "OB1134621"
    Then safety inspector names should not be displayed as links
    Examples:
      | User                |
      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-irhp-permits
  Scenario Outline: Limited Read only and Read Only should not be able to view permit number as links
    Given i have logged in to internal as "<User>"
    When i search for and click on my licence "OC1057274"
    Then permit reference number should not be displayed as links
    Examples:
      | User                |
      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-TM
  Scenario Outline: Limited Read only and Read Only should not be able to view buttons input fields and links for TM page
    Given i have logged in to internal as "<User>"
    When i url search for a transport manager
    Then tm details page should not display buttons and links
    And responsibilities page should not display input fields
    Examples:
      | User                |
      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-irfo
  Scenario Outline: Limited Read only and Read Only should not be able to view buttons input fields on IRFO page
    Given i have logged in to internal as "<User>"
    When i search for and click on my licence "OC1057274"
    Then irfo page should not displayed any input fields
    Examples:
      | User                |
      | limitedReadOnlyUser |
      | readOnlyUser        |

  @readonly-snapshots
  Scenario Outline: Read Only should not be able to view input fields and create snapshots on cases
    Given i have logged in to internal as "<User>"
    When i url search for a case
    Then i should not be able to add case details
    And i should not be able to create snapshots
    And i should not be able to edit case details
    Examples:
      | User         |
      | readOnlyUser |