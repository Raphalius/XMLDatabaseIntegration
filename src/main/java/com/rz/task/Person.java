package com.rz.task;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO - If internal and external employees have different data structures I see 2 possible solutions:
// 1. Transform Person into an interface an make Classes Internal and External that would implement it
// 2. Create a Wapper or Wrappers for Person class that would include additional data
@JacksonXmlRootElement(localName = "Person")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @JacksonXmlProperty(isAttribute = true)
    private String personId;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String pesel;
}
