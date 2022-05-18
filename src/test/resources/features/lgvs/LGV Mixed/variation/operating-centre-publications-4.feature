@lgv
@cross-browser
@lgv-smoke

Feature: 4 - Publications generate for increased vehicle authorities where OCs are not changed

  Scenario Outline: Publications generate and display correctly on both apps. (Authority only increases)
    Given I have a "goods" "<licenceType>" licence with 2 operating centres
    And i change my total vehicle authorities to "<HGVs>" HGVs "<LGVs>" LGVs and "same" trailers without changing the operating centres
    And The variation is submitted
    When the corresponding publication is generated and published
    Then the publication is visible via self serve search
    And the "New Variation" "<variationType>" publication text is correct with "<HGVs>" hgvs and "<LGVs>" lgvs

    Examples:
    | licenceType            | HGVs | LGVs | variationType    |
    | standard_international | same | 10   | LGV              |
    | standard_international | 10   | same | HGV auth         |
    | standard_international | 8    | 5    | HGV and LGV auth |
    | standard_national      | 10   | same | HGV auth         |
