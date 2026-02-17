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
             PreparedStatement stmt = con.prepareStatement(licenceSql());
             ResultSet result = stmt.executeQuery()) {
            while (result.first()) {
                licenceDetails.put("Licence number", result.getString("Licence number"));
                licenceDetails.put("Organisation", result.getString("Organisation"));
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
        return  "select l.lic_no as \"Licence number\", ro.description as \"Organisation\", rlt.description as \"Licence type\", rls.description as \"Licence status\", ta.name as \"Traffic Area\", rgp.description as \"Goods or PSV\"" +
                "from licence l join organisation o on l.organisation_id = o.id" +
                "join ref_data ro on o.type = ro.id" +
                "join ref_data rlt on l.licence_type = rlt.id" +
                "join ref_data rls on l.status = rls.id" +
                "join traffic_area ta on l.traffic_area_id = ta.id" +
                "join ref_data rgp on l.goods_or_psv = rgp.id" +
                "where l.deleted_date is NULL" +
                "and l.status not in ('lsts_cancelled', 'lsts_not_submitted', 'lsts_unlicenced', 'lsts_withdrawn')" +
                "and l.created_on > DATE_SUB(SYSDATE(), INTERVAL 1 YEAR;);";
    }


}
