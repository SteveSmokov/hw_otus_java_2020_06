package ru.otus.diplom.utils;

public final class TaskUtil {

    /**
     * Изменение значения приоритета для системы JIRA
     *
     * @param p - значение приоритета в заявке ЕИС порта
     * @return - значение приоритета в JIRA
     */
    public static String getIDPriority(Integer p) {
        switch (p) {
            case 0:
                return "4";//Low
            case 1:
                return "3";//Medium
            case 2:
                return "2";//High
            default:
                return "";
        }
    }

    /**
     * Изменение значения приоритета для клиентской системы
     *
     * @param p - значение приоритета в заявке JIRA
     * @return - значение приоритета в ЕИС порта
     */
    public static int getIDPriority(String p) {
        switch (p) {
            case "4":
                return 0;//Low
            case "3":
                return 1;//Medium
            case "2":
                return 2;//High
            default:
                return 0;
        }
    }

    /**
     * Изменение значения ID приложения для системы JIRA
     *
     * @param appID - значение в заявке ЕИС порта
     * @return - значение для JIRA
     */
    public static String getAppID(long appID) {
        if (appID == 2L) {
            return "13414";//CONTRACTS
        } else if (appID == 4L) {
            return null;//OILTERMINAL
        } else if (appID == 6L) {
            return "13416";//WCLI
        } else if (appID == 7L) {
            return "13417";//GENCARGOES
        } else if (appID == 11L) {
            return null;//1С
        } else if (appID == 14L) {
            return "13415";//DISPATCHER
        } else if (appID == 31L) {
            return "13408";//SAP
        } else if (appID == 16L) {
            return null;//REPORTS
        } else if (appID == 17L) {
            return "13413";//MGM_VSB
        } else if (appID == 18L) {
            return null;//PASSES_MNG
        } else if (appID == 26L) {
            return null;//AQUAGIS
        } else if (appID == 100L) {
            return "13410";//BOD
        } else if (appID == 688L) {
            return "13407";//CCLI
        } else if (appID == 689L) {
            return "13412";//LORRYS
        } else if (appID == 691L) {
            return null;//GENREPORTS
        } else if (appID == 694L) {
            return null;//ИСПС
        } else if (appID == 2305L) {
            return null;//WEB-PORTAL_OUT
        } else if (appID == 2306L) {
            return null;//WEB-PORTAL_IN
        } else if (appID == 2211L) {
            return "13409";//NSIADM
        }
        return null;//
    }

    /**
     * Изменение значения ID приложения для системы ЕИС порта
     *
     * @param appID - значение в заявке JIRA
     * @return - значение для ЕИС порта
     */
    public static Long getAppID(String appID) {
        switch (appID) {
            case "13414":
                return 2L;//CONTRACTS
            case "13416":
                return 6L;//WCLI
            case "13417":
                return 7L;//GENCARGOES
            case "13415":
                return 14L;//DISPATCHER
            case "13408":
                return 31L;//SAP
            case "13413":
                return 17L;//MGM_VSB
            case "13410":
                return 100L;//BOD
            case "13407":
                return 688L;//CCLI
            case "13412":
                return 689L;//LORRYS
            case "13409":
                return 2211L;//NSIADM
            default:
                return null;//
        }
    }

    public static String getComponentID(Long prsID) {
        if (prsID == 4798481L) {
            return "11413";
        } else if (prsID == 4798468L) {
            return "11411";
        } else return null;
    }

    public static Long getPrsID(String componentID) {
        if (componentID.equals("11413")) return 4798481L;
        else if (componentID.equals("11411")) return 4798468L;
        else return null;
    }
}
