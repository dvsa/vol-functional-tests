Feature: Publications generate on LGV related changes and regression


  Scenario Outline: Publications generate and display correctly on both apps. (LGV variation increase)
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the "<variationType>" publication text is correct with "<HGVs>" hgvs and "<LGVs>" lgvs

    Examples:

    | licenceType            | HGVs | LGVs | variationType |
#    | standard_international | 5    | 5    | LGV           |
#    | standard_international | 8    | 0    | HGV           |
    | standard_international | 8    | 5    | HGV and LGV   |
#    | standard_national      | 8    | 0    | HGV           |

  Scenario: variation grant displays new information?

  Scenario: new licence displays the correct information after publish


#  submit variation, check internal, publish, check internal, search for on SS, text matches.

#  Scenario Out of Objection date

#  as soon as granted, publication updates without requiring another publish.