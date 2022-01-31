Feature: The Type of licence page now features the LGV Mixed and LGV Only selection with validation

  Scenario Outline: When choosing a Goods SI licence, the LGV Mixed and LGV Only options appear
    Given I create a new external user
    And i have logged in to self serve
    When i go to apply for a "<Country>" goods standard international licence
    Then i am prompted with the choice of LGV Mixed and LGV Only applications

    Examples:
    | Country |
    | GB      |
    | NI      |

  Scenario Outline: Clicking Yes to LGV Goods starts the LGV Only application route and type of licence is marked as complete
    Given I create a new external user
    And i have logged in to self serve
    When i go to apply for a "<Country>" goods standard international licence
    And i choose to have light goods vehicles only and click save and continue
    Then each section on the application overview page has the correct status for the "lgv_only_fleet" licence

    Examples:
      | Country |
      | GB      |
      | NI      |

  Scenario Outline: Caseworkers can review the yes selection to the LGV Only choice on internal
    Given I create a new external user
    And i have logged in to self serve
    When i go to apply for a "<Country>" goods standard international licence
    And i choose to have light goods vehicles only and click save and continue
    Then the caseworker can review the "yes" LGV Only choice on internal

    Examples:
      | Country |
      | GB      |
      | NI      |

  Scenario Outline: Caseworkers can review the no selection to the LGV Only choice on internal
    Given I create a new external user
    And i have logged in to self serve
    When i go to apply for a "<Country>" goods standard international licence
    And i choose to have mixed vehicles and click save and continue
    Then the caseworker can review the "no" LGV Only choice on internal

    Examples:
      | Country |
      | GB      |
      | NI      |

  Scenario Outline: Cancelling switch LGV Only type warning message and no deletion of data
    Given I have a "GB" lgv only application
    And i navigate to the apply for a licence page
    And I go to update the vehicle type on the licence to "<newLicenceType>" "<newVehicleType>" "N/A"
    When I cancel the warning message and click cancel on the type of licence page
    Then each section on the application overview page should have the complete status with no data deleted

    Examples:
      | newLicenceType         | newVehicleType |
      | standard_national      |                |
      | standard_international | mixed_fleet    |