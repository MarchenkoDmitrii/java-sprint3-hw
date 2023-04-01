import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class TaskManager {
//    Добавляем счетчик-идентификатор задач
    public static int idCounter = 1;
//    хэш-мап с задачами для внешнего использования
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    //    хэш-мап с подзадачами для промежуточного накопления их, перед присваиванием их эпикам
    private final HashMap<Integer,SubTask> subTaskHashMap = new HashMap<>();
//    Хэш-мапа эпиков для внешнего использования уже с подзадачами
    protected HashMap<Epic, HashMap<Integer,SubTask>> epics = new HashMap<>();

    public Collection<Task> getAllTasks() {
        System.out.println("Задачи:");
        return tasks.values();
    }
    public Collection<Epic> getAllEpic() {
        System.out.println("Эпики");
        return epics.keySet();
    }
    public ArrayList<Collection<SubTask>> getAllSubTask() {
        System.out.println("Подзадачи");
//        Создаем лист для выдачи подзадач со всех эпиков
        ArrayList<Collection<SubTask>> subTaskList = new ArrayList<>();
//        Вытаскиваем из "внешней" хэш-мапы
        for (Epic epic : epics.keySet()) {
            // Проверяем эпик на пустоту и нулевую величину(при удалении всех подзадач велью ключа "0",
            // а не NULL ¯\_(ツ)_/¯
            if ((epics.get(epic) == null)||(epics.get(epic).size() == 0)) {
                continue;
            }
           subTaskList.add(epics.getOrDefault(epic,null).values());
        }
        return subTaskList;
    }
    public void removeAllTasks() {
        tasks.clear();
    }
    public void removeAllSubTasks() {
        for (Epic epic : epics.keySet()) {
            if ((epics.get(epic) == null)||(epics.get(epic).size() == 0)) {
                continue;
            }
            epics.get(epic).clear();
//   Этот метод будет проверять статус Эпика каждый раз, когда происходит изменение числа сабтасков или их удаление
            updateEpicStatus(getEpicById(epic.id));
        }
        subTaskHashMap.clear();
        }

    public void removeAllEpics() {
        epics.clear();
        subTaskHashMap.clear();
    }
    public Task getTaskById(int id) {
        try {
            return tasks.get(id);
        }
        catch (NullPointerException exception){
            return null;
        }
    }
    public SubTask getSubTaskById(int id) {
        try {
            return subTaskHashMap.get(id);
        }
        catch (NullPointerException exception){
            return null;
        }
    }
    public Epic getEpicById(int id) {
       try {
           for (Epic epic : epics.keySet()) {
               if(epic.id == id){
                   return epic;
               }
           }
       }
       catch (NullPointerException exception){
           return null;
       }
       return null;
    }

    public void createTask(Task task) {
        tasks.put(task.id, task);
    }

    public void updateTask(Task task,int id) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }else System.out.println("Задачи с ID: '"+id+"' не существует");
    }
    public void createEpic(Epic epic) {
//      Делаем статус Эпика "NЕW" так как он только создан и при отсутствии сабтасков должен быть NEW
        epic.setStatus("NEW");
        epics.put(epic, null);
    }

    public void updateEpic(Epic epic, int id) {
//        Так как нам ID нужен тот же, то присвоим экземпляру ID из параметра метода
        epic.id = id;
//        Так как мы меняем эпик, то из таблицы удалим все подзадачи, Эпик же другой!
        if (epics.containsKey(getEpicById(id))) {
            epics.put(epic,null);
            updateEpicStatus(epic);
        }else {
            System.out.println("Эпика с ID: '" + id + "' не существует");
        }

    }
    public void createSubTasks(Epic epic, SubTask subtask) {
        subTaskHashMap.put(subtask.id,subtask);
        epics.put(epic,subTaskHashMap);
    }

    public void updateSubTasks(SubTask subtask, int id) {
        subtask.id = id;
//        Проверяем мапу сабтасков на нужный ключ
            if (subTaskHashMap.containsKey(id)) {
                subTaskHashMap.put(id, subtask);
//                Меняем сабтаск в хэш-мапе Эпиков
                for (Epic epic : epics.keySet()) {
                    if((epics.get(epic) == null)||(epics.get(epic).size() == 0)) {
                        continue;
                    }
                    if(epics.containsValue(id)) {
                        epics.put(epic, subTaskHashMap);
                        updateEpicStatus(epic);
                    }
                }
            }else {
                System.out.println("Подзадачи с ID: '" + id + "' не существует");
            }
    }
    public void removeTaskById(int id) {
           tasks.remove(id);
           throw new IllegalArgumentException("Задачи с ID: '"+id+"' не существует");

    }
    public void removeEpicById(int id) {
        epics.remove(getEpicById(id));
        throw new IllegalArgumentException("Эпика с ID: '"+id+"' не существует");
    }
    public void removeSubTaskById(int id) {
        try{
            subTaskHashMap.remove(id);
                for (Epic epic : epics.keySet()) {
                    if ((epics.get(epic) == null)||(epics.get(epic).size() == 0)) {
                        continue;
                    }
                    epics.get(epic).remove(id);
                    updateEpicStatus(epic);
            }
        }catch (IllegalArgumentException e){
            System.out.println("Подзадачи с ID: '"+id+"' не существует");
        }
    }
    public Collection<SubTask> getAllSubTasksByEpic(Epic epic) {
        try {
            return epics.get(epic).values();
        } catch (NullPointerException exception) {
            return null;
        }

    }
    private void updateEpicStatus(Epic epic) {
//        Создадим две переменные для проверки статусов у сабтасков
        boolean allSubTasksDone = true;
        boolean allSubTasksNew = true;
//        Если Эпик пустой, то сразу делаем его NEW
        if((epics.get(epic) == null)||(epics.get(epic).size() == 0)){
            epic.setStatus("NEW");
            return;
        }
//        Цикл проходит по всем сабтаскам эпика и делает ложными булевые значения, если они отличаются от DONE или NEW
        for (SubTask subtask : epics.get(epic).values()) {
            if (!subtask.getStatus().equals("DONE")) {
                allSubTasksDone = false;
            }
            if (!subtask.getStatus().equals("NEW")) {
                allSubTasksNew = false;
            }
        }
        if(allSubTasksDone) {
            epic.setStatus("DONE");
        }
        if(allSubTasksNew) {
            epic.setStatus("NEW");
        }
//        Если оба булевы значение ложь, то Эпик будет со статусом "В_РАБОТЕ"
        if(!(allSubTasksDone)||(allSubTasksNew)){
            epic.setStatus("IN_PROGRESS");
        }

    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subTaskHashMap=" + subTaskHashMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskManager that = (TaskManager) o;
        return tasks.equals(that.tasks) && subTaskHashMap.equals(that.subTaskHashMap) && epics.equals(that.epics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, subTaskHashMap, epics);
    }
}
