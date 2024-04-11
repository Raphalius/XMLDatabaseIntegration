package com.rz.task;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

//TODO - Possible improvement for working with a real DB is downloading data from DB to some data structure,
// adding/deleting/modifying in the data structure and then uploading the changes to DB.
// This would be much more efficient when multiple modifications are made. This could also ensure atomicity.
@Service
@Slf4j
public class EmployeeService {

    XmlMapper xmlMapper;
    @Value("${app.paths.db}")
    private String dbPath;

    public EmployeeService(AppConfig appConfig) {
        this.xmlMapper = appConfig.xmlMapper();
    }

    //TODO - Possible improvement would be to overload this method and provide different parameters for the search
    // multiple searches would be much more efficient if the db data was loaded into a data structure
    public Person find(boolean isInternal, String firstName, String lastName, String mobile) {

        File dir = new File(getCataloguePath(isInternal));
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                try {
                    Person person = xmlMapper.readValue(file, Person.class);
                    if (person.getFirstName().equals(firstName) &&
                            person.getLastName().equals(lastName) &&
                            person.getMobile().equals(mobile)) {
                        return person;
                    }
                } catch (IOException e) {
                    log.error("Failed to find the person.");
                }
            }
        }
        return null;
    }

    public void create(boolean isInternal, Person person) {
        File file = new File(getCataloguePath(isInternal) + "/" + person.getPersonId() + ".xml");

        if (file.exists()) {
            log.info("Employee of id: " + person.getPersonId() + " already exists.");
            return;
        }
        try {
            xmlMapper.writeValue(file, person);
        } catch (IOException e) {
            log.error("Failed to create the employee.");
        }
    }

    public void remove(String personId) {
        File file = determineInternalOrExternal(personId);

        if (file != null) {
            file.delete();
        }
    }

    public void modify(Person person, boolean willBeInternal, String newPersonId, String newFirstName,
                       String newLastName, String newMobile, String newEmail, String newPesel) {
        remove(person.getPersonId());
        create(willBeInternal, new Person(
                newPersonId.isEmpty() ? person.getPersonId() : newPersonId,
                newFirstName.isEmpty() ? person.getFirstName() : newFirstName,
                newLastName.isEmpty() ? person.getLastName() : newLastName,
                newMobile.isEmpty() ? person.getMobile() : newMobile,
                newEmail.isEmpty() ? person.getEmail() : newEmail,
                newPesel.isEmpty() ? person.getPesel() : newPesel));
    }

    private File determineInternalOrExternal(String personId) {

        File fileInternal = new File(getCataloguePath(true) + "/" + personId + ".xml");
        File fileExternal = new File(getCataloguePath(false) + "/" + personId + ".xml");

        if (fileInternal.exists()) {
            return fileInternal;
        } else if (fileExternal.exists()) {
            return fileExternal;
        }
        log.info("Employee of id: " + personId + " Not found");
        return null;
    }

    private String getCataloguePath(boolean isInternal) {
        return (dbPath + (isInternal ? "/internal" : "/external"));
    }

}
