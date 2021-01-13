package ru.otus.diplom.enums;

/**
 * Типы приоритетов для заявки с набором значений для клиентской заявки
 * и для заявки Jira
 */
public enum TaskPriority {
    Low(0, "4"),
    Medium(1, "3"),
    High(2, "2");

    private Integer value;
    private String jiraValue;

    TaskPriority(Integer value, String jiraValue) {
        this.value = value;
        this.jiraValue = jiraValue;
    }

    public Integer getValue() {
        return value;
    }

    public String getJiraValue() {
        return jiraValue;
    }

    public static TaskPriority byValue(Integer value){
        for (TaskPriority priority: values()){
            if (priority.getValue().equals(value)) return priority;
        }
        return Low;
    }

    public static TaskPriority byJiraValue(String jValue){
        for (TaskPriority priority: values()){
            if (priority.getJiraValue().equals(jValue)) return priority;
        }
        return Low;
    }
}
