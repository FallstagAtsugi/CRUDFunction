package com.example.sampleTodo.app.task;

import com.example.sampleTodo.entity.Task;
import com.example.sampleTodo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * ToDoアプリ
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    /**
     * タスクの一覧を表示
     *
     * @param taskForm
     * @param model
     * @return resources/templates下のHTMLファイル名
     */
    @GetMapping
    public String task(TaskForm taskForm, Model model) {
        //新規登録か更新かを判断する仕組み
        taskForm.setNewTask(true); //trueだとHTML側に新規登録ということを教えている

        //Taskのリストを取得する
        List<Task> list = taskService.findAll();

        model.addAttribute("list", list);
        model.addAttribute("title", "タスク一覧");

        return "task/index";
    }

    /**
     * タスクデータを１件挿入
     *
     * @param taskForm
     * @param result
     * @param model
     */
    @PostMapping("/insert")
    public String insert(
            @Valid @ModelAttribute TaskForm taskForm,
            BindingResult result,
            Model model) {

        //TaskFormのデータをTaskに格納
        Task task = makeTask(taskForm, 0); //0で新規

        if (!result.hasErrors()) {
            //1件挿入後リダイレクト
            taskService.insert(task);
            return "redirect:/task";
        } else {
            taskForm.setNewTask(true);
            model.addAttribute("taskForm", taskForm);
            List<Task> list = taskService.findAll();
            model.addAttribute("list", list);
            model.addAttribute("title", "タスク一覧（バリデーション）");
            return "task/index";
        }
    }

    /**
     * 1件タスクデータを取得し、フォーム内に表示
     *
     * @param taskForm
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{id}")
    public String showUpdate(
            TaskForm taskForm,
            @PathVariable int id, //スラッシュ以降に入力された文字列を取得
            Model model) {

        //Taskを取得
        Optional<Task> taskOpt = taskService.getTask(id);

        //TaskからTaskFormへの詰め直し
        Optional<TaskForm> taskFormOpt = taskOpt.map(t -> makeTaskForm(t));//Optionalの中にラップされていたtaskを取り出しTに格納 変数tをmakeTaskFormで使用し、その戻り値は変数TaskFormOptに格納される

        //TaskFormがnullで無ければ中身を取り出し
        if (taskFormOpt.isPresent()) {
            taskForm = taskFormOpt.get(); //安全な取り出し方
        }
        model.addAttribute("taskForm", taskForm);
        List<Task> list = taskService.findAll();
        model.addAttribute("list", list);
        model.addAttribute("taskId", id);
        model.addAttribute("title", "更新用フォーム");

        return "task/index";
    }

    /**
     * タスクidを取得し、一件のデータ更新
     *
     * @param taskForm
     * @param result
     * @param taskId
     * @param model
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/update")
    public String update(
            @Valid @ModelAttribute TaskForm taskForm,
            BindingResult result,
            @RequestParam("taskId") int taskId, //hiddenの受取はこのように記述する
            Model model,
            RedirectAttributes redirectAttributes) {

        //TaskFormのデータをTaskに格納
        Task task = makeTask(taskForm, taskId);

        if (!result.hasErrors()) {

            //更新処理、フラッシュスコープの使用、リダイレクト（個々の編集ページ）
            taskService.update(task);
            redirectAttributes.addFlashAttribute("complete", "変更が完了しました");
            return "redirect:/task/" + taskId;
        } else {
            model.addAttribute("taskForm", taskForm);
            model.addAttribute("title", "タスク一覧");
            return "task/index";
        }
    }

    /**
     * タスクidを取得し、一件のデータ削除
     *
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/delete")
    public String delete(
            @RequestParam("taskId") int id,
            Model model) {

        //タスクを1件削除しリダイレクト
        taskService.deleteById(id);

        return "redirect:/task";

    }


    /**
     * TaskFormのデータをTaskに入れて返す
     *
     * @param taskForm
     * @param taskId   新規登録の場合は0を指定
     * @return
     */
    private Task makeTask(TaskForm taskForm, int taskId) {
        Task task = new Task();
        if (taskId != 0) {
            task.setId(taskId);
        }
        task.setUserId(1);
        task.setTypeId(taskForm.getTypeId());
        task.setTitle(taskForm.getTitle());
        task.setDetail(taskForm.getDetail());
        task.setDeadline(taskForm.getDeadline());
        return task;
    }

    /**
     * TaskのデータをTaskFormに入れて返す
     *
     * @param task
     * @return
     */
    private TaskForm makeTaskForm(Task task) {

        TaskForm taskForm = new TaskForm();

        taskForm.setTypeId(task.getTypeId());
        taskForm.setTitle(task.getTitle());
        taskForm.setDetail(task.getDetail());
        taskForm.setDeadline(task.getDeadline());
        taskForm.setNewTask(false);

        return taskForm;
    }

}
