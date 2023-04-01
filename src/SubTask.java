public class SubTask extends Task {

    public SubTask(String name, String description,String status) {
        super(name, description,status);
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
