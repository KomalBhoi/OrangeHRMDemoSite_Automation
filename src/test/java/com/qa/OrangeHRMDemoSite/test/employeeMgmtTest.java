package com.qa.OrangeHRMDemoSite.test;

import com.qa.OrangeHRMDemoSite.Utils.readExcelData;
import com.qa.OrangeHRMDemoSite.base.baseTest;
import com.qa.OrangeHRMDemoSite.pages.employeeMgmtPage;
import org.testng.Assert;
import org.testng.annotations.*;

public class employeeMgmtTest extends baseTest {

    employeeMgmtPage empMgmtPg;

    @Test(priority = 1,groups = "loginRequired")
    public void test_addEmpDetails() throws InterruptedException {
        empMgmtPg= new employeeMgmtPage();
        empMgmtPg.addEmpDetails("Swanandi","Anil","Bhelkhande","6581",true,true);
        empMgmtPg.searchEmployeeDetails("Swanandi","","","","","","");

        boolean isFound = empMgmtPg.verifyEmpInList("6581","Swanandi Anil","Bhelkhande");
        Assert.assertTrue(isFound,"Employee details not found in Employee List!");
    }

    @Test(priority = 2,groups = "loginRequired")
    public void test_negativeTC_addEmpDetails() throws InterruptedException {
        empMgmtPg= new employeeMgmtPage();
        String validation_msg = empMgmtPg.addEmpDetail_NegativeTC("","","");
        Assert.assertTrue(validation_msg.contains("Required"),"Username 'Required' not shown!");
    }

    @DataProvider(name = "getEmployeeData")
    public Object[][] getEmployeeData() {
        return readExcelData.getTestData("EmployeeDetails");
    }

    @Test(priority = 3,groups = "loginRequired")
    public void test_addEmployeeData() throws InterruptedException {
        empMgmtPg= new employeeMgmtPage();
        Object[][] empData = getEmployeeData();
        for(int i=0;i < empData.length;i++){
              boolean isFirst = (i==0);
              empMgmtPg.addEmpDetails(
                      (String) empData[i][0],
                      (String) empData[i][1],
                      (String) empData[i][2],
                      (String) empData[i][3],
                      isFirst,
                      false);
            }
    }

    @Test(priority=4,groups = "loginRequired")
    public void test_editEmpDetails() throws InterruptedException {
        empMgmtPg= new employeeMgmtPage();
        empMgmtPg.searchEmployeeDetails("Swanandi Anil","","","","","","");
        String result =empMgmtPg.editEmpDetails("012456");

        if(result.equals("Updated")){
            Assert.assertTrue(true,"Employee details updated successfully..!");
        }else{
            Assert.fail("No record found for updation!");
        }
    }

    @Test(priority = 5,groups = "loginRequired")
    public void test_deleteEmpDetails() throws InterruptedException {
        empMgmtPg= new employeeMgmtPage();
        empMgmtPg.searchEmployeeDetails("Swanandi Anil","","","","","","");
        String result = empMgmtPg.deleteEmpDetails();

        if(result.equals("deleted")){
            Assert.assertTrue(true,"Employee details deleted successfully");
        }else{
            Assert.fail("Employee not found for delete operation!");
        }
    }
}
