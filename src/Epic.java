
public class Epic extends Task {

    public Epic(String name, String description,String status) {
        super(name, description, status);
        this.status = status;
    }
    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}