package ru.otus.diplom.enums;

/**
 * Справочник для заявки с набором значений для клиентской заявки
 * и для заявки Jira(используется поле с версиями)
 */
public enum Application {
    CONTRACTS(2L, "13414"),
    OILTERMINAL(4L, null),
    WCLI(6L, "13416"),
    GENCARGOES(7L, "13417"),
    С1(11L, null),//1C
    DISPATCHER(14L, "13415"),
    SAP(31L, "13408"),
    REPORTS(16L, null),
    MGM_VSB(17L, "13413"),
    PASSES_MNG(18L, null),
    AQUAGIS(26L, null),
    BOD(100L, "13410"),
    CCLI(688L, "13407"),
    LORRYS(689L, "13412"),
    GENREPORTS(691L, null),
    ISPS(694L, null),
    WEB_PORTAL_OUT(2305L, null),
    WEB_PORTAL_IN(2306L, null),
    NSIADM(2211L, "13409");

    private Long value;
    private String jiraValue;

    Application(Long value, String jiraValue) {
        this.value = value;
        this.jiraValue = jiraValue;
    }

    public Long getValue() {
        return value;
    }

    public String getJiraValue() {
        return jiraValue;
    }

    public static Application byValue(Long value){
        for (Application application: values()){
            if (application.getValue().equals(value)) return application;
        }
        return null;
    }

    public static Application byJiraValue(String jValue){
        if ((jValue != null) && (!jValue.isEmpty())) {
            for (Application application : values()) {
                if (application.getJiraValue().equals(jValue)) return application;
            }
        }
        return null;
    }
}
