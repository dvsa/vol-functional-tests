package org.dvsa.testing.framework.Utils.Generic;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import activesupport.system.Properties;
import org.dvsa.testing.framework.Global.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;

import static activesupport.database.DBUnit.checkResult;

public class DBUtils {

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
}
