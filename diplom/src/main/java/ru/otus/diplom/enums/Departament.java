package ru.otus.diplom.enums;

public enum Departament {
    SUPPORT(1L, null),
    DEVELOPMENT(4798468L, "11411"),
    ADMINISTRATION(4798481L, "11413");

    private Long value;
    private String jiraValue;

    Departament(Long value, String jiraValue) {
        this.value = value;
        this.jiraValue = jiraValue;
    }

    public Long getValue() {
        return value;
    }

    public String getJiraValue() {
        return jiraValue;
    }

    public static Departament byValue(Long value){
        for (Departament priority: values()){
            if (priority.getValue().equals(value)) return priority;
        }
        return null;
    }

    public static Departament byJiraValue(String jValue){
        if ((jValue != null) && (!jValue.isEmpty())) {
            for (Departament departament : values()) {
                if (departament.getJiraValue().equals(jValue)) return departament;
            }
        }
        return null;
    }
}
