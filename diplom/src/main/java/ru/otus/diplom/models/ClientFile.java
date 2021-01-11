package ru.otus.diplom.models;

import lombok.*;
import ru.otus.diplom.annotations.Column;

import java.io.InputStream;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
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
