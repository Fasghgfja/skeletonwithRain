<sub><b>Hinweis:</b> Dieses Dokument versteht sich als Guideline und dient der Strukturierung des Abnahmeberichts. Bitte halten Sie die in diesem Bericht zusammengefassten Beobachtungen, Kritiken, und Verbesserungsvorschläge in einer verständlichen und konstruktiven Art und Weise fest. Das Entwicklungsteam muss zu Ihrem Bericht schriftlich Stellung nehmen und gegebenenfalls Änderungen am System vornehmen. Wenn notwendig, untermauern Sie Ihren Bericht durch Screenshots.</sub>

# 1. Results
Procedure:
1. server version of the system was tested with the given test data.
2. local version of the system was connected to the MySQL database and tested with the given test data.
3. test cases from the test script were processed individually 
4. acceptance test report created

The following users were used for testing:
- UserTesterAccount1 
- GardenerTesterAccount1
- AdminTesterAccount
- Max
- Susi
- admin

The tested system has proven to be functional. During the test, some minor errors were detected. However, these errors did not have a significant impact on the overall performance of the system. In general, the system successfully executed all required tasks. We will go into more detail about the errors we noticed in the further sections.

# 2. Functionality
![Funktionalitäten](uploads/62ded010ba084f3047440e60ff9c22f2/Funktionalitäten.jpg)


The tested system has a large number of functions, most of which work perfectly. However, during the acceptance test, some minor issues were identified that have potential for improvement. These include difficulties in filtering objects and locations, undesired changes in values between different sensor stations, and occasional problems with pop-up windows and the calendar for date selection.

1. (2.7) Some difficulties were identified when testing the filtering function. In particular, it is not very user-friendly to search for a specific object and have to enter the exact name to find the desired objects. An example of this is that one cannot just enter "Rocket" to find the plant "Alpine Rocket". Furthermore, it was found that filtering by a specific location in the plant catalogue does not work properly.

2. (3.3 & 3.4) Another problem concerns the modification of the interval between sensor station and access point or between sensor and sensor station. It was found that changes to the values of one sensor station can also have an effect on another sensor station. For example, the values for the sensor station "AAAAA" and the plant "Alpine Rocket" can also change the values for the sensor station "G4T2" and the plant "Hawaiian Snow".

3. (3.8) Furthermore, it was suggested to give an indication when a new image is available for approval. A visual indicator such as a red dot in the corner could be used to draw users' attention to new images. This would improve the efficiency of the image release.

4. (4.5 & 4.6) Problems with the pop-up windows were also noted during the test. In particular, the pop-up window "Add gardener:" sometimes appeared behind the pop-up window for "Edit sensor station". The same problem was also observed with the "Remove gardener:" pop-up window. 

![add_gardener](uploads/8a3f5debd5e3229480fb10ecd2926933/add_gardener.png)

5. (4.11 & .4.12) The time displayed in the web application for the measurement does not match the actual time of the measurement. The measurement took place at 19:53, but the web application shows 17:44 (see the times in the screenshot).

![measurement](uploads/cc4fdbd4328bf806f667dba10196bacc/measurement.png)

6. (4.22 & 4.23) Another suggestion for improvement relates to the calendar that is displayed to select the date. It was noted that it is not obvious how to confirm the selected date. The calendar should include a button to confirm the date entered, rather than trusting users to click outside the pop-up window. This improvement would make date selection easier. It was also observed that the calendar sometimes disappears by itself.

In summary, the system tested has all the required features. The system also has many additional features such as the server version. This makes it possible to open the website quickly and efficiently. The website is also very well designed. It is clearly structured and easy to use. However, during the acceptance test, some minor problems occurred that affect the usability and stability of the system. The above suggestions for improvement should be implemented to further optimise the overall performance of the system.



# 3. Performance, fault tolerance and stability
- system runs very smoothly
- no performance deficiencies detected
- it is ensured that deleted/inactive user accounts, access points and sensor stations have no effect on past reporting periods

# 4. Usability
The usability of the system is very good and fully satisfies the requirements of the target group. The user interface is attractively designed and navigation through the system is intuitive. Users can complete their tasks effectively. 

# 5. Test script
The test script was done well. The test cases were well structured and well documented. Test coverage and reusability were given.

# 6. Further anomalies
There were no other anomalies. The suggestions for improvement were mentioned in section 2.Functionalities.

# 7. Tabular list of identified deficiencies

| TC ID | Discrepancy | Classification |
| ------ | ------ | ------ |
|  2.7  | medium discrepancies | On the Plant's Catalogue page, you cannot filter the plants by a specific location. If you want to search for a plant, you have to enter the whole plant name. For example, if you only enter "Rocket", the plant "Alpine Rocket" will not be found. |
| 3.3 | medium discrepancies | When I change the value for station "AAAAA" plant "Alpine Rocket", somehow values for station "G4T2" plant "Hawaiian Snow" are also changed. (Using the web hosted version and logged in as GardenerTesterAccount1) |
| 3.4 | medium discrepancies | Same problem as in 3.3 |
| 3.8 | cosmetic discrepancies | Maybe give a hint, if new picture is waiting for approval. For example a red dot in the corner. |
| 4.5 | cosmetic discrepancies |  "Add Gardeners:" pop-up sometimes appears behind the pop-up for "Edit Sensor Station"|
| 4.6 | cosmetic discrepancies | "remove Gardeners:" pop-up sometimes appears behind the pop-up for "Edit Sensor Station" |
| 4.11 | medium discrepancies | The time shown in the webapp for the measurement is not the same as the actual time of the measurement. The measurement took place at 19:53 and the webapp shows 17:44. |
| 4.12 | medium discrepancies | The time shown in the webapp for the measurement is not the same as the actual time of the measurement. The measurement took place at 19:53 and the webapp shows 17:44.. |
| 4.22 | medium discrepancies | The calendar that appears to select date could have a button to confirm the entered date. To confirm the selected date, it is necessary to click outside the pop-up and this is not obvious. - Sometimes the calendar disappears by itself again. |
| 4.23 | medium discrepancies | The calendar that appears to select date could have a button to confirm the entered date. To confirm the selected date, it is necessary to click outside the pop-up and this is not obvious. - Sometimes the calendar disappears by itself again. |




```