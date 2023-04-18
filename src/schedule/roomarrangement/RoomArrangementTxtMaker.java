package schedule.roomarrangement;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class RoomArrangementTxtMaker {
        public void txtMaker(CustomHashMapForDisplayingRoomArrangement<Integer, String, Integer, String, String, Integer> timeTableCustomDisplayMap) {
            // This will be replaced with a database connection in time. Placeholder.
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("timetable.txt"), StandardCharsets.UTF_8))) {
                for (Integer key : timeTableCustomDisplayMap.keySet()) {
                    String valueString = timeTableCustomDisplayMap.getString(key);
                    if (valueString != null) {
                        writer.write(key + ": " + valueString);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }