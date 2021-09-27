Feature: Publications generate on LGV related changes and regression


  Scenario Outline: Publications generate and display correctly on both apps. (LGV variation increase)
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the "New Variation" "<variationType>" publication text is correct with "<HGVs>" hgvs and "<LGVs>" lgvs

    Examples:
    | licenceType            | HGVs | LGVs | variationType |
    | standard_international | 5    | 5    | LGV           |
    | standard_international | 8    | 0    | HGV           |
    | standard_international | 8    | 5    | HGV and LGV   |
    | standard_national      | 8    | 0    | HGV           |

  Scenario Outline: variation grant displays new information
    Given i have a valid "goods" "<licenceType>" licence
    And i create and submit and grant an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the "Variation Granted" "<variationType>" publication text is correct with "<HGVs>" hgvs and "<LGVs>" lgvs

    Examples:
      | licenceType            | HGVs | LGVs | variationType |
      | standard_international | 5    | 5    | LGV           |
      | standard_international | 8    | 0    | HGV           |
      | standard_international | 8    | 5    | HGV and LGV   |
      | standard_national      | 8    | 0    | HGV           |

  Scenario: Out of objection date displays 22 days after submitting


#  Scenario Out of Objection date

#  as soon as granted, publication updates without requiring another publish.