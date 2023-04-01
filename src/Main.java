public class Main {
    public static void main(String[] args) {
    TaskManager manager = new TaskManager();
        manager.createTask(new Task("Купить молоко","Сходить в ближайший магазин и взять молоко","NEW"));
        manager.createEpic(new Epic("Переезд","Переезд в другую квартиру","NEW"));
        manager.createEpic(new Epic("Ремонт","Ремонт в новой квартире","NEW"));
        manager.createSubTasks(manager.getEpicById(2),new SubTask("Собрать коробки","Коробки для перезда не собраны","DONE"));
        manager.createTask(new Task("Сходить на фитнес","Сходи в зал, зал сам в себя не походит!","NEW"));
        manager.createSubTasks(manager.getEpicById(2),new SubTask("Упаковать кошку","И чтобы она осталась жива!","DONE"));
        manager.createSubTasks(manager.getEpicById(2),new SubTask("Сказать слова прощания","Молитву","DONE"));
        manager.updateSubTasks(new SubTask("Успокоить кота","Ну кому-то надо это делать","IN_PROGRESS"),4);
        manager.updateEpic(new Epic("Отпуск","Уехать далеко и надолго","NEW"),3);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubTask());
        System.out.println(manager.getEpicById(2));
        System.out.println(manager.getAllSubTasksByEpic(manager.getEpicById(2)));
        manager.removeSubTaskById(4);
        manager.removeAllSubTasks();
        System.out.println(manager.getEpicById(2));
        System.out.println(manager.getAllSubTasksByEpic(manager.getEpicById(2)));
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

    }
}