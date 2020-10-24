package com.example.sampleTodo.app.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskForm {

    @Digits(integer = 1, fraction = 0)
    private int typeId;

    @NotBlank(message = "タイトルを入力してください")
    @Size(min = 1, max = 20, message = "20文字以内で入力してください")
    private String title;

    @NotNull(message = "内容を入力してください")
    private String detail;

    @NotNull(message = "期限を設定してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "期限が過去に設定されています")
    private LocalDateTime deadline;

    public boolean isNewTask;     //新規登録か、追加登録したいのか判別
}
