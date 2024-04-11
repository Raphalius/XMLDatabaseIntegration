package com.rz.task;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    Person person1 = new Person("01",
            "Rafal",
            "Zywica",
            "000000001",
            "rzywica@email.com",
            "00000000001");
    Person person2 = new Person("02",
            "Jakub",
            "Anananas",
            "000000002",
            "ja@email.com",
            "00000000002");

    Person person3 = new Person("03",
            "Albert",
            "Huligan",
            "000000003",
            "ah@email.com",
            "00000000003");
    @BeforeAll
    public void setUpDb() {
        employeeService.create(true, person1);
        employeeService.create(false, person2);
    }
    @AfterAll
    public void cleanUp() {
        employeeService.remove(person1.getPersonId());
        employeeService.remove(person2.getPersonId());
    }

    @Test
    public void testFindHappyScenario() {

        //GIVEN
        //WHEN
        Person found = employeeService.find(true,
                person1.getFirstName(),
                person1.getLastName(),
                person1.getMobile());
        //THEN
        Assertions.assertEquals(person1.getPersonId(), found.getPersonId());
    }

    @Test
    public void testFindSadScenario() {
        //GIVEN
        //WHEN
        Person notFound = employeeService.find(true,
                person3.getFirstName(),
                person3.getLastName(),
                person3.getMobile());
        //THEN
        Assertions.assertNull(notFound);
    }

    @Test
    public void testCreate() {
        //GIVEN
        //WHEN
        employeeService.create(false, person3);
        Person found = employeeService.find(false,
                person3.getFirstName(),
                person3.getLastName(),
                person3.getMobile());
        //THEN
        Assertions.assertEquals(person3.getPersonId(), found.getPersonId());
    }

    @Test
    public void testDelete() {
        //GIVEN
        //WHEN
        employeeService.remove(person3.getPersonId());
        Person notFound = employeeService.find(false,
                person3.getFirstName(),
                person3.getLastName(),
                person3.getMobile());
        //THEN
        Assertions.assertNull(notFound);
    }

    @Test
    public void testModify() {
        //GIVEN
        //WHEN
        employeeService.modify(person1,
                false ,
                "" ,
                "" ,
                "" ,
                "" ,
                "" ,
                "123123" );
        Person found = employeeService.find(false,
                person1.getFirstName(),
                person1.getLastName(),
                person1.getMobile());
        //THEN
        Assertions.assertNotNull(found);
        Assertions.assertEquals("123123", found.getPesel());
        Assertions.assertEquals("Rafal", found.getFirstName());
    }
}
