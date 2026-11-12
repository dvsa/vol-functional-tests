package org.dvsa.testing.framework.Journeys.licence;

import apiCalls.Utils.volBuilders.VehiclesBuilder;
import org.apache.commons.io.FileUtils;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PSVVehicleManagementJourney extends BasePage {
    private final World world;
    private final List<AddedVehicle> addedVehicles = new ArrayList<>();
    private List<ExportedVehicle> exportedVehicles = List.of();
    private boolean vehicleListOpened;
    private AddedVehicle removedVehicle;
    private AddedVehicle remainingVehicle;

    public PSVVehicleManagementJourney(World world) {
        this.world = world;
    }

    public void addVehicle(String makeModel) {
        openVehicleList();
        AddedVehicle vehicle = new AddedVehicle(generateUniqueRegistrationMark(), makeModel);
        openAddVehicleForm();
        waitAndEnterText("vrm", SelectorType.ID, vehicle.registrationMark());
        waitAndEnterText("data[makeModel]", SelectorType.ID, vehicle.makeModel());
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTitleToBePresent("Vehicle details");
        addedVehicles.add(vehicle);
        assertVehicleIsPresent(vehicle);
    }

    public void addTwoVehicles(String firstMakeModel, String secondMakeModel) {
        addVehicle(firstMakeModel);
        addVehicle(secondMakeModel);
    }

    public void verifyLastAddedVehiclePersistsAfterReload() {
        assertFalse(addedVehicles.isEmpty(), "A PSV vehicle must be added before persistence can be verified");
        refreshPage();
        waitForTitleToBePresent("Vehicle details");
        assertVehicleIsPresent(addedVehicles.get(addedVehicles.size() - 1));
    }

    public void removeFirstAddedVehicle() {
        assertEquals(2, addedVehicles.size(), "Exactly two scenario-created PSV vehicles are required for removal");
        openVehicleList();
        removedVehicle = addedVehicles.get(0);
        remainingVehicle = addedVehicles.get(1);

        WebElement row = findVehicleRow(removedVehicle.registrationMark());
        assertNotNull(row, "The scenario-created PSV vehicle should be available for removal");
        row.findElement(By.xpath("./td[@data-heading='Remove']//button")).click();
        waitForElementToBePresent("//form[@id='generic-delete-confirmation']");
        waitAndClick("//form[@id='generic-delete-confirmation']//button[@name='form-actions[submit]']", SelectorType.XPATH);
        waitForTitleToBePresent("Vehicle details");
    }

    public void verifyRemovedVehicleIsAbsentAndOtherVehicleRemains() {
        assertNotNull(removedVehicle, "A scenario-created PSV vehicle must be removed before its absence is verified");
        assertNotNull(remainingVehicle, "An unaffected scenario-created PSV vehicle must be retained for verification");
        refreshPage();
        waitForTitleToBePresent("Vehicle details");

        assertTrue(findVehicleRow(removedVehicle.registrationMark()) == null,
                "The removed PSV vehicle should be absent after reloading the licence");
        assertVehicleIsPresent(remainingVehicle);
    }

    public void exportVehicleList() throws Exception {
        assertFalse(addedVehicles.isEmpty(), "A scenario-created PSV vehicle is required before exporting the list");
        openVehicleList();
        waitAndClick("more-actions__button__container", SelectorType.ID);
        waitAndClick("export", SelectorType.ID);

        Path downloadDirectory = Path.of(
                System.getProperty("user.dir"),
                "target",
                "psv-vehicle-downloads",
                addedVehicles.get(addedVehicles.size() - 1).registrationMark());
        try {
            Path csv = UniversalActions.downloadCsv(downloadDirectory);
            exportedVehicles = readExportedVehicles(csv);
        } finally {
            FileUtils.deleteDirectory(downloadDirectory.toFile());
        }
    }

    public void verifyExportContainsLastAddedVehicle() {
        assertFalse(addedVehicles.isEmpty(), "A scenario-created PSV vehicle is required before export can be verified");
        AddedVehicle expectedVehicle = addedVehicles.get(addedVehicles.size() - 1);
        assertTrue(exportedVehicles.stream().anyMatch(exportedVehicle ->
                        exportedVehicle.registrationMark().equals(expectedVehicle.registrationMark())
                                && exportedVehicle.makeModel().equals(expectedVehicle.makeModel())),
                "The PSV vehicle export should contain the added vehicle's registration and make/model");
    }

    private void openVehicleList() {
        if (!vehicleListOpened) {
            world.dvlaJourney.navigateToManageVehiclesPage("licence");
            waitForTitleToBePresent("Vehicle details");
            vehicleListOpened = true;
        }
    }

    private void openAddVehicleForm() {
        waitAndClick("more-actions__button__container", SelectorType.ID);
        waitAndClick("addSmall", SelectorType.ID);
        waitForTitleToBePresent("Add vehicle");
    }

    private String generateUniqueRegistrationMark() {
        while (true) {
            String registrationMark = VehiclesBuilder.generateRandomVRM();
            boolean alreadyAdded = addedVehicles.stream()
                    .anyMatch(vehicle -> vehicle.registrationMark().equals(registrationMark));
            if (!alreadyAdded) {
                return registrationMark;
            }
        }
    }

    private List<WebElement> currentVehicleRows() {
        return findElements(
                "//form[@id='application_vehicle-safety_vehicle-psv']//tbody/tr[td[@data-heading='Vehicle registration mark']]",
                SelectorType.XPATH);
    }

    private WebElement findVehicleRow(String registrationMark) {
        return currentVehicleRows().stream()
                .filter(row -> row.findElement(By.xpath("./td[@data-heading='Vehicle registration mark']"))
                        .getText().trim().equals(registrationMark))
                .findFirst()
                .orElse(null);
    }

    private void assertVehicleIsPresent(AddedVehicle expectedVehicle) {
        WebElement row = findVehicleRow(expectedVehicle.registrationMark());
        assertNotNull(row, "The added PSV vehicle should appear in the current vehicle list");
        assertEquals(expectedVehicle.makeModel(),
                row.findElement(By.xpath("./td[@data-heading='Make / model']")).getText().trim(),
                "The current vehicle list should show the added PSV vehicle's make/model");
    }

    private List<ExportedVehicle> readExportedVehicles(Path csv) throws Exception {
        List<ExportedVehicle> vehicles = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(csv, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
            for (CSVRecord record : parser) {
                vehicles.add(new ExportedVehicle(
                        record.get("Vehicle registration mark").trim(),
                        record.get("Make / model").trim()));
            }
        }
        return vehicles;
    }

    private record AddedVehicle(String registrationMark, String makeModel) {
    }

    private record ExportedVehicle(String registrationMark, String makeModel) {
    }
}
