package main.java.business;

import java.util.UUID;

public class Student {
    private String id;
    private String name;
    private String email;
    private String studyProgram;

    public Student(String name, String email, String studyProgram) {
        this.id = generateUuid();
        this.name = name;
        this.email = email;
        this.studyProgram = studyProgram;
    }

    public Student(){
        this.id = "";
        this.name = "";
        this.email = "";
        this.studyProgram = "";

    }
    private String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public String getId() {
        return id;
    }


}
