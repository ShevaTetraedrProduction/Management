package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Journal {
    private IntegerProperty idJ = new SimpleIntegerProperty();
    private StringProperty nameJ = new SimpleStringProperty();
    private StringProperty last_nameJ = new SimpleStringProperty();
    private List<Integer> subjectsJ = new ArrayList<>();
    private Object[] a;
    private IntegerProperty subject1 = new SimpleIntegerProperty();
    private IntegerProperty subject2, subject3, subject4, subject5, subject6, subject7, subject8, subject9, subject10, subject11,
            subject12, subject13, subject14, subject15;



    public double getAVG() {
        return AVG;
    }

    public void setAVG(double AVG) {
        this.AVG = AVG;
    }

    private double AVG = 0;


    public Journal(Integer... subjectsJ) {
        this.subjectsJ = Arrays.asList(subjectsJ);
    }

    public Journal(List<Integer> subjectsJ) {
        this.subjectsJ = subjectsJ;
    }

    public List<Integer> getSubjects() {

        return subjectsJ;

    }


    @SuppressWarnings("Duplicates")
    public void setSubjectsJ(List<Integer> subjectsJ) {
        this.subjectsJ = subjectsJ;
        AVG = 0;
        subjectsJ.forEach(v -> AVG += v.intValue());
        AVG /= subjectsJ.size();
        AVG = Math.round(AVG);
        a = subjectsJ.toArray();
        int i = 0;
        if (i >= subjectsJ.size()) return;
        subject1 = new SimpleIntegerProperty(subjectsJ.get(i++));        if (i >= subjectsJ.size()) return;
        subject2 = new SimpleIntegerProperty(subjectsJ.get(i++));        if (i >= subjectsJ.size()) return;
        subject3 = new SimpleIntegerProperty(subjectsJ.get(i++));         if (i >= subjectsJ.size()) return;
        subject4 = new SimpleIntegerProperty(subjectsJ.get(i++));         if (i >= subjectsJ.size()) return;
        subject5 = new SimpleIntegerProperty(subjectsJ.get(i++));         if (i >= subjectsJ.size()) return;
        subject6 = new SimpleIntegerProperty(subjectsJ.get(i++));         if (i >= subjectsJ.size()) return;
        subject7 = new SimpleIntegerProperty(subjectsJ.get(i++));         if (i >= subjectsJ.size()) return;
        subject8 = new SimpleIntegerProperty(subjectsJ.get(i++));         if (i >= subjectsJ.size()) return;
        subject9 = new SimpleIntegerProperty(subjectsJ.get(i++));         if (i >= subjectsJ.size()) return;
        subject10 = new SimpleIntegerProperty(subjectsJ.get(i++));        if (i >= subjectsJ.size()) return;
        subject11 = new SimpleIntegerProperty(subjectsJ.get(i++));        if (i >= subjectsJ.size()) return;
        subject12 = new SimpleIntegerProperty(subjectsJ.get(i++));        if (i >= subjectsJ.size()) return;
        subject13 = new SimpleIntegerProperty(subjectsJ.get(i++));        if (i >= subjectsJ.size()) return;
        subject14 = new SimpleIntegerProperty(subjectsJ.get(i++));        if (i >= subjectsJ.size()) return;
        subject15 = new SimpleIntegerProperty(subjectsJ.get(i));
    }

    public int getIdJ() {
        return idJ.get();
    }

    public IntegerProperty idJProperty() {
        return idJ;
    }

    public void setIdJ(int idJ) {
        this.idJ.set(idJ);
    }

    public String getNameJ() {
        return nameJ.get();
    }

    public StringProperty nameJProperty() {
        return nameJ;
    }

    public void setNameJ(String nameJ) {
        this.nameJ.set(nameJ);
    }

    public String getLast_nameJ() {
        return last_nameJ.get();
    }

    public StringProperty last_nameJProperty() {
        return last_nameJ;
    }

    public void setLast_nameJ(String last_nameJ) {
        this.last_nameJ.set(last_nameJ);
    }

    public List<Integer> getSubjectsJ() {
        return subjectsJ;
    }

    public Object[] getA() {
        return a;
    }

    public void setA(Object[] a) {
        this.a = a;
    }

    public int getSubject1() {
        return subject1.get();
    }

    public IntegerProperty subject1Property() {
        return subject1;
    }

    public void setSubject1(int subject1) {
        this.subject1.set(subject1);
    }

    public int getSubject2() {
        return subject2.get();
    }

    public IntegerProperty subject2Property() {
        return subject2;
    }

    public void setSubject2(int subject2) {
        this.subject2.set(subject2);
    }

    public int getSubject3() {
        return subject3.get();
    }

    public IntegerProperty subject3Property() {
        return subject3;
    }

    public void setSubject3(int subject3) {
        this.subject3.set(subject3);
    }

    public int getSubject4() {
        return subject4.get();
    }

    public IntegerProperty subject4Property() {
        return subject4;
    }

    public void setSubject4(int subject4) {
        this.subject4.set(subject4);
    }

    public int getSubject5() {
        return subject5.get();
    }

    public IntegerProperty subject5Property() {
        return subject5;
    }

    public void setSubject5(int subject5) {
        this.subject5.set(subject5);
    }

    public int getSubject6() {
        return subject6.get();
    }

    public IntegerProperty subject6Property() {
        return subject6;
    }

    public void setSubject6(int subject6) {
        this.subject6.set(subject6);
    }

    public int getSubject7() {
        return subject7.get();
    }

    public IntegerProperty subject7Property() {
        return subject7;
    }

    public void setSubject7(int subject7) {
        this.subject7.set(subject7);
    }

    public int getSubject8() {
        return subject8.get();
    }

    public IntegerProperty subject8Property() {
        return subject8;
    }

    public void setSubject8(int subject8) {
        this.subject8.set(subject8);
    }

    public int getSubject9() {
        return subject9.get();
    }

    public IntegerProperty subject9Property() {
        return subject9;
    }

    public void setSubject9(int subject9) {
        this.subject9.set(subject9);
    }

    public int getSubject10() {
        return subject10.get();
    }

    public IntegerProperty subject10Property() {
        return subject10;
    }

    public void setSubject10(int subject10) {
        this.subject10.set(subject10);
    }

    public int getSubject11() {
        return subject11.get();
    }

    public IntegerProperty subject11Property() {
        return subject11;
    }

    public void setSubject11(int subject11) {
        this.subject11.set(subject11);
    }

    public int getSubject12() {
        return subject12.get();
    }

    public IntegerProperty subject12Property() {
        return subject12;
    }

    public void setSubject12(int subject12) {
        this.subject12.set(subject12);
    }

    public int getSubject13() {
        return subject13.get();
    }

    public IntegerProperty subject13Property() {
        return subject13;
    }

    public void setSubject13(int subject13) {
        this.subject13.set(subject13);
    }

    public int getSubject14() {
        return subject14.get();
    }

    public IntegerProperty subject14Property() {
        return subject14;
    }

    public void setSubject14(int subject14) {
        this.subject14.set(subject14);
    }

    public int getSubject15() {
        return subject15.get();
    }

    public IntegerProperty subject15Property() {
        return subject15;
    }

    public void setSubject15(int subject15) {
        this.subject15.set(subject15);
    }
}
