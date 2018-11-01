package com.drishika.gradzcircle.cucumber.stepdefs;

import com.drishika.gradzcircle.GradzcircleApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = GradzcircleApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
