@OLCS-22913
@INT
@Surrender-int
@int_regression
@gov-verify

Feature: Logic for Surrender menu item

  Background:
    Given i have a valid "goods" "standard_national" licence

  Scenario: Surrender Licence
    And my application to surrender is under consideration
    When the caseworker approves the surrender
    Then the licence status should be "surrendered"
    And the surrender menu should be hidden in internal
    And the licence should not displayed in selfserve

  Scenario: Attempt to Withdraw surrender
    And my application to surrender is under consideration
    When the caseworker attempts to withdraw the surrender
    Then a modal box is displayed

  Scenario: Withdrawn and re apply for a surrender
    And my application to surrender is under consideration
    When the caseworker attempts to withdraw the surrender
    And the caseworker confirms the withdraw
    Then the licence status should be "valid"
    And the surrender menu should be hidden in internal
    And the change history shows the surrender and its withdrawal
    And the user should be able to re apply for a surrender in internal

  Scenario: Cancel surrender withdraw for valid licence
    And my application to surrender is under consideration
    When the caseworker attempts to withdraw the surrender
    And the caseworker cancels the withdraw
    Then the modal box is hidden
    And the surrender menu should be displayed

  Scenario Outline: Cancel surrender withdraw for suspended and curtailed licence
    And the licence status is "<licence_status>"
    And my application to surrender is under consideration
    When the caseworker attempts to withdraw the surrender
    And the caseworker cancels the withdraw
    Then the user should remain on the surrender details page
    And the modal box is hidden
    And the surrender menu should be displayed

    Examples:
      | licence_status |
      | suspend        |
      | curtail        |

  Scenario Outline: Undo a surrender
    And the licence status is "<licence_status>"
    And my application to surrender is under consideration
    When the caseworker approves the surrender
    And the licence status should be "surrendered"
    And the case worker undoes the surrender
    Then the licence status should be "<licence>"

    Examples:
      | licence_status | licence   |
      | suspend        | suspended |
      | curtail        | curtailed |

  Scenario Outline: Surrender suspended, curtailed licence
    And the licence status is "<licence_status>"
    And my application to surrender is under consideration
    When the caseworker approves the surrender
    Then the licence status should be "<licence>"
    And the surrender menu should be hidden in internal
    And the licence should not displayed in selfserve

    Examples:
      | licence_status | licence     |
      | suspend        | Surrendered |
      | curtail        | Surrendered |

  Scenario: Check links are hidden
    And my application to surrender is under consideration
    When i create an admin and url search for my licence
    Then the quick actions and decision buttons are not displayed for the menu items listed
      | Cases           |
      | IRHP            |
      | Docs            |
      | Processing      |
      | Fees            |
      | Licence details |