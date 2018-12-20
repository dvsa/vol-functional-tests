@SS
@OLCS-22256

  Feature: Operator declares Standard National licence details

    Background:
      Given i have a valid "public" licence
      And i choose to surrender my licence
      And i navigate to the operator licence page

    Scenario: Licence in operators possession
      When the user selects in your possession option
      Then they should be presented with the text You must destroy your operator licence

      Scenario: Licence is lost
        When the user select the Lost option
        Then they are presented with the lost additional information text box

    Scenario: Licence is stolen
      When the user select the Stolen option
      Then they are presented with the stolen additional information text box

    Scenario: Surrender licence
      When I click on Save and continue
      Then I should be taken to the Review your discs and documentations page
      And the correct licence details should be displayed
      And the correct discs details should be displayed
      And the correct documentation details should be displayed