package gr.apt.lms.persistence.enumeration

enum class LeaveType {
    PAID_LEAVE, UNPAID_LEAVE, SICKNESS_LEAVE, MATERNITY_LEAVE, MARRIAGE_LEAVE, PARENTAL_LEAVE;

    companion object {
        @JvmStatic
        val valuesList: List<String>
            get() = java.util.List.of(
                PAID_LEAVE.toString(),
                UNPAID_LEAVE.toString(),
                SICKNESS_LEAVE.toString(),
                MATERNITY_LEAVE.toString(),
                MARRIAGE_LEAVE.toString(),
                PARENTAL_LEAVE.toString()
            )
    }
}