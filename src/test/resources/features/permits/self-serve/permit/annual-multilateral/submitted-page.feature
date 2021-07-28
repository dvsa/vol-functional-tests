#permit type is not being used at the moment
@MULTILATERAL
Feature: Submitted page

  Background:
    Given I have a "goods" "standard_national" licence
    And I am on the VOL self-serve site

  #AC03
  @OLCS-23019 @olcs-26046
  Scenario: Has reference number and advisory text
    And I am on the annual multilateral submitted page
    Then the reference number on the multilateral submitted page is as expected
    And all the multilateral submitted advisory text is present
    And I open the receipt and it should open in a new window

  #AC04a
  @OLCS-23019
  Scenario: Fee already paid so view receipt link is not displayed
    And I have an ongoing annual multilateral with all fees paid
    Then there shouldn't be a view receipt link on the multilateral submitted page

  #AC04a
  @OLCS-23019
  Scenario: Fee paid by internal case worker
    When a case worker worker pays all fees for my ongoing multilateral permit application
    Then there shouldn't be a view receipt link on the multilateral submitted page

  #AC04a and AC05=
  @INTERNALMULTILATERAL @OLCS-23019
  Scenario: Fee waived by internal case worker
    When a case worker worker waives all fees for my ongoing multilateral permit application
    Then there shouldn't be a view receipt link on the multilateral submitted page

  #AC08
  @OLCS-23019 @olcs-27502 @olcs-27581
  Scenario: User submits application
    When I submit an annual multilateral permit on external
    Then The application status on the self service dashboard goes to VALID
    Then I should be on the permits dashboard page with my application under Issued permits and certificates table