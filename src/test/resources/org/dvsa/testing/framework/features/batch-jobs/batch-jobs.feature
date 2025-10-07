@batch-smoke
Feature:  AWS Batch

  Scenario:  Last TM Letter
    Given I trigger the Last TM letter batch job
    Then that should should be successful