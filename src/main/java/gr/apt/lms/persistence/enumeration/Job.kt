package gr.apt.lms.persistence.enumeration

enum class Job {
    PROGRAMMER, PROJECT_MANAGER, SECRETARY, ACCOUNTANT;

    companion object {
        @JvmStatic
        val valuesList: List<String>
            get() = listOf(
                PROGRAMMER.toString(),
                PROJECT_MANAGER.toString(),
                SECRETARY.toString(),
                ACCOUNTANT.toString()
            )
    }
}