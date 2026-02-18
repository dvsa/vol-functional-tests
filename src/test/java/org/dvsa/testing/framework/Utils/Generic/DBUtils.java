package org.dvsa.testing.framework.Utils.Generic;

import activesupport.aws.s3.SecretsManager;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import activesupport.system.Properties;
import org.dvsa.testing.framework.Global.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;

import static activesupport.database.DBUnit.checkResult;

public class DBUtils {

    private static final Logger LOGGER = LogManager.getLogger(DBUtils.class);
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    static {
        try {
            dbUsername = SecretsManager.getSecretValue("dbUsername");
            dbPassword = SecretsManager.getSecretValue("dbPassword");
            LOGGER.info("Database credentials loaded from Secrets Manager");
        } catch (Exception e) {
            LOGGER.error("Failed to get DB credentials", e);
            dbUsername = "fallback_user";
            dbPassword = "fallback_password";
        }
    }
    private World world;

    public DBUtils(World world) {
        this.world = world;
    }

    public int getFirstPsvDiscNumber(String licenceId, Configuration configuration) throws UnsupportedDatabaseDriverException, SQLException {
        String sqlStatement = String.format(
                "SELECT disc_no FROM psv_disc WHERE licence_id = '%s' AND ceased_date IS NULL;", licenceId);
        Properties.set("dbUsername", configuration.config.getString("dbUsername"));
        Properties.set("dbPassword", configuration.config.getString("dbPassword"));
        ResultSet result = checkResult(sqlStatement);
        result.first();
        return result.getInt(1);
    }

    public static HashMap getLicenceDetails() {
        HashMap licenceDetails = new HashMap();

        String env = System.getProperty("env", "qa");
        String currentDbUrl = getDatabaseUrl(env);

        try (Connection con = DriverManager.getConnection(currentDbUrl, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(licenceSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet result = stmt.executeQuery()) {
            if (result.first()) {
                licenceDetails.put("Licence number", result.getString("Licence number"));
                licenceDetails.put("Organisation type", result.getString("Organisation type"));
                licenceDetails.put("Licence type", result.getString("Licence type"));
                licenceDetails.put("Licence status", result.getString("Licence status"));
                licenceDetails.put("Traffic Area", result.getString("Traffic Area"));
                licenceDetails.put("Goods or PSV", result.getString("Goods or PSV"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return licenceDetails;
    }

    private static String getDatabaseUrl(String env) {
        String baseParams = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        return switch (env.toLowerCase()) {
            case "qa", "qualityassurance" ->
                    "jdbc:mysql://olcsdb-rds.qa.olcs.dev-dvsacloud.uk:3306/OLCS_RDS_OLCSDB" + baseParams;
            case "int", "integration" ->
                    "jdbc:mysql://olcsdb-rds.int.olcs.dev-dvsacloud.uk:3306/OLCS_RDS_OLCSDB" + baseParams;
            case "prep", "preproduction" ->
                    "jdbc:mysql://olcsdb-rds.prep.olcs.dev-dvsacloud.uk:3306/OLCS_RDS_OLCSDB" + baseParams;
            default -> {
                LOGGER.warn("Unknown environment: {}. Defaulting to QA", env);
                yield "jdbc:mysql://olcsdb-rds.qa.olcs.dev-dvsacloud.uk:3306/OLCS_RDS_OLCSDB" + baseParams;
            }
        };
    }

    public static String licenceSql() {
        return "SELECT l.lic_no AS \"Licence number\", " +
                "ro.description AS \"Organisation type\", " +
                "rlt.description AS \"Licence type\", " +
                "rls.description AS \"Licence status\", " +
                "ta.name AS \"Traffic Area\", " +
                "rgp.description AS \"Goods or PSV\" " +
                "FROM licence l " +
                "JOIN organisation o ON l.organisation_id = o.id " +
                "JOIN ref_data ro ON o.type = ro.id " +
                "JOIN ref_data rlt ON l.licence_type = rlt.id " +
                "JOIN ref_data rls ON l.status = rls.id " +
                "JOIN traffic_area ta ON l.traffic_area_id = ta.id " +
                "JOIN ref_data rgp ON l.goods_or_psv = rgp.id " +
                "WHERE l.deleted_date IS NULL " +
                "AND l.status NOT IN ('lsts_cancelled', 'lsts_not_submitted', 'lsts_unlicenced', 'lsts_withdrawn') " +
                "AND l.created_on > DATE_SUB(SYSDATE(), INTERVAL 1 YEAR);";
    }


}
