package ru.otus.diplom.models;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.diplom.adapters.CommentTypeAdapter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonAdapter(CommentTypeAdapter.class)
public class Comment implements Serializable {
    private Long id;
    private String author_login;
    private String author_name;
    private String text;
    private Date created;
    private Date updated;

}
