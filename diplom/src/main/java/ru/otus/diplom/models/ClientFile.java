package ru.otus.diplom.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.diplom.annotations.Column;

import java.io.InputStream;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClientFile {
    @Column
    private long id;
    @Column
    private long tsk_id;
    @Column
    private String file_name;
    @Column
    private InputStream file_blob;
}
