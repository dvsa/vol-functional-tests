#permit type is not being used at the moment
@MULTILATERAL
Feature: Annual Multilateral permit number-of-permits page

  Background:
    Given  I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I am on the number of permits required page

  #AC01
  @OLCS-24290
  Scenario:Back button return to the 'Multilateral permits Application overview' page
    When I go back
    Then I should be on the Annual Multilateral overview page

  #AC02/AC03:
  @OLCS-24290
  Scenario:Page heading has got relevant information as per the AC
    Then the reference number and heading are displayed correct
    When  I enter zero as value in the number of permits fields
    And  I save and continue
    Then the relevant error message is displayed
    And  I select save and return overview link
    Then the relevant error message is displayed
    When I specify my number of multilateral permits
    Then I am navigated to annual multilateral check your answers page

  #AC14
  @OLCS-24290
  Scenario: Save and Return to overview updates and takes the user to overview
    When I specify my number of multilateral permits
    And I select save and return overview link
    Then the number of permits section on the annual multilateral overview page is complete

  #AC15
  @OLCS-24542  @OLCS-24290
  Scenario: Number of Permits requested changes should update the corresponding fee record
    And I specify the number of permits I require for my multilateral permit
    And the case worker is viewing current fees
    When I update the number of permits for my multilateral permit
    Then the fees are to be updated to reflect changes

  #AC16
  @OLCS-24542 @OLCS-24290  @olcs-27581
  Scenario: Remembers answer when you change your answer
    When I specify my number of multilateral permits
    And I choose to change the number of permits section
    Then my previously selected values are remembered