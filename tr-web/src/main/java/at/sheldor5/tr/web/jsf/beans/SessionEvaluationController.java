package at.sheldor5.tr.web.jsf.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.web.DataProvider;
import jdk.internal.org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.web.DataProvider;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vanessa on 08.06.2017.
 */
@Named(value = "sessionEvaluation")
@RequestScoped
public class SessionEvaluationController implements Serializable{
    
    private List<Session> sessionList = new ArrayList<>();

    @Inject
    private DataProvider dataProvider;

    @PostConstruct
    private void init() {
        sessionList=dataProvider.getMockupSessions();
        String hey ="testitest";
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    /*private DataProvider dataProvider;
    private List<Session> sessionList = new ArrayList<>();
    private User user;
    private String forename="forename";
    private String surname="surname";
    private UserMapping userMapping;

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void init(){
        UserController userController = new UserController();
        userController.init();
        user=userController.getUser();
        forename=userController.getForename();
        surname=userController.getSurname();
        userMapping=userController.getUserMapping();
    }

    public void initMock(){
        sessionList=dataProvider.getMockupSessions();
        String hey ="testitest";
    }
    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(LocalDate from, LocalDate to) {
        dataProvider.getSessions(userMapping,from,to);
    }

    public LocalDate getDateOfSession(int index){
       return sessionList.get(index).getDate();
    }

    public LocalTime getStart(int index){
        return sessionList.get(index).getStart();
    }

    public LocalTime getEnd(int index){

        return sessionList.get(index).getEnd();
    }

    public long getSummary(int index){
        return sessionList.get(index).getSummary();
    }

    public int getRule(){
        //TODO
        return 1;
    }

    public double getMultiplier(int index){
        return sessionList.get(index).getMultiplier();
    }

    public String getHoursToWork(){

        return "8:00";
    }

    public long getOvertime(int index){
        return sessionList.get(index).getValuedSummary();
    }
*/
}
