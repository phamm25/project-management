package edu.union.csc260.project2gpta.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses
({
        BacklogItemTests.class,
        TaskTests.class,
        SprintTests.class,
        GroupTests.class,
        GroupManagerTests.class,
        GroupMemberTests.class,
        UserFactoryTests.class
})

public class ModelTestSuite {

}
