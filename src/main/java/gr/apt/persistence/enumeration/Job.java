package gr.apt.persistence.enumeration;

import java.util.List;

public enum Job {
    PROGRAMMER,
    PROJECT_MANAGER,
    SECRETARY,
    ACCOUNTANT;

    public static List<String> getValuesList(){
        return List.of(Job.PROGRAMMER.toString(),
                Job.PROJECT_MANAGER.toString(),
                Job.SECRETARY.toString(),
                Job.ACCOUNTANT.toString());
    }
}
