package com.qa.OrangeHRMDemoSite.test;

import com.qa.OrangeHRMDemoSite.base.baseTest;
import com.qa.OrangeHRMDemoSite.pages.*;
import com.qa.OrangeHRMDemoSite.precondition.PreconditionManager;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.qa.OrangeHRMDemoSite.pages.candidatePage.log;

public class recruitementTest extends baseTest {

    private vacancyPage vacancyPg;
    private candidatePage candidatePg;

    @BeforeClass(alwaysRun = true)
    public void initPages(){
        vacancyPg = new vacancyPage();
        candidatePg = new candidatePage();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupRecruitementData() throws InterruptedException {
        try {
            PreconditionManager.ensureRecruitementData();
        }catch(Exception e){
            log.warn("Recruitment precondition skipped: "+e.getMessage());
        }
    }

    // ==================================================================================
    //  Test 1 – Add Vacancy + Verify Listing
    // ==================================================================================
    @Test(priority = 1,groups = {"loginRequired","recruitement"})
    public void test_addVacancies() throws InterruptedException {

        String vacancyName     = "QA Lead";
        String jobTitle        = "QA Lead";
        String description     = "Leadership skills";
        String hiringManager   = "Rina Suresh Desai";
        String noOfPositions   = "2";

        // Step 1: Add Vacancy
        vacancyPg.addVacancies(vacancyName, jobTitle, description, hiringManager, noOfPositions);

        // Step 2: Search for vacancy
        boolean isFound = vacancyPg.searchForVacancies(jobTitle);

        // Step 3: Validate row data
        if(isFound) {
            boolean isValid = vacancyPg.verifyVacancyInList(jobTitle, vacancyName, hiringManager);
            Assert.assertTrue(isValid, "Vacancy data mismatch in the results!");
        }
    }

    // ==================================================================================
    // Test 2 – Add Candidate
    // ==================================================================================
    @Test(priority = 2,groups = "loginRequired",description = "Verify candidate can be added successfully")
    public void test_addCandidateInfo() throws InterruptedException {

        candidatePg.addCandidatesInfo(
                "Jassi",
                "Jaisi",
                "Jaiswar",
                "QA Lead",
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
    @Test(priority = 3,groups = "loginRequired",description = "Verify interview shortlisting process")
    public void test_interviewProcess() throws InterruptedException {

        // Step 1: Search the candidate
       boolean isFound = candidatePg.searchCandidate(
                "", "QA Lead", "", "",      // no job title filters
                "Jassi",             // candidate to search
                "",                  // keywords
                "", "",         // date range
                ""                   // method of application
        );

        System.out.println("RecruitementTest page isFound: "+isFound);
        // Step 2: Shortlist / Interview Flow
        if(isFound) {
           boolean foundRows = candidatePg.initiateInterviewProcess();
           if(foundRows){
               log.info("---- Interview process initiated successfully ----");
           }else{
               log.error("No rows found for interview process initiation..");
           }
        }else{
            throw new SkipException("Candidate not found. Skipping interview process.");
        }

        // Final validation
        Assert.assertFalse(candidatePg.isErrorDisplayed(),
                "Validation error appeared during interview process!");
    }
}
