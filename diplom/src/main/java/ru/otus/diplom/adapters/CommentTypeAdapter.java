package ru.otus.diplom.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.SneakyThrows;
import ru.otus.diplom.models.Comment;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CommentTypeAdapter extends TypeAdapter<Comment> {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSzzzz");

    @Override
    public void write(JsonWriter out, Comment value) throws IOException {
        out.beginObject();
        out.endObject();
        out.close();
    }

    @SneakyThrows
    @Override
    public Comment read(JsonReader in) throws IOException {
        Comment comment = new Comment();
        in.beginObject();
        while(in.hasNext()) {
            String firstName = in.nextName();
            switch (firstName) {
                case "id":
                    comment.setId(Long.valueOf(in.nextString()));
                    break;
                case "created":
                    comment.setCreated(sdf.parse(in.nextString()));
                    break;
                case "updated":
                    comment.setUpdated(sdf.parse(in.nextString()));
                    break;
                case "body":
                    comment.setText(in.nextString());
                    break;
                case "author":
                    in.beginObject();
                    while (in.hasNext()) {
                        String name = in.nextName();
                        switch (name) {
                            case "key":
                                comment.setAuthor_login(in.nextString());
                                break;
                            case "displayName":
                                if (in.peek() != JsonToken.NULL) {
                                    comment.setAuthor_name(in.nextString());
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
        return comment;
    }
}
