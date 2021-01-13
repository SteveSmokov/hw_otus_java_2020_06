package ru.otus.diplom.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.otus.diplom.enums.Application;
import ru.otus.diplom.enums.Departament;
import ru.otus.diplom.enums.TaskPriority;
import ru.otus.diplom.models.Task;

import java.io.IOException;

public class TaskTypeAdapter extends TypeAdapter<Task> {@Override
public void write(JsonWriter out, Task value) throws IOException {
    out.beginObject();
    out.name("fields");
    out.beginObject();
    out.name("project");
    out.beginObject();
    out.name("key").value(value.getPROJECT_KEY());
    out.endObject();
    out.name("summary").value(value.getTitle());
    out.name("description").value(value.getText());
    out.name("issuetype");
    out.beginObject();
    out.name("id").value(value.getISSUE_TYPE_ID());
    out.endObject();
    out.name("priority");
    out.beginObject();
    out.name("id").value(TaskPriority.byValue(value.getPriority()).getJiraValue());
    out.endObject();
    Application apl = Application.byValue(value.getApl_id());
    if ((apl != null) && (apl.getJiraValue() != null)) {
        out.name("fixVersions");
        out.beginArray();
        out.beginObject();
        out.name("id").value(apl.getJiraValue());
        out.endObject();
        out.endArray();
    }
    Departament dep = Departament.byValue(value.getWplan_prs_id());
    if ((dep != null) && (dep.getJiraValue() != null)) {
        out.name("components");
        out.beginArray();
        out.beginObject();
        out.name("id").value(dep.getJiraValue());
        out.endObject();
        out.endArray();
    }
    out.name("customfield_11101").value(String.valueOf(value.getId()));
    out.name("customfield_11400").value(value.getPrs_name());
    out.name("customfield_11201");
    out.beginObject();
    out.name("id").value(value.getREGISTER_TYPE_ID());
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
                    task.setJTaskId(in.nextString());
                    break;
                case "key":
                    task.setJTaskName(in.nextString());
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
                                task.setPriority(TaskPriority.byJiraValue(getObjectValue(in,"id")).getValue());
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
