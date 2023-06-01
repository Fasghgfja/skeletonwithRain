<sub><b>Note:</b>This page is intended as a guideline and is used to structure the test script and associated test protocol. This page defines the test execution for the integration tests (and is a suggestion for the acceptance tests). The target group are the persons responsible for the execution of the integration tests. </sub>

[[_TOC_]]

# 1. Test preparation
> Two versions are given: one is the sever version and the other is the local version. 
> The server version can be found at [http://srh-softwaresolutions.com](http://srh-softwaresolutions.com) and can be used to test the functionality of the webapp without any further setup to be done.
> The local version will have to be set-up before being used.

## 1.1 Test data
At the beginning of the test the following users are set up:

### Test data for [server version](http://srh-softwaresolutions.com)

User/Password | Role
--- |-------
<b>UserTesterAccount1/test</b> | User  
<b>UserTesterAccount2/test</b> | User  
<b>UserTesterAccount3/test</b> | User   
<b>GardenerTesterAccount1/test</b> | Gardener 
<b>GardenerTesterAccount2/test</b> | Gardener 
<b>GardenerTesterAccount3/test</b> | Gardener 
<b>AdminTesterAccount/test</b> | Admin

### Test data for local version

User/Password | Role
--- |-------
<b>Max/passwd</b> | User  
<b>Susi/passwd</b> | Gardener  
<b>admin/passwd</b> | Admin

> <sub><ins>Note:</ins>By loggin with an admin account, is possible to create more users, in case they are needed for testing</sub>

- Create mysql database with the properties given in /main/resources/application.properties
- This includes the database URL and also the username and password that needs to be used
- After successfully creating the database, insert data.sql (which ca be found in the same directory as application.properties) into the database
- The local version is now ready and available at [localhost:8080](http://localhost:8080)

# 2. Test protocol

- Test date: <sub>22.05.23 - 29.05.23</sub>
- Tester: <sub>Gamze Kilic, Luca Michael Rahm, Ivan Ploner</sub>
- Tested version: <sub>git main branch status 22.05.23</sub>
- Test entry criteria met: yes/no according to <sub>yes</sub>
- Test environment: <sub>application locally on own computer & server version(http://srh-softwaresolutions.com)</sub>

# 3. Test cases

The test cases described here fully cover the use cases listed in the concept description. Additional test cases were added to check general functional requirements. Deviations from the expected result states were documented as part of the test performed (cf. Section 2, Test protocol) and classified according to the following classifications.
- <b>OK</b>: No discrepancies found.
- <b>Cosmetic discrepancies</b>: Minor layout issues: e.g., line breaks in TExt awkward, texts for buttons too long, etc.
- <b>Medium discrepancies</b>: Functionality is basically present, but can only be used in a limited way, e.g. some expected entries in a dropdown list are missing, data changes are only visible after closing and reopening a dialog, etc.
- <b>Major discrepancies</b>: Functionality is not usable, e.g. action buttons show no reaction, data is not written correctly to the database, etc.
- <b>System unusable</b>: performing this test leaves the system in an unusable state, e.g. system crashes. Database becomes inconsistent, data is (unplanned) deleted, etc.

## 3.1 Test Cases Guests

<table>
  <tr>
    <th colspan="2">TC 1.1: Register an account</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A guest wants to register an account as a user  </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The guest has a device that connect to the internet</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The guest opens the URL localhost:8080 </li>
        <li>The guest clicks on the "register" button and gets redirected to the registration page.  </li>
        <li>The guest is prompted to enter a username and a password. </li>
        <li>After all the required fields are filled, the guest can click on the register button. </li>
        <li>A registration form is open where the guest can edit the previously entered details or confirm and create the account</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The guest is now a registered user</li>
        <li>The newly registered user can now log in</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 1.2: View a plant's gallery</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A guest wants to view a certain plant's gallery </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The guest has a device that can scan QR codes and connect to the internet</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The guest scans the plant's QR-code (that can be generally found outside a greenhouse) </li>
        <li>Once the QR-code is scanned, the guest gets redirected on its device to the registration page.</li>
        <li>The guest clicks on the "view plant's gallery" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The guest can see all the approved pictures present in the plant's gallery. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 1.3:Upload a picture to a plant's gallery</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A guest wants to upload a picture or more to a plant's gallery </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The guest has a device that can scan QR codes and connect to the internet </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The guest scans the plant's QR-code (that can be generally found outside a greenhouse) </li>
        <li>Once the QR-code is scanned, the guest gets redirected on its device to the registration page.</li>
        <li>The guest clicks on the "upload picture" button</li>
        <li>The guest gets redirected to its own device gallery where the picture/s to be uploaded can be chosen. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The picture/s get/s added to a review list where the plants gardener will have to approve or deny it/them, before it/they get/s added to the viewable gallery of the plant. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


## 3.2 Test cases Users
<table>
  <tr>
    <th colspan="2">TC 2.1: Log in</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to log into an already created account </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user needs to have an already registered account and the login page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>A user enters a valid username and password combination and clicks on the "log in" button. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user gets redirected to the user's dashboard page. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 2.2: Log in (wrong username) </td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to log in, but a wrong username is given </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The login page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>A user enters a wrong username and a password and clicks on the "log in" button. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The login page is refreshed and an error message appear, saying that the username or the password is invalid</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 2.3: Log in (wrong password) </td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to log in, but a wrong password is given </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The login page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>A user enters a valid username and a wrong password and clicks on the "log in" button. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The login page is refreshed and an error message appear, saying that the username or the password is invalid</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 2.4: Log out</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to log out from an already logged in session </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and any Plant HealthTM website page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the icon in the top right corner of the page </li>
        <li>A curtain menu opens up with a "log out" button in it that the user can click</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user is now logged out of its account and gets redirected to the login page</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 2.5: Edit own details</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to edit the account's personal details  </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and any Plant HealthTM website page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the icon in the top right corner of the page </li>
        <li>A curtain menu opens up with a "profile settings" button in it, that the user can click and gets redirected to the edit profile page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user can now edit the account's personal details and then press the "save" button to update them</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 2.6: View Plant catalogue</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to view the "plant catalogue" </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and any Plant HealthTM website page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the "plants catalogue" button and gets redirected to the plants catalogue page. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user can see all the plants available in the Plant Health™ website. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 2.7: Filter objects in a list</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to filter objects in a list</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and any Plant HealthTM website page that contain a list of objects is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the field under any attribute at the top of the list and enters a string</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The list is now filtered based on the chosen string</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>
 <ul>
   <li>On the Plant's Catalogue page, you cannot filter the plants by a specific location.
If you want to search for a plant, you have to enter the whole plant name. For example, if you only enter "Rocket", the plant "Alpine Rocket" will not be found. </li>
   </ul>
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [ ] cosmetic discrepancies
- [x] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 2.8: Sort objects in a list</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to sort objects in a list</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and any Plant HealthTM website page that contain a list of objects is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on any attribute at the top of the list</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The objects in the list are now sorted based on the chosen attribute</li>
        <li>On each click, the list gets reversed (first ascending and then descending)</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 2.9: Show latest measurements </td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to see the latest measurements for a plant or a greenhouse in a list</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and any Plant HealthTM website page that contain a list of plants or greenhouses is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the dropdown button on the left side of the selected plant or greenhouse</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user can now see a list of the latest measurements for the specified plant or greenhouse</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 2.10: View all the followed plants</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to view all the followed plants</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and any Plant HealthTM website page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the "dashboard" button and gets redirected to the dashboard. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user can see all the followed plants from the dashboard, under "followed plants"</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 2.11: Follow a new plant</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to add a certain plant to "followed plants" in the dashboard</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and the plant catalogue page open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the "follow plant" button next to the chosen plant </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The plant is no more visible in "plan catalogue" for the logged-in user and is added to "followed plants" in dashboard</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 2.12: Unfollow a plant</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A user wants to remove a certain plant from "followed plants" in dashboard </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The user is logged in and the dashboard page open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The user clicks on the "unfollow plant" button next to the chosen plant in "followed plants"</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The plant is removed from "followed plants" in the dashboard and it returns visible in "plants catalogue" for the logged-in user</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

## 3.3 Test cases Gardeners

<table>
  <tr>
    <th colspan="2">TC 3.1:View assigned greenhouses (through manage assigned greenhouses page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to view the greenhouses that manages </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and any Plant HealthTM website page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "manage assigned greenhouses" button and gets redirected to the greenhouse monitoring page. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The gardener can see all the greenhouses that manages. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.2:View assigned greenhouses (through dashboard page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to view the greenhouses that manages </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and any Plant HealthTM website page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "dashboard" button and gets redirected to the dashboard </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The gardener can preview a small selection of greenhouses (only few greenhouses at a time) and view a graph next to it, displaying the current selected greenhouse's measurements </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.3: Setting web-app interval</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to set the interval between the sensor station and the access point </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouses page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "intervals" button next to the selected greenhouse</li>
        <li>A pop-up appears, asking the gardener for inserting the new interval, after the "saved" (or "abort") button can be clicked</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The interval is now saved and the data from all the connected sensor stations will be sent to the Access Point, once every selected interval</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>When I change the value for station "AAAAA"  plant "Alpine Rocket", somehow values for station "G4T2" plant "Hawaiian Snow" are also changed. (Using the web hosted version and logged in as GardenerTesterAccount1)
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [ ] cosmetic discrepancies
- [x] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.4: Setting measurements interval</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to set the interval between the sensors and the sensor station </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouses page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "intervals" button next to the selected greenhouse</li>
        <li>A pop-up appears, asking the gardener for inserting the new interval, after the "saved" (or "abort") button can be clicked</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The interval is now saved and the data from all the sensors will be sent to the assigned sensor station, once every selected interval</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>Same problem as in 3.3</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [ ] cosmetic discrepancies
- [x] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.5: View greenhouse's data (through manage assigned greenhouses page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to view a greenhouse's data </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouses page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "open detail-view" button next to a greenhouse and gets redirected to the greenhouse details page. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The gardener can see all the greenhouse's data. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.6: View greenhouse's data (through dashboard page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to view a greenhouse's data </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the dashboard page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "open detail-view" button next to a greenhouse and gets redirected to the greenhouse details page. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The gardener can see all the greenhouse's data. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.7: View measurement's graph</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to view a measurement's graph from a certain plant</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouse details page for a specific plant is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on one of the measurement's box at the top of the page</li>
        <li>Alternatively, the gardener can also click one of the entries from the measurement's list, on the right side of the graph </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The graph now shows the measurements over time for that specific type</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.8: Approve an uploaded picture</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to approve a plant's uploaded picture</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in, the greenhouse details page for a specific plant is open and there is at least one picture in "awaiting approval" </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "awaiting approval" button</li>
        <li>A pop-up slideshow appears, showing one by one all the awaiting approval pictures, where the gardener can approve by pressing the "approve image" button.</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>All the approved pictures are now part of the plant's gallery and not anymore in "awaiting approval"</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>Maybe give a hint, if new picture is waiting for approval. For example a red dot in the corner.
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [x] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 3.9: Deny an uploaded picture</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to deny a plant's uploaded picture  </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in, the greenhouse details page for a specific plant is open and there is at least one picture in "awaiting approval" </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "awaiting approval" button</li>
        <li>A pop-up slideshow appears, showing one by one all the awaiting approval pictures, where the gardener can deny by pressing the "deny" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>All the denied pictures are now deleted and not anymore in "awaiting approval"</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 3.10: Delete a picture from a gallery</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to delete a picture in a plant's gallery </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouse detail page for a specific plant is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "plant gallery" button</li>
        <li>A pop-up slideshow appears, showing one by one all the images present in the gallery where the gardener has the possibility to click the "delete" button and delete the selected picture </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The picture is now deleted</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.11: Edit sensor threshold</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to set thresholds for a specific sensor </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouse detail page for a specific plant is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "edit" button, next to the selected sensor in "sensors"</li>
        <li>A pop-up appears, giving the possibility to change the upper and the lower bound of the threshold and then click on the "save changes" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The sensor's thresholds are now set and the alarm count will increase every time these bounds are exceeded</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.12: Reset alarm count</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to reset the alarm count of a specific sensor</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouse detail page for a specific plant is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "edit" button, next to the selected sensor in "sensors"</li>
        <li>A pop-up appears, giving the possibility to reset the alarm count and the click on the "save changes" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The alarm count is reset back to zero</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.13: Set a greenhouse's alarm to fixed</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to set an assigned greenhouse's alarm to fixed</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in, the greenhouse detail page for a specific plant is open and the plant has an alarm on (red status for one or more measurements)</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the "alarm switch to fixed" button, in the "edit sensor station" and then clicks on "save changes"</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The alarm is now fixed and after some time the access point will automatically reset the alarm count for all the measurements</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 3.14: Set a greenhouse's alarm to fixed (manually)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to set an assigned greenhouse's alarm to fixed</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is in proximity to the physical access point</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener clicks on the physical button, on the access point</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The alarm is now fixed and after some time the access point will automatically reset the alarm count for all the measurements in the web-app</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 3.15: Add plant to greenhouse</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>A gardener wants to add a new plant to the greenhouse</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The gardener is logged in and the greenhouse detail page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The gardener enters a new plant's name in the field "new plant name" and then clicks on the "change plant" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The plant that was previously in the greenhouse will be detached, a new plant will be created with the selected name and added to the greenhouse</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

## 3.4 Test cases Administrators


<table>
  <tr>
    <th colspan="2">TC 4.1: View all sensor stations in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view all sensor stations in the system </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "manage sensor station" button, under "administrator utility" and gets redirected to the sensor station management page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can now see all the sensor stations present in the system</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 4.2: Create sensor station</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to create a new sensor station </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the sensor station management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "create new sensor station" button</li>
        <li>A pop-up appears, giving the possibility to enter sensor station's details</li>
        <li>After at least all the mandatory fields are inserted, the administrator clicks on the "create" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The new sensor station is created and present in "sensor stations", in the sensor station management page</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.3:Edit sensor station</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to edit a sensor station</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the sensor station management page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "edit" button</li>
        <li>A pop-up appears, giving the possibility to edit the sensor station's details, then the administrator can click on the "save" (or "abort") button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The sensor station is now edited</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>



<table>
  <tr>
    <th colspan="2">TC 4.4: Delete sensor station</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to delete a sensor station </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the sensor station management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "delete" button next to the sensor station that needs to be deleted</li>
        <li>A pop-up appears, asking the administrator for confirmation where "yes" (or "no") can be clicked</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The sensor station is now deleted and is not present anymore in "sensor stations", in the sensor station management page</li>
        <li>All entities related to the specified sensor station will be detached</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 4.5:Assign gardener to sensor station</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to assign a gardener to a sensor station  </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the sensor station management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "edit" button on the chosen sensor station</li>
        <li>A pop-up appears, giving the possibility to assign gardeners to the sensor station and then save the updates</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The selected gardeners are now assigned to the sensor station</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>
 <ul>
  <li> "Add Gardeners:" pop-up sometimes appears behind the pop-up for "Edit Sensor Station" </li>
      </ul>
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [x] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.6:Remove gardener from sensor station</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to remove an assigned gardener from a sensor station</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the sensor station management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "edit" button on the chosen sensor station</li>
        <li>A pop-up appears, giving the possibility to remove previously assigned gardeners from the sensor station and then save the updates</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The selected gardeners are now removed from the sensor station</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>
 <ul>
  <li> "remove Gardeners:" pop-up sometimes appears behind the pop-up for "Edit Sensor Station" </li>
      </ul>
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [x] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.7: View sensor station's data (through sensor station management page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view a sensor station's data </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the sensor station management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "open detail-view" button next to a sensor station and gets redirected to the greenhouse details page. </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can see all the sensor station's data. </li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.8: View all access points in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view all the access points in the system </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "manage access points" button, under "administrator utility" and gets redirected to the access points management page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can now see all the access points present in the system</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.9: Create access point</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to create a new access point </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the access points management page is open</td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "create new access point" button</li>
        <li>A pop-up appears, giving the possibility to enter access point's details</li>
        <li>After at least all the mandatory fields are inserted, the administrator clicks on the "create" button</li>
        <li>This will automatically create config.yaml file</li>
        <li>The administrator then upload the Rasperry directory to the physical access point and starts the accesspoint_applicaion.py</li>
        <li>The administrator then needs to validate the access point from the access points management page by clicking the "validate" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The new access point is created and present in "access points", in the access point management page</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.10: Delete access point</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to delete an access point </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the access point management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "delete" button next to the access point that needs to be deleted</li>
        <li>A pop-up appears, asking the administrator for confirmation where "yes" (or "no") can be clicked</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The access point is now deleted and is not present anymore in "access points", in the access point management page</li>
        <li>All entities related to the specified access point will be detached</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.11: Pair sensor station to access point (from greenhouse details page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants pair a selected sensor station to an access point</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in, the selected access point and a new sensor station are already created and the greenhouse details page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator enters the newly created sensor station's name into the program called Arduino_programm_v1.0.ino and uploads the code to the physical sensor station</li>
        <li>On the web-app, the administrator clicks the "pairing" button</li>
        <li>On the arduino, the administrator presses the physical "pairing" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The sensor station is now paired with the access point and measurements will be sent from the sensor station to the web-app</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>
    <ul>
     <li>the time shown in the webapp for the measurement is not the same as the actual time of the measurement. The measurement took place at 19:53 and the webapp shows 17:44.</li>
      </ul>
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [ ] cosmetic discrepancies
- [x] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.12: Pair sensor station to access point (from sensor station management page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants pair a selected sensor station to an access point</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in, the selected access point and a new sensor station are already created and the sensor station management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator enters the newly created sensor station's name into the program called Arduino_programm_v1.0.ino and uploads the code to the physical sensor station</li>
        <li>On the web-app, the administrator clicks the "pairing" button</li>
        <li>On the arduino, the administrator presses the physical "pairing" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The sensor station is now paired with the access point and measurements will be sent from the sensor station to the web-app</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td><ul>
     <li>the time shown in the webapp for the measurement is not the same as the actual time of the measurement. The measurement took place at 19:53 and the webapp shows 17:44.</li>
      </ul>
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [ ] cosmetic discrepancies
- [x] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.13:Generate QR code for plant (from plant management page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to create a QR code for a plant </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the plant management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "generate QR code button" on the selected plant and gets redirected to generate QR code page for that specific plant</li>
        <li>The administrator can select from different designs and color and then press the "update" button to regenerate the QR code</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator have a working QR code that redirects to the selected plant. The QR code can be right-clicked and saved.</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 4.14:Generate QR code for plant (from generate QR code page)</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to create a QR code for a plant </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "generate QR code button"under "administrator utility" and gets redirected to the generate QR code page</li>
        <li>The administrator can enter the plant id and select from different designs and color and then press the "update" button to regenerate the QR code</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator have a working QR code that redirects to the selected plant. The QR code can be right-clicked and saved.</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 4.15:View all users in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view all the users in the system </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "manage users" button, under "administrator utility" and gets redirected to the user management page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can now see all the users present in the system</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 4.16: Create user</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to create a new user </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the user management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "create new user" button</li>
        <li>A pop-up appears, giving the possibility to enter user's details</li>
        <li>After at least all the mandatory fields are inserted, the administrator clicks on the "create" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The new user is created and present in "users", in the user management page</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.17:Edit user</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to edit a user  </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the user management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "edit" button, next to the selected user</li>
        <li>A pop-up appears, giving the possibility to modify user's details (except for the username and the password)</li>
        <li>After the modifications are done, the administrator clicks on the "save" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user is now edited</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 4.18:Modify user's role </td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to modify a certain user's role  </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the user management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "edit" button, next to the selected user</li>
        <li>A pop-up appears, giving the possibility to modify user's roles (gardener,admin)</li>
        <li>After the modifications are done, the administrator clicks on the "save" button</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user has now the specified roles</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.19: Delete user</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to delete a user </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the user management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "delete" button next to the user that needs to be deleted</li>
        <li>A pop-up appears, asking the administrator for confirmation where "yes" (or "no") can be clicked</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The user is now deleted and is not present anymore in "users", in the user management page</li>
        <li>All entities related to the specified user will be detached</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>




<table>
  <tr>
    <th colspan="2">TC 4.20:View all log entries</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view all the logs entries </td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "audit logs" button, under "administrator utility" and gets redirected to the audit log page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can now see all the log entries</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>

<table>
  <tr>
    <th colspan="2">TC 4.21:View all measurements in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view all measurements in the system</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "manage measurements" button, under "administrator utility" and gets redirected to the measurement management page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can now see all the measurements in the system</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.22:Delete measurements in time interval</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to delete measurements in a certain time interval</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the measurement management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "delete all measurement from to" button</li>
        <li>A pop-up appear, giving the administrator the possibility to select two dates and times, then the "delete" (or "cancel") button can be clicked</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>All the measurements between the two dates are now deleted</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>
 <ul>
   <li> -The calendar that appears to select date could have a button to confirm the entered date. To confirm the selected date, it is necessary to click outside the pop-up and this is not obvious. - Sometimes the calendar disappears by itself again.</li>
  </ul>
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [ ] cosmetic discrepancies
- [x] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.23:Delete measurements in time interval for a specific sensor station</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to delete measurements in a certain time interval for a specific sensor station</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the measurement management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "delete for sensor station" button</li>
        <li>A pop-up appear, giving the administrator the possibility to select a sensor station and two dates and times, then the "delete" (or "cancel") button can be clicked</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>All the measurements between the two dates are now deleted for the specified sensor station</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td>
 <ul>
   <li> The calendar that appears to select date could have a button to confirm the entered date. To confirm the selected date, it is necessary to click outside the pop-up and this is not obvious. - Sometimes the calendar disappears by itself again.</li>
  </ul>
</td>
  </tr>
  <tr><td></td>
    <td>

- [ ] OK
- [ ] cosmetic discrepancies
- [x] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.24:View all approved images in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view all the approved images in the system</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "manage images" button, under "administrator utility" and gets redirected to the image management page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can now see all the approved images in the system</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.25:View all awaiting approval images in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to view all the images that are awaiting approval in the system</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and any Plant HealthTM website page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "manage images" button, under "administrator utility" and gets redirected to the image management page</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The administrator can now see all the images that are awaiting approval in the system</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.26:Delete an image from all the approved images in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to delete an image from all the approved images in the system</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the image management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "delete image" button that is in the top right corner on the selected image from the approved images</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The selected image is now deleted from the system</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.27:Approve an image from all the awaiting approval images in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to approve an image from all images that are awaiting approval in the system</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the image management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "approve image" button that is in the top left corner on the selected image from the awaiting approval images</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The selected image is now approved, it will be visible in the respective plant gallery and not anymore present in the awaiting approval images</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


<table>
  <tr>
    <th colspan="2">TC 4.28:Deny an image from all the awaiting approval images in the system</td>
  </tr>
  <tr>
    <td><b>Use Case:</b></td>
    <td>An administrator wants to deny an image from all images that are awaiting approval in the system</td>
  </tr>
  <tr>
    <td><b>Initial state:</b></td>
    <td>The administrator is logged in and the image management page is open </td>
  </tr>
  <tr>
    <td><b>Action:</b></td>
    <td>
      <ol>
        <li>The administrator clicks on the "deny image" button that is in the top right corner on the selected image from the awaiting approval images</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td><b>Expected result state: </b></td>
    <td>
      <ul>
        <li>The selected image is denied, it will not be visible in the respective plant gallery nor anymore present in the awaiting approval images</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td><b>Observed discrepancies: </b></td>
    <td></td>
  </tr>
  <tr><td></td>
    <td>

- [x] OK
- [ ] cosmetic discrepancies
- [ ] medium discrepancies
- [ ] major discrepancies
- [ ] System unusable

</td>
  </tr>
</table>


## 3.3 Further non-functional test cases
> In this section, list further test cases for traceable and reproducible verification of relevant non-functional AN requirements. These are e.g:
> - Tests on response times
> - Consistency of the user interface
> - Stability tests
# 4. Appendix
## 4.1 Glossary
## 4.2 Referenced documents
<ul>
  <li>Software concept (version, date)</li>
</ul>