package com.qa.OrangeHRMDemoSite.precondition;

import com.qa.OrangeHRMDemoSite.pages.employeeMgmtPage;
import com.qa.OrangeHRMDemoSite.pages.leaveMgmtPage;
import com.qa.OrangeHRMDemoSite.pages.vacancyPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreconditionManager {

    private static final Logger log = LogManager.getLogger(PreconditionManager.class);

    private static boolean leaveModuleReady= false;
    private final leaveMgmtPage leaveMgmtPg;

    public PreconditionManager() {
        this.leaveMgmtPg = new leaveMgmtPage();
    }

    public static void ensureRecruitementData() throws InterruptedException {
        employeeMgmtPage empPg=new employeeMgmtPage();
        vacancyPage vacancyPg = new vacancyPage();

        if (!empPg.isEmployeePresent("John", "Smith")) {
            empPg.createEmployee("John", "Smith");
        }

        if (!vacancyPg.isVacancyPresent("QA Automation Vacancy")) {
            vacancyPg.createVacancyIfRequired(
                    "QA Automation Vacancy",
                    "QA Engineer",
                    "John Smith"
            );
        }
    }

    public void ensureLeaveModuleReady() {

        if (leaveModuleReady) {
            log.info("Leave module already initialized — skipping preconditions.");
            return;
        }

        log.info("Initializing Leave module preconditions...");

        leaveMgmtPg.initializeLeaveModuleIfRequired();
        leaveMgmtPg.ensureLeaveTypeExists("Paid Leave");

        leaveModuleReady = true;

        log.info("Leave module preconditions completed successfully.");
    }

}
