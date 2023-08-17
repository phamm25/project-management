package edu.union.csc260.project2gpta.TestData;
import java.time.LocalDate;

public class BacklogItem {
    public static edu.union.csc260.project2gpta.model.BacklogItem createTestBacklogItem(){
        return new edu.union.csc260.project2gpta.model.BacklogItem(
                "Backlog Item Name",
                "Low Priority",
                "Backlog Item Description\n- Line 1\n- Line 2\n- etc...",
                LocalDate.of(2023,5,6),
                LocalDate.of(2023,5,8)
        );
    }


}
