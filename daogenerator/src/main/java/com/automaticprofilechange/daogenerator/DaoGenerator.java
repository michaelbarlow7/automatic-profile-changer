package com.automaticprofilechange.daogenerator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by michael on 1/05/16.
 */
public class DaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.mbarlow.automaticprofilechanger.model");
        schema.enableKeepSectionsByDefault();

        Entity alarm = schema.addEntity("Alarm");
        alarm.addIdProperty().autoincrement();
        alarm.addStringProperty("name");
        // Availability is signified by a bit array.
        // The first bit is whether it's enabled or not.
        // The rest of the bits signify whether it's enabled on the days of the week
        // from Sunday through to Saturday.
        alarm.addByteProperty("enabled").notNull();
        alarm.addIntProperty("startTime"); // A number in the range of 0 - 1439
        alarm.addIntProperty("endTime"); // A number in the range of 0 - 1439
        alarm.addStringProperty("profile").notNull(); // The profile stored on the phone

        new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, "../app/src/main/java");
    }
}
