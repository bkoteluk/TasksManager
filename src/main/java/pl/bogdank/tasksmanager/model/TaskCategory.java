package pl.bogdank.tasksmanager.model;

public enum TaskCategory {
    HOBBY("w czasie wolnym"),
    HOME("obowiązki domowe"),
    SHOPPING("zakupy"),
    WORK("w pracy");

    private String description;

    TaskCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
