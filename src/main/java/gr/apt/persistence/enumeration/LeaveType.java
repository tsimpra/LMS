package gr.apt.persistence.enumeration;

import java.util.List;

public enum LeaveType {
    PAID_LEAVE,
    UNPAID_LEAVE,
    SICKNESS_LEAVE,
    MATERNITY_LEAVE,
    MARRIAGE_LEAVE,
    PARENTAL_LEAVE;

    public static List<String> getValuesList(){
        return List.of(
                LeaveType.PAID_LEAVE.toString(),
                LeaveType.UNPAID_LEAVE.toString(),
                LeaveType.SICKNESS_LEAVE.toString(),
                LeaveType.MATERNITY_LEAVE.toString(),
                LeaveType.MARRIAGE_LEAVE.toString(),
                LeaveType.PARENTAL_LEAVE.toString()
        );
    }
}
