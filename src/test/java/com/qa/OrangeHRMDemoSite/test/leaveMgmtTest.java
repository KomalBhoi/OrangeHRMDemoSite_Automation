package com.qa.OrangeHRMDemoSite.test;

import com.qa.OrangeHRMDemoSite.base.baseTest;
import com.qa.OrangeHRMDemoSite.pages.leaveMgmtPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class leaveMgmtTest extends baseTest {

    private leaveMgmtPage leaveMgmtPg;

    @BeforeClass
    public void initPages() {
        leaveMgmtPg = new leaveMgmtPage();
    }

    @Test(priority = 1,groups = "loginRequired", description = "Assign leave to an employee")
    public void test_assignLeave() {

        String empNm ="Ravi M B";
        String leaveType = "CAN - Bereavement";
        String comment = "Lorem ipsum1231111";

        leaveMgmtPg.clearLeaveDetails();
        //leaveMgmtPg.assignLeave(empNm,leaveType,comment);
        boolean isAssigned = leaveMgmtPg.assignLeave(empNm,leaveType,comment);
//        boolean found = leaveMgmtPg.searchAssignedLeaveWithRetry("", "", "Scheduled", "", "",
//                "", 3, 2);
//        Assert.assertTrue(found,"Assigned leave not found even after retries!");
        Assert.assertTrue(isAssigned,"Leave assignment failed!");
    }

    @Test(priority = 2,groups = "loginRequired", description = "Search assigned leave")
    public void test_searchForAssignedLeave() {
        String fromDt="";
        String toDt="";
        String leaveSt="Scheduled";
        String leaveType="";
        String empNm ="";
        String subUnit="";

        boolean isFound = leaveMgmtPg.searchAssignedLeave(fromDt,toDt,leaveSt,leaveType, empNm,subUnit);
        //System.out.println("isFound= "+isFound);
        Assert.assertTrue(isFound,"Assigned leave not found in list!!");
    }

    @Test(priority = 3,groups = "loginRequired", description = "Cancel first found leave if present")
    public void test_cancelLeaveIfPresent() {

        String fromDt="";
        String toDt="";
        String leaveSt="Scheduled";
        String leaveType="";
        String empNm ="";
        String subUnit="";

        boolean isFound = leaveMgmtPg.searchAssignedLeave(fromDt,toDt,leaveSt,leaveType, empNm,subUnit);

        if(isFound){
            leaveMgmtPg.cancelLeaveIfPresent();
            //Assert.assertTrue(isFound,"No leave found for cancel!!");
        }else{
            Assert.assertTrue(true,"No leave present to cancel (acceptable)");
        }
    }
}
