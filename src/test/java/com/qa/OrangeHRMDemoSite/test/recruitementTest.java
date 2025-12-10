package com.qa.OrangeHRMDemoSite.test;

import com.qa.OrangeHRMDemoSite.base.baseTest;
import com.qa.OrangeHRMDemoSite.pages.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class recruitementTest extends baseTest {

    private vacancyPage vacancyPg;
    private candidatePage candidatePg;

    @BeforeClass
    public void initPages(){
        vacancyPg = new vacancyPage();
        candidatePg = new candidatePage();
    }

    // ==================================================================================
    //  Test 1 – Add Vacancy + Verify Listing
    // ==================================================================================
    @Test(priority = 1)
    public void test_addVacancies() throws InterruptedException {

        String vacancyName     = "Software Engineer";
        String jobTitle        = "Software Engineer";
        String description     = "Selenium with Java";
        String hiringManager   = "Peter Mac Anderson";
        String noOfPositions   = "1";

        // Step 1: Add Vacancy
        vacancyPg.addVacancies(vacancyName, jobTitle, description, hiringManager, noOfPositions);

        // Step 2: Search for vacancy
        boolean isFound = vacancyPg.searchForVacancies(jobTitle, vacancyName, hiringManager, "");

        // Step 3: Validate row data
        if(isFound) {
            boolean isValid = vacancyPg.verifyVacancyInList("Software Engineer", "Software Engineer", "Peter Mac Anderson");
            Assert.assertTrue(isValid, "Vacancy data mismatch in the results!");
        }
    }

    // ==================================================================================
    // Test 2 – Add Candidate
    // ==================================================================================
    @Test(priority = 2,description = "Verify candidate can be added successfully")
    public void test_addCandidateInfo() throws InterruptedException {

        candidatePg.addCandidatesInfo(
                "Jassi",
                "Jaisi",
                "Jaiswar",
                "Account Assistant",
                "jas111@gmail.com",
                "8023125621",
                "Leadership,communication skills",
                "",
                "");

        Assert.assertFalse(candidatePg.isErrorDisplayed(), "Validation error appeared while adding candidate!");
    }

    // ==================================================================================
    // Test 3 – Search Candidate + Shortlist (Interview Process)
    // ==================================================================================
    @Test(priority = 3, description = "Verify interview shortlisting process")
    public void test_interviewProcess() throws InterruptedException {

        // Step 1: Search the candidate
        candidatePg.searchCandidate(
                "", "", "", "",      // no job title filters
                "Jassi",             // candidate to search
                "",                  // keywords
                "", "",         // date range
                ""                   // method of application
        );

        // Step 2: Shortlist / Interview Flow
        candidatePg.initiateInterviewProcess();

        // Final validation
        Assert.assertFalse(candidatePg.isErrorDisplayed(),
                "Validation error appeared during interview process!");
    }
}
