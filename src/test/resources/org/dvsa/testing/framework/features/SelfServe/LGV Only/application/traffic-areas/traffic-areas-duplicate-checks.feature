@lgv-traffic-area
Feature: When applying for a licence or variation in the same traffic area as a preexisting licence triggers an error

  Background:
    Given I create a new external user

  @ss_regression
  Scenario: Applying for a Goods SI licence with an already Goods SI licenced traffic area triggers an error
    And i have a "goods" "standard_international" licence with a hgv authorisation of "5" in the North West Of England
    When i apply for a new "GB" lgv only application and enter a postcode in the North West of England
    Then the traffic area is set as North West of England
    And a licence already exists in this traffic area error appears when I complete the page

  Scenario: Applying for a Goods SI licence with an already Goods SN licenced traffic area triggers an error
    And i have a "goods" "standard_national" licence with a hgv authorisation of "5" in the North West Of England
    When i apply for a new "GB" lgv only application and enter a postcode in the North West of England
    Then the traffic area is set as North West of England
    And a licence already exists in this traffic area error appears when I complete the page

  Scenario: Applying for a Goods SI licence with an already PSV SI licenced traffic area triggers an error
    And i have a "public" "standard_international" licence with a hgv authorisation of "5" in the North West Of England
    When i apply for a new "GB" lgv only application and enter a postcode in the North West of England
    Then the traffic area is set as North West of England
    And a licence already exists in this traffic area error does not appear when I complete the page