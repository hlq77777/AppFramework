package com.android.kingwong.kingwongproject.novate.model;

/**
 * Created by LIUYONGKUI726 on 2017-06-09.
 */

public class MusicBookCategory {


    public MusicBookCategory() {
    }



    /**
     * id : 15
     * createDate : 1496286231000
     * modifyDate : 1496291243000
     * order : 1
     * name : 推荐1
     * treePath : ,13,
     * grade : 1
     */

    private int id;
    private long createDate;
    private long modifyDate;
    private int order;
    private String name;
    private String treePath;
    private int grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTreePath() {
        return treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }


    @Override
    public String toString() {
        return "MusicBookCategory{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", order=" + order +
                ", name='" + name + '\'' +
                ", treePath='" + treePath + '\'' +
                ", grade=" + grade +
                '}';
    }
}
