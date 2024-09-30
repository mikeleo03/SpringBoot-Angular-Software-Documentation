package com.example.restTemplateDemo1.web.service;

import com.example.restTemplateDemo1.web.dto.Person;

public interface PersonService {

    public Person saveUpdatePerson(Person person);

    public Person findPersonById(Integer id);
}
