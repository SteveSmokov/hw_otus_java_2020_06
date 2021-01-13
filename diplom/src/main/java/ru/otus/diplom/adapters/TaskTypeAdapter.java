package ru.otus.diplom.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.otus.diplom.models.Task;
import ru.otus.diplom.utils.TaskUtil;

import java.io.IOException;

public class TaskTypeAdapter extends TypeAdapter<Task> {@Override
public void write(JsonWriter out, Task value) throws IOException {
    out.beginObject();
    out.name("fields");
    out.beginObject();
    out.name("project");
    out.beginObject();
    out.name("key").value(value.getProjectKey());
    out.endObject();
    out.name("summary").value(value.getTitle());
    out.name("description").value(value.getText());
    out.name("issuetype");
    out.beginObject();
    out.name("id").value(value.getIssueType());
    out.endObject();
    out.name("priority");
    out.beginObject();
    out.name("id").value(TaskUtil.getIDPriority(value.getPriority()));
    out.endObject();
    String val = TaskUtil.getAppID(value.getApl_id());
    if (val != null) {
        out.name("fixVersions");
        out.beginArray();
        out.beginObject();
        out.name("id").value(val);
        out.endObject();
        out.endArray();
    }
    val = TaskUtil.getComponentID(value.getWplan_prs_id());
    if (val != null) {
        out.name("components");
        out.beginArray();
        out.beginObject();
        out.name("id").value(val);
        out.endObject();
        out.endArray();
    }
    out.name("customfield_11101").value(String.valueOf(value.getId()));
    out.name("customfield_11400").value(value.getPrs_name());
    out.name("customfield_11201");
    out.beginObject();
    out.name("id").value(value.getRegType());
    out.endObject();
    out.endObject();
    out.endObject();
    out.close();
}

    @Override
    public Task read(JsonReader in) throws IOException {
        Task task = new Task();
        in.beginObject();
        while(in.hasNext()) {
            String firstName = in.nextName();
            switch (firstName) {
                case "id":
                    String value = in.nextString();
                    task.setJtask_id(value);
                    break;
                case "key":
                    value = in.nextString();
                    task.setJtask_name(value);
                    break;
                case "fields":
                    in.beginObject();
                    while (in.hasNext()) {
                        String name = in.nextName();
                        switch (name) {
                            case "summary":
                                task.setTitle(in.nextString());
                                break;
                            case "description":
                                if (in.peek() != JsonToken.NULL) {
                                    task.setText(in.nextString());
                                } else in.nextNull();
                                break;
                            case "priority":
                                task.setPriority(TaskUtil.getIDPriority(getObjectValue(in,"id")));
                                break;
                            case "customfield_11101":
                                if (in.peek() != JsonToken.NULL) {
                                    task.setId(Long.valueOf(in.nextString()));
                                } else in.nextNull();
                                break;
                            case "customfield_11400":
                                if (in.peek() != JsonToken.NULL) {
                                    task.setPrs_name(in.nextString());
                                } else in.nextNull();
                                break;
                            default:
                                in.skipValue();
                        }
                    }
                    in.endObject();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        return task;
    }

    private String getObjectValue(JsonReader in, String fieldName){
        String result = null;
        try {
            in.beginObject();
            while (in.hasNext()) {
                if (in.nextName().equals(fieldName)){
                    result = in.nextString();
                    break;
                } else in.skipValue();
            }
            in.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
