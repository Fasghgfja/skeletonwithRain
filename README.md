# Plant Health

Plant Health has set itself the goal to equip unused public and private space with little greenhouses in order to bring back plants into offices, hallways etc.\
It shows that there are three main reasons why people shy away from plating plants:
1. Lack of time
2. Knowledge
3. Environment

In order to achieve this goal an overall technical solution should be developed, that tracks the plants in the greenhouses and uses relevant data of ground, air and light quality to display in an web application for users which environmental circumstances may be good or bad for the plant.

## Description

Greenhouses are placed in public or pirvate office rooms. These greenhouses are equipped with sensors to measure ground humidity, air humidity, air pressure, air temperature and light intensity. They are also equipped with LEDs and speakers to give audial and visual feedback.\
These sensor stations communicate with an access point. Data will be transmitted to the access point. The access point checks if certain thresholds are exceeded. Visual signals will be displayed on the sensor station if this is the case. Access points will also store the received data temporarly before then transmitting it to the web server.\
The core of the project is a webapp that allows users to keep track of their plants. Therefore, the data sent to the webserver is displayed in the webapp.\
Users are able to follow certain greenhouses in order to see the different measurements made. This is displayed in a dashboard view with colorcoding (red = bad, yellow = neutral, green = good). Users can also see more in-depth information including pictures and more precise information on each measured value.\
Plants may also have a gardener assigned that takes care of them. This gardener has an even more in-depth view on the state of the plant and is able to track different measurements over a specific time frame.

### Used Technologies
- Java 17
- Prime Faces
- MySQL
- Arduino
- Raspberry Pi


## Contributors:
- Giuliano Giambertone
- Stefan Huter
- Deniz Özkaya
- Marco Ponti
- Tom van Loon
