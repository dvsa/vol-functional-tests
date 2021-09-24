Feature: Publications


  Scenario: Publications generate and display correctly on both apps. (LGV increase)
    Given i have a valid "goods" "standard_international" licence
    And i create and submit an operating centre variation with "5" hgvs and "5" lgvs
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search

  Scenario: new variation triggers and information is right.

  Scenario: variation grant displays new information?

  Scenario: new licence displays the correct information after publish


#  submit variation, check internal, publish, check internal, search for on SS, text matches.

#  Scenario Out of Objection date

#  as soon as granted, publication updates without requiring another publish.