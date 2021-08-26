package gr.apt.persistence.enumeration;

import java.util.List;

public enum LeaveType {
    PAYED_LEAVE,
    UNPAYED_LEAVE;

    public static List<String> getValuesList(){
        return List.of(LeaveType.PAYED_LEAVE.toString(),
                LeaveType.UNPAYED_LEAVE.toString());
    }
}
