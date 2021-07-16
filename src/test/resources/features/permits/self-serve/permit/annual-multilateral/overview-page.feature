#permit type is not being used at the moment
@MULTILATERAL
Feature: Annual multilateral permit overview page

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I have began applying for an Annual Multilateral Permit
    And I'm on the annual multilateral overview page

  #AC01
  @OLCS-23013 @olcs-27581
  Scenario: Back link returns to permits dashboard
    When I click the back link
    Then I should be on the permits dashboard page with an ongoing application

  #AC02
  @OLCS-23013
  Scenario: Reference number is displayed
    Then the application reference number should be on the annual multilateral overview page

  #AC06 and AC07
  @OLCS-23013 @olcs-27581
  Scenario Outline: Section links navigate to relevant pages
    When I select <section> from multilateral overview page
    Then I am navigated to the corresponding page for <section>

    Examples:
      |section                   |
      |Number of permits required|

  #AC06, AC07, AC08, AC09, and AC11
  @OLCS-23013  @olcs-27581
  Scenario: Sections have expected default statuses
    Then the default section statuses are as expected

  # TODO: ACs partially done as pages are for these sections are not fully done
  #AC10 and AC11
  @OLCS-23013
  Scenario: Section links are disabled if previous steps have not been completed
    Then future sections beyond the next following step from currently completed section are disabled

  #AC15
  @OLCS-23013
  Scenario: Cancels annual multilateral permit application
    When I click cancel application link on the overview page
    Then I should be taken to cancel confirmation page