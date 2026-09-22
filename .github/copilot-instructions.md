# Copilot Instructions for vol-functional-tests

## Build & Test Commands

```bash
# Run tests by tag (primary way to execute)
mvn clean test -Denv=<env> -Dbrowser=<browser> -Dcucumber.filter.tags="@tagName"

# Run full suite via integration-test phase (used in CI)
mvn clean verify -Denv=<env> -Dbrowser=<browser> -Dcucumber.filter.tags="@tagName"

# Run a single feature file by its tag
mvn clean test -Denv=qa -Dbrowser=chrome -Dcucumber.filter.tags="@APPLY-GOODS-LICENCE"

# Generate Allure report after test run
mvn allure:report

# Dependency vulnerability scan (skips tests)
mvn verify -DskipTests
```

**Required system properties**: `-Denv` (e.g. `qa`, `uat`, `prod`) and `-Dbrowser` (e.g. `chrome`, `edge`). When running against a Selenium Grid, also supply `-DgridURL=<url>`.

> Note: `int` is an alias for `qa` — the `run.sh` script replaces `int` with `qa` automatically.

## Architecture

This is a **Cucumber BDD test framework** structured in four layers:

```
Feature files (.feature)         — Gherkin scenarios, tag-filtered
    ↓
Step definitions (stepdefs/)     — bind @Given/@When/@Then to Java
    ↓
Journey classes (Journeys/)      — orchestrate multi-step flows
    ↓
Page objects (pageObjects/)      — selectors & WebDriver interactions
```

### World — the shared state container

`World.java` (in `Injectors/`) is the central dependency injection object. Every class that needs shared state receives a `World` instance in its constructor. `Initialisation.java` (in `stepdefs/vol/`) wires up all Journey and API-call objects at test startup.

```java
// Pattern used everywhere
public class SomeJourney {
    private World world;
    public SomeJourney(World world) { this.world = world; }
}
```

### DVSA internal libraries (not in this repo)

- **`vol-api-calls`** (`apiCalls.*`) — creates licences, applications, users, and grants via API before UI steps run
- **`active-support`** — provides `Browser` (WebDriver management), `Properties` (system property reader), `MailPit` (email retrieval), and `EnvironmentType`

### Test domains

Tests are split into two contexts reflected throughout the codebase:

- **SelfServe** — external operator-facing portal (`Journeys/licence/SelfServeNavigation`, `pageObjects/external/`)
- **Internal** — DVSA caseworker portal (`Journeys/licence/InternalNavigation`, `pageObjects/internal/`)
- **Permits** — separate ECMT/bilateral/multilateral permit journeys (`Journeys/permits/`, `pageObjects/external/pages/`)

### Page object base classes

- `BasePage` (extends `DriverUtils`) — all page objects and step-def classes inherit from this; provides `findElement`, `waitAndClick`, `isTextPresent`, etc.
- `pageObjects/internal/BaseModel.java` — base for internal page objects
- `pageObjects/external/pages/baseClasses/BasePermitPage.java` — base for permit page objects

### Parallelism & retry

Tests run with 5 forks and 8 threads (`maven-surefire-plugin`). Failed scenarios are automatically extracted and re-run via the `retry-failed` Maven profile, driven by `maven-antrun-plugin` post-integration-test.

## Key Conventions

### Feature file tags

Tags are declared at the **top of the feature file** (not per scenario). Common tags used to filter CI runs:

| Tag | Purpose |
|-----|---------|
| `@smoke` | Smoke suite |
| `@FullRegression` | Full regression |
| `@int_regression` | Internal regression |
| `@APIsmoke` | API-backed smoke |
| `@accessibility` | Accessibility checks |
| `@printAndSign` | Print-and-sign flow |

### Test setup pattern

Most scenarios rely on API-created state. The step `"I have a submitted "<operator>" "<licenceType>" application"` calls `world.licenceCreation.createApplication(...)` which uses `vol-api-calls` to create a licence via API — no UI is used for setup.

### Environment configuration

Config is loaded by `active-support`'s `Configuration` class using the `env` system property. Environment-specific URLs, credentials, and settings are resolved automatically. `MailPit` is used to retrieve OTPs and reset links in email-based test flows.

### CI execution

Workflows in `.github/workflows/` submit tests to **AWS Batch** (not GitHub-hosted runners). The `run.sh` script is the container entry point. Reports are uploaded to S3 and retrieved post-run. The pass threshold is **85%** (`fail-on-test-failures: true, pass-percentage: 85`).

### Adding new tests

1. Add a `.feature` file under `src/test/resources/org/dvsa/testing/framework/features/<domain>/`
2. Add step definition class under `src/test/java/.../stepdefs/<domain>/` extending `BasePage`
3. Add journey helper under `src/test/java/.../Journeys/<domain>/` taking `World` in constructor
4. Register the new journey in `Initialisation.java` and declare the field in `World.java`
