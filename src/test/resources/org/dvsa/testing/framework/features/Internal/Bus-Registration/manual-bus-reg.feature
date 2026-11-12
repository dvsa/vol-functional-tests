@MANUAL-BUS-REG
@int_regression
@CPMS_tests
@FullRegression
@printAndSign

Feature: Complete Manual Bus Registration Steps

  Background: Testing
    Given as a "admin" I have a psv application with traffic area "north_east" and enforcement area "north_east" which has been granted
    And i have logged in to internal as "admin"

  @localsmoke @manualbus @manual-bus-route-values @vol763
  Scenario: Paying Fees to Complete Bus Registration Manually
    And i add a manual bus registration with service details
      | serviceNo  | 123            |
      | startPoint | River Road     |
      | finishPoint | Market Square |
      | via        | High Street    |
    When it has been paid and granted
    Then the bus registration should be granted
    And the traffic areas should be displayed on the service details page

  @manual-bus-variation @manual-bus-registration-history @vol763
  Scenario: Create and grant a variation and cancellation of a manually registered bus route
    And i add a manual bus registration with service details
      | serviceNo  | 459                |
      | startPoint | North Terminal     |
      | finishPoint | Lakeside Centre    |
      | via        | Cedar Avenue       |
    When it has been paid and granted
    Then the bus registration should be granted
    When I create a variation of the manual bus registration
    Then the new bus registration variation should inherit the original service details
    When I update the bus registration variation with service details
      | serviceNo  | 460              |
      | startPoint | West Terminal    |
      | finishPoint | Hilltop Centre   |
      | via        | King Street      |
    Then the variation should retain the updated service details and the original registration should remain unchanged
      | serviceNo  | 460              |
      | startPoint | West Terminal    |
      | finishPoint | Hilltop Centre   |
      | via        | King Street      |
    When I grant the manual bus registration variation
      | reason | Timetable |
    Then the bus registration should be granted
    And the manual registration history should show
      | record    | variationNo | status     | applicationType | dateReceived | dateEffective          |
      | original  | 0           | Registered | Manual          | Today        | Five months from today |
      | variation | 1           | Registered | Manual          | Today        | Five months from today |
    When I create a cancellation for the manual bus registration
    Then the cancellation should inherit the saved service details
    When I save the cancellation dates
      | dateReceived            | Today                  |
      | dateEffective           | Seventy days from today |
      | applicationCompleteDate | Today                  |
      | endDate                 | Leave blank            |
    Then the cancellation dates should remain saved
    When I complete the manual bus cancellation registration checks
      | Timetable acceptable             | Yes |
      | Maps supplied                    | Yes |
      | TRC's / Conditions satisfactory  | Yes |
      | Application copied to LA/PTE     | Yes |
      | LA/PTE support short notice      | Yes |
      | Application signed               | Yes |
    Then the manual bus cancellation registration checks should remain saved
      | Timetable acceptable             | Yes |
      | Maps supplied                    | Yes |
      | TRC's / Conditions satisfactory  | Yes |
      | Application copied to LA/PTE     | Yes |
      | LA/PTE support short notice      | Yes |
      | Application signed               | Yes |
    When I grant the manual bus cancellation
    Then the manual bus cancellation should remain cancelled after reload
    And the manual registration history should show
      | record       | variationNo | status     | applicationType | dateReceived | dateEffective           | endDate     |
      | original     | 0           | Registered | Manual          | Today        | Five months from today  |             |
      | variation    | 1           | Registered | Manual          | Today        | Five months from today  |             |
      | cancellation | 2           | Cancelled  | Manual          | Today        | Seventy days from today | Leave blank |

  @manual-bus-short-notice @vol763
  Scenario: Register a short-notice manual bus variation
    And i add a manual bus registration with service details
      | serviceNo  | 462              |
      | startPoint | River Road       |
      | finishPoint | Market Square   |
      | via        | High Street      |
    When it has been paid and granted
    Then the bus registration should be granted
    When I create a variation of the manual bus registration
    Then the new bus registration variation should inherit the original service details
    When I update the short-notice bus registration variation with service details
      | serviceNo  | 463               |
      | startPoint | Central Terminal  |
      | finishPoint | Harbour Exchange |
      | via        | Mill Road         |
    When I save the short notice reason and comment
      | reason  | Unforseen circumstances                                                |
      | comment | A temporary road closure requires this service to start immediately. |
    Then the short notice reason and comment should remain saved
      | reason  | Unforseen circumstances                                                |
      | comment | A temporary road closure requires this service to start immediately. |
    When I complete the short notice registration checks
      | Timetable acceptable             | Yes |
      | Maps supplied                    | Yes |
      | TRC's / Conditions satisfactory  | Yes |
      | Application copied to LA/PTE     | Yes |
      | LA/PTE support short notice      | Yes |
      | Application signed               | Yes |
    Then the short notice registration checks should remain saved
      | Timetable acceptable             | Yes |
      | Maps supplied                    | Yes |
      | TRC's / Conditions satisfactory  | Yes |
      | Application copied to LA/PTE     | Yes |
      | LA/PTE support short notice      | Yes |
      | Application signed               | Yes |
    When I grant the manual bus registration variation
      | reason | Timetable |
    Then the bus registration should be granted

  @manual-bus-cancellation @vol763
  Scenario: Cancel a manually registered bus route
    And i add a manual bus registration with service details
      | serviceNo  | 468                |
      | startPoint | Orchard Terminal   |
      | finishPoint | Riverside Centre |
      | via        | Station Road       |
    When it has been paid and granted
    Then the bus registration should be granted
    When I create a cancellation for the manual bus registration
    Then the cancellation should inherit the saved service details
    When I save the cancellation dates
      | dateReceived            | Today                  |
      | dateEffective           | Seventy days from today |
      | applicationCompleteDate | Today                  |
      | endDate                 | Leave blank            |
    Then the cancellation dates should remain saved
    When I complete the manual bus cancellation registration checks
      | Timetable acceptable             | Yes |
      | Maps supplied                    | Yes |
      | TRC's / Conditions satisfactory  | Yes |
      | Application copied to LA/PTE     | Yes |
      | LA/PTE support short notice      | Yes |
      | Application signed               | Yes |
    Then the manual bus cancellation registration checks should remain saved
      | Timetable acceptable             | Yes |
      | Maps supplied                    | Yes |
      | TRC's / Conditions satisfactory  | Yes |
      | Application copied to LA/PTE     | Yes |
      | LA/PTE support short notice      | Yes |
      | Application signed               | Yes |
    When I grant the manual bus cancellation
    Then the manual bus cancellation should remain cancelled after reload


  Scenario: Editing a bus registration of status Registered
    When i add a new bus registration
    And it has been paid and granted
    Then all Service Details fields should be editable
    And the edited Bus Registration details should be saved