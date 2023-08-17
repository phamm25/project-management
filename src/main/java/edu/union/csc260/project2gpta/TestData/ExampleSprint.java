package edu.union.csc260.project2gpta.TestData;

import java.time.LocalDate;

public class ExampleSprint {
    public static edu.union.csc260.project2gpta.model.Sprint createTestSprint(){
        return new edu.union.csc260.project2gpta.model.Sprint(
                "Example Sprint Name",
                LocalDate.of(2023,5,6),
                LocalDate.of(2023,5,8)
        );
    }
}
