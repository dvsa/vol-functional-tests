@int_regression
@FullRegression
@transport_manager

Feature: Transport Manager - internal detail and processing tabs

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to internal as "admin"
    And I capture the transport manager id from the licence

  @Tm_details_tab
  Scenario: Transport Manager Details tab loads
    When I navigate to the transport manager Details tab
    Then the transport manager "Details" page should be displayed at path "details/"
    And the transport manager Details page should display the personal details section

  @Tm_competences_tab
  Scenario: Add a competence to a Transport Manager (Competences tab)
    When I add a competence "tm_qt_cpcsn" with serial "AUTO-CPC-12345" issued on "01" "02" "2020" to the transport manager
    Then the transport manager Competences table should contain serial "AUTO-CPC-12345"

  @Tm_employment_tab
  Scenario: Add an employer to a Transport Manager (Employment tab)
    When I add an employer "Automated Employer Ltd" to the transport manager with position "Owner" hours "10"
    Then the transport manager Employment table should contain employer "Automated Employer Ltd"

  @Tm_previous_history_tab
  Scenario: Add a previous licence to a Transport Manager (Previous History tab)
    When I add a previous licence "OB1234567" with holder "Automated Test Holder" to the transport manager
    Then the transport manager Previous History section should contain "OB1234567"

  @Tm_documents_tab
  Scenario: Upload a document to a Transport Manager (Documents tab)
    When I upload a document "AutomatedTmDocument" to the transport manager
    Then the transport manager Documents table should contain "AutomatedTmDocument"
    And the transport manager Documents table should contain a document linked to the licence

  @Tm_cases_tab
  Scenario: Add a case to a Transport Manager (Cases tab)
    When I add a case "AutomatedTmCase" to the transport manager
    Then the transport manager Cases table should contain "AutomatedTmCase"

  @Tm_processing_notes_tab
  Scenario: Add a processing note to a Transport Manager (Processing Notes tab)
    When I add a processing note "Automated TM note - please ignore" to the transport manager with priority "Y"
    Then the transport manager Notes table should contain "Automated TM note - please ignore"

  @Tm_processing_event_history_tab
  Scenario: Actions on a Transport Manager are recorded in Change History
    When I add a processing note "Change-history probe" to the transport manager with priority "N"
    And I navigate to the transport manager Processing Event History tab
    Then the transport manager "Change history" page should be displayed at path "processing/event-history/"
    And the transport manager Change History table should contain at least one entry

  @Tm_processing_publication_tab
  Scenario: A Public Inquiry decision on a TM case appears on the TM Publication tab
    When I add a case "AutomatedTmCasePI" to the transport manager
    And I add a public inquiry to the transport manager case
    And I add a hearing to the transport manager's public inquiry
    And I add a decision to the transport manager's public inquiry
    And I navigate to the transport manager Processing Publication tab
    Then the transport manager "Publication" page should be displayed at path "processing/publication/"
    And the transport manager Publication table should contain at least one entry

  @Tm_processing_read_history_tab
  Scenario: Visits to a Transport Manager are recorded in Access History
    When I navigate to the transport manager Details tab
    And I navigate to the transport manager Processing Read History tab
    Then the transport manager "Access history" page should be displayed at path "processing/read-history/"
    And the transport manager Access History table should contain at least one entry

  @Tm_relink_document_from_licence
  Scenario: Copy a document from a licence to a Transport Manager via Relink
    When I copy the first licence document to the transport manager via Relink
    And I navigate to the transport manager Documents tab
    Then the transport manager Documents table should contain the relinked document

  @Tm_edit_competence
  Scenario: Edit an existing competence on a Transport Manager
    When I add a competence "tm_qt_cpcsn" with serial "AUTO-CPC-11111" issued on "01" "02" "2020" to the transport manager
    And I edit the transport manager's competence serial to "AUTO-CPC-99999"
    Then the transport manager Competences table should contain serial "AUTO-CPC-99999"

  @Tm_edit_responsibilities
  Scenario: Set the manager type on a Transport Manager's responsibility
    When I open the transport manager's responsibility edit page for the first licence
    Then the browser should be on a transport manager responsibility edit page
    When I set the transport manager's responsibility manager type to "tm_t_e" and save
    Then the transport manager Responsibilities table Manager type should be "External"

  @Tm_edit_case
  Scenario: Edit a case on a Transport Manager
    When I add a case "Original TM case description" to the transport manager
    And I edit the transport manager's case description to "Edited TM case description"
    Then the transport manager Cases table should contain "Edited TM case description"

  @Tm_merge
  Scenario: Merge one Transport Manager into another and verify licence links transfer
    Given I capture the first transport manager as the merge source
    When I create a second application and capture the second transport manager
    And I merge the first transport manager into the second
    Then the winning transport manager should be linked to both licences

  @Tm_licence_search_combined_filters
  Scenario: Transport manager licence search honours the licence and transport manager filters together
    Given I capture the first transport manager as the merge source
    When I create a second application and capture the second transport manager
    And I merge the first transport manager into the second
    And I identify the transport manager that holds both licences
    Then searching transport manager licences by that transport manager alone should return both licences
    And searching transport manager licences by the first licence alone should return only that licence
    And searching transport manager licences by both the first licence and that transport manager should return only the matching record
