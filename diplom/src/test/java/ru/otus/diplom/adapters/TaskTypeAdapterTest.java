package ru.otus.diplom.adapters;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.diplom.enums.TaskPriority;
import ru.otus.diplom.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@DisplayName("Тестирование адаптера конвертирования заявки в JSON и обратно")
class TaskTypeAdapterTest {
    private final Gson gson = new Gson();

    private final String jsonStr = "{" +
                "\"fields\":{" +
                    "\"project\":{\"key\":\"OFAMPU\"},"+
                    "\"summary\":\"Title\","+
                    "\"description\":\"Text\","+
                    "\"issuetype\":{\"id\":\"10002\"},"+
                    "\"priority\":{\"id\":\"2\"},"+
                    "\"components\":[{\"id\":\"11413\"}],"+
                    "\"customfield_11101\":\"1\","+
                    "\"customfield_11400\":\"Test personal\","+
                    "\"customfield_11201\":{\"id\":\"10404\"}"+
                "}" +
            "}";

    private final String jiraJson = "{\n" +
            "            \"expand\": \"operations,versionedRepresentations,editmeta,changelog,renderedFields\",\n" +
            "            \"id\": \"66488\",\n" +
            "            \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/issue/66488\",\n" +
            "            \"key\": \"OFAMPU-59\",\n" +
            "            \"fields\": {\n" +
            "                \"issuetype\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/issuetype/10002\",\n" +
            "                    \"id\": \"10002\",\n" +
            "                    \"description\": \"Задание для выполнения.\",\n" +
            "                    \"iconUrl\": \"https://jira.ppl33-35.com:8443/secure/viewavatar?size=xsmall&avatarId=10318&avatarType=issuetype\",\n" +
            "                    \"name\": \"Задача\",\n" +
            "                    \"subtask\": false,\n" +
            "                    \"avatarId\": 10318\n" +
            "                },\n" +
            "                \"timespent\": 1800,\n" +
            "                \"customfield_10030\": null,\n" +
            "                \"customfield_10031\": null,\n" +
            "                \"project\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/project/11801\",\n" +
            "                    \"id\": \"11801\",\n" +
            "                    \"key\": \"OFAMPU\",\n" +
            "                    \"name\": \"ЕИС ОФ АМПУ\",\n" +
            "                    \"projectTypeKey\": \"software\",\n" +
            "                    \"avatarUrls\": {\n" +
            "                        \"48x48\": \"https://jira.ppl33-35.com:8443/secure/projectavatar?pid=11801&avatarId=13401\",\n" +
            "                        \"24x24\": \"https://jira.ppl33-35.com:8443/secure/projectavatar?size=small&pid=11801&avatarId=13401\",\n" +
            "                        \"16x16\": \"https://jira.ppl33-35.com:8443/secure/projectavatar?size=xsmall&pid=11801&avatarId=13401\",\n" +
            "                        \"32x32\": \"https://jira.ppl33-35.com:8443/secure/projectavatar?size=medium&pid=11801&avatarId=13401\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"fixVersions\": [],\n" +
            "                \"customfield_11001\": null,\n" +
            "                \"customfield_11200\": null,\n" +
            "                \"aggregatetimespent\": 1800,\n" +
            "                \"resolution\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/resolution/10000\",\n" +
            "                    \"id\": \"10000\",\n" +
            "                    \"description\": \"Для этой проблемы работа завершена.\",\n" +
            "                    \"name\": \"Готово\"\n" +
            "                },\n" +
            "                \"customfield_11201\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/customFieldOption/10404\",\n" +
            "                    \"value\": \"По системе порта\",\n" +
            "                    \"id\": \"10404\",\n" +
            "                    \"disabled\": false\n" +
            "                },\n" +
            "                \"customfield_11400\": \"ДЯТЛОВ С. С.\",\n" +
            "                \"customfield_10500\": null,\n" +
            "                \"customfield_10104\": null,\n" +
            "                \"customfield_10105\": null,\n" +
            "                \"customfield_10029\": null,\n" +
            "                \"resolutiondate\": \"2019-10-10T10:16:16.000+0300\",\n" +
            "                \"workratio\": -1,\n" +
            "                \"lastViewed\": null,\n" +
            "                \"watches\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/issue/OFAMPU-59/watchers\",\n" +
            "                    \"watchCount\": 4,\n" +
            "                    \"isWatching\": false\n" +
            "                },\n" +
            "                \"created\": \"2019-10-10T10:13:05.000+0300\",\n" +
            "                \"customfield_12200\": null,\n" +
            "                \"customfield_10022\": null,\n" +
            "                \"customfield_10100\": null,\n" +
            "                \"customfield_10023\": [],\n" +
            "                \"priority\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/priority/3\",\n" +
            "                    \"iconUrl\": \"https://jira.ppl33-35.com:8443/images/icons/priorities/low.svg\",\n" +
            "                    \"name\": \"Medium\",\n" +
            "                    \"id\": \"3\"\n" +
            "                },\n" +
            "                \"customfield_10101\": null,\n" +
            "                \"customfield_10024\": null,\n" +
            "                \"customfield_10102\": null,\n" +
            "                \"customfield_10025\": null,\n" +
            "                \"customfield_12600\": null,\n" +
            "                \"labels\": [],\n" +
            "                \"customfield_10026\": null,\n" +
            "                \"customfield_11700\": null,\n" +
            "                \"customfield_10016\": null,\n" +
            "                \"customfield_10017\": null,\n" +
            "                \"customfield_11900\": null,\n" +
            "                \"timeestimate\": 0,\n" +
            "                \"aggregatetimeoriginalestimate\": null,\n" +
            "                \"versions\": [],\n" +
            "                \"customfield_11901\": null,\n" +
            "                \"issuelinks\": [],\n" +
            "                \"assignee\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/user?username=P.Ignatov\",\n" +
            "                    \"name\": \"P.Ignatov\",\n" +
            "                    \"key\": \"p.ignatov\",\n" +
            "                    \"emailAddress\": \"p.ignatov@ppl33-35.com\",\n" +
            "                    \"avatarUrls\": {\n" +
            "                        \"48x48\": \"https://jira.ppl33-35.com:8443/secure/useravatar?ownerId=p.ignatov&avatarId=11405\",\n" +
            "                        \"24x24\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=small&ownerId=p.ignatov&avatarId=11405\",\n" +
            "                        \"16x16\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=xsmall&ownerId=p.ignatov&avatarId=11405\",\n" +
            "                        \"32x32\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=medium&ownerId=p.ignatov&avatarId=11405\"\n" +
            "                    },\n" +
            "                    \"displayName\": \"Ігнатов Петро Євгенович\",\n" +
            "                    \"active\": true,\n" +
            "                    \"timeZone\": \"Europe/Kiev\"\n" +
            "                },\n" +
            "                \"updated\": \"2019-10-10T10:16:16.000+0300\",\n" +
            "                \"status\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/status/10001\",\n" +
            "                    \"description\": \"\",\n" +
            "                    \"iconUrl\": \"https://jira.ppl33-35.com:8443/\",\n" +
            "                    \"name\": \"Готово\",\n" +
            "                    \"id\": \"10001\",\n" +
            "                    \"statusCategory\": {\n" +
            "                        \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/statuscategory/3\",\n" +
            "                        \"id\": 3,\n" +
            "                        \"key\": \"done\",\n" +
            "                        \"colorName\": \"green\",\n" +
            "                        \"name\": \"Выполнено\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"components\": [\n" +
            "                    {\n" +
            "                        \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/component/11413\",\n" +
            "                        \"id\": \"11413\",\n" +
            "                        \"name\": \"SA\",\n" +
            "                        \"description\": \"Отдел администрирования\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"timeoriginalestimate\": null,\n" +
            "                \"description\": \"Дать возможность рассылки писем на ампу, омтп, стивидоров Здоровцовой Анастасии  с  адреса zam@ods.uspa.gov.ua                                                                               IP: 192.168.4.74\",\n" +
            "                \"customfield_11101\": \"536344\",\n" +
            "                \"customfield_11300\": null,\n" +
            "                \"customfield_11500\": null,\n" +
            "                \"customfield_10015\": \"1|i042lb:\",\n" +
            "                \"customfield_12700\": null,\n" +
            "                \"customfield_10800\": null,\n" +
            "                \"aggregatetimeestimate\": 0,\n" +
            "                \"summary\": \"ДАТЬ ВОЗМОЖНОСТЬ РАССЫЛКИ ПИСЕМ НА АМПУ, ОМТП, СТИВИДОРОВ ЗДОРОВЦОВОЙ АНАСТАСИИ  С  АДРЕСА ZAM@ODS.U\",\n" +
            "                \"creator\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/user?username=jigit\",\n" +
            "                    \"name\": \"jigit\",\n" +
            "                    \"key\": \"jigit\",\n" +
            "                    \"emailAddress\": \"zero@ppl33-35.net\",\n" +
            "                    \"avatarUrls\": {\n" +
            "                        \"48x48\": \"https://jira.ppl33-35.com:8443/secure/useravatar?avatarId=10349\",\n" +
            "                        \"24x24\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=small&avatarId=10349\",\n" +
            "                        \"16x16\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=xsmall&avatarId=10349\",\n" +
            "                        \"32x32\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=medium&avatarId=10349\"\n" +
            "                    },\n" +
            "                    \"displayName\": \"Robot\",\n" +
            "                    \"active\": true,\n" +
            "                    \"timeZone\": \"Europe/Kiev\"\n" +
            "                },\n" +
            "                \"subtasks\": [],\n" +
            "                \"reporter\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/user?username=jigit\",\n" +
            "                    \"name\": \"jigit\",\n" +
            "                    \"key\": \"jigit\",\n" +
            "                    \"emailAddress\": \"zero@ppl33-35.net\",\n" +
            "                    \"avatarUrls\": {\n" +
            "                        \"48x48\": \"https://jira.ppl33-35.com:8443/secure/useravatar?avatarId=10349\",\n" +
            "                        \"24x24\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=small&avatarId=10349\",\n" +
            "                        \"16x16\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=xsmall&avatarId=10349\",\n" +
            "                        \"32x32\": \"https://jira.ppl33-35.com:8443/secure/useravatar?size=medium&avatarId=10349\"\n" +
            "                    },\n" +
            "                    \"displayName\": \"Robot\",\n" +
            "                    \"active\": true,\n" +
            "                    \"timeZone\": \"Europe/Kiev\"\n" +
            "                },\n" +
            "                \"customfield_12100\": \"{summaryBean=com.atlassian.jira.plugin.devstatus.rest.SummaryBean@7741b759[summary={pullrequest=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@b9c9344[overall=PullRequestOverallBean{stateCount=0, state='OPEN', details=PullRequestOverallDetails{openCount=0, mergedCount=0, declinedCount=0}},byInstanceType={}], build=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@220ef778[overall=com.atlassian.jira.plugin.devstatus.summary.beans.BuildOverallBean@4de756ce[failedBuildCount=0,successfulBuildCount=0,unknownBuildCount=0,count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], review=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@654f9f4e[overall=com.atlassian.jira.plugin.devstatus.summary.beans.ReviewsOverallBean@46214eb8[stateCount=0,state=<null>,dueDate=<null>,overDue=false,count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], deployment-environment=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@1341d433[overall=com.atlassian.jira.plugin.devstatus.summary.beans.DeploymentOverallBean@6467a1b3[topEnvironments=[],showProjects=false,successfulCount=0,count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], repository=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@7dcbf79e[overall=com.atlassian.jira.plugin.devstatus.summary.beans.CommitOverallBean@31532211[count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], branch=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@2cae9844[overall=com.atlassian.jira.plugin.devstatus.summary.beans.BranchOverallBean@759dde5b[count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}]},errors=[],configErrors=[]], devSummaryJson={\\\"cachedValue\\\":{\\\"errors\\\":[],\\\"configErrors\\\":[],\\\"summary\\\":{\\\"pullrequest\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"stateCount\\\":0,\\\"state\\\":\\\"OPEN\\\",\\\"details\\\":{\\\"openCount\\\":0,\\\"mergedCount\\\":0,\\\"declinedCount\\\":0,\\\"total\\\":0},\\\"open\\\":true},\\\"byInstanceType\\\":{}},\\\"build\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"failedBuildCount\\\":0,\\\"successfulBuildCount\\\":0,\\\"unknownBuildCount\\\":0},\\\"byInstanceType\\\":{}},\\\"review\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"stateCount\\\":0,\\\"state\\\":null,\\\"dueDate\\\":null,\\\"overDue\\\":false,\\\"completed\\\":false},\\\"byInstanceType\\\":{}},\\\"deployment-environment\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"topEnvironments\\\":[],\\\"showProjects\\\":false,\\\"successfulCount\\\":0},\\\"byInstanceType\\\":{}},\\\"repository\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null},\\\"byInstanceType\\\":{}},\\\"branch\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null},\\\"byInstanceType\\\":{}}}},\\\"isStale\\\":false}}\",\n" +
            "                \"aggregateprogress\": {\n" +
            "                    \"progress\": 1800,\n" +
            "                    \"total\": 1800,\n" +
            "                    \"percent\": 100\n" +
            "                },\n" +
            "                \"customfield_10200\": null,\n" +
            "                \"customfield_12501\": null,\n" +
            "                \"customfield_12500\": null,\n" +
            "                \"customfield_11601\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/customFieldOption/10800\",\n" +
            "                    \"value\": \"Входящий звонок\",\n" +
            "                    \"id\": \"10800\",\n" +
            "                    \"disabled\": false\n" +
            "                },\n" +
            "                \"customfield_11204\": null,\n" +
            "                \"customfield_11600\": null,\n" +
            "                \"customfield_11205\": null,\n" +
            "                \"environment\": null,\n" +
            "                \"customfield_11603\": null,\n" +
            "                \"customfield_11206\": null,\n" +
            "                \"customfield_11602\": null,\n" +
            "                \"duedate\": null,\n" +
            "                \"progress\": {\n" +
            "                    \"progress\": 1800,\n" +
            "                    \"total\": 1800,\n" +
            "                    \"percent\": 100\n" +
            "                },\n" +
            "                \"votes\": {\n" +
            "                    \"self\": \"https://jira.ppl33-35.com:8443/rest/api/2/issue/OFAMPU-59/votes\",\n" +
            "                    \"votes\": 0,\n" +
            "                    \"hasVoted\": false\n" +
            "                }\n" +
            "            }\n" +
            "        }";

    @Test
    @DisplayName("Тестирование компонента для работы с Jira")
    void testGson(){
        Task task = Task.builder()
                .id(1L)
                .priority(TaskPriority.High.getValue())
                .title("Title")
                .text("Text")
                .wplan_prs_id(4798481L)
                .prev_start_task(new Date())
                .p_time_start(new Date())
                .jTaskId("1")
                .jTaskName("OFAMPU-1")
                .jEditDate(new Date())
                .apl_id(1L)
                .apl_name("Test application name")
                .prs_name("Test personal")
                .build();
        String taskJson = gson.toJson(task);
        log.info(taskJson);
        Assertions.assertEquals(jsonStr, taskJson);
    }

    @Test
    @DisplayName("Тестирование компонента для работы с Jira")
    void testJsonToTask(){
        Task task = gson.fromJson(jiraJson, Task.class);
        log.info(String.valueOf(task));

    }

    @Test
    @DisplayName("Тестирование конвертирования значения даты заявки Jira")
    void testformatDateTime() throws ParseException {
        String dateString = "2021-01-10T08:48:32.006+0200";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSzzzz");
        Date date = sdf.parse(dateString);
        log.info("Date - {}", date.toString());
        Assertions.assertNotNull(date);
    }
}