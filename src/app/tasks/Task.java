package app.tasks;

import app.enumeration.StatusTasks;
import app.enumeration.TypeTask;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private StatusTasks status = StatusTasks.NEW;
    private final TypeTask type = TypeTask.TASK;

    //    Новые поля класса
    private Duration duration;
    private Instant startTime;

    //    Конструкторы
    public Task(Task task) {
        id = task.getId();
        name = task.getName();
        description = task.getDescription();
        status = task.getStatus();
        duration = task.duration ;
        startTime = task.startTime;
    }

    public Task(String name, String description, Duration duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(Integer id, String name, String description, Duration duration, Instant startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }


    //    Геттеры
    public Duration getDuration() {
        return duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return startTime.plus(duration);
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public StatusTasks getStatus() {
        return status;
    }

    public TypeTask getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    //    Сеттеры
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(StatusTasks status) {
        this.status = status;
    }


    //    Переопределение методов
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + this.getStatus() +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + this.startTime.plus(this.duration) + '\'' +
                '}';
    }

}
