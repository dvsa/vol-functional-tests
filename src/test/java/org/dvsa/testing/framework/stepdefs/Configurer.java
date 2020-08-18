package org.dvsa.testing.framework.stepdefs;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;

import java.util.Locale;
import java.util.Map;

public class Configurer implements TypeRegistryConfigurer {

    @Override
    public void configureTypeRegistry(TypeRegistry registry) {

        registry.defineDataTableType(new DataTableType(Income.class, new TableEntryTransformer<Income>() {
            @Override
            public Income transform(Map<String, String> entry) {
                return new Income(entry.get("name"),entry.get("amount"),entry.get("frequency"));
            }
        }));
    }

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

}
