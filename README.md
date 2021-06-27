# Flight Remote Control - Android App

in this assinment, we created a remote control for Flight Simulator (FlightGear simulator).

The remote looks like:

![remote](resources/app.png?raw=TRUE)

The 'Connect' button colored in green means it is connected to the server. when it's not the button is red.

## Project Structure:
we used the MVVM architecture - as shown at the diagram below:
![UML](resources/class_diagram.png?raw=TRUE)  
The view holds a reference to the view-model (but the view-model does not hold reference to the view) and can activates its methods, although most of the communication between the view and the view-model is made by data binding. The view-model implements the ```Observable``` interface so it can notify whenever one of its properties have been changed.  
The view-model holds a reference to the model (but the model does not hold reference to the view-model) and can activates its methods, The view-model implements the ```Observable``` interface so it can notify whenever one of its properties have been changed.  
Since the android UI package doesn't supply a Joystick, we made a special custom view for this purpose.

## Installing And Running The Application:

There are two options for running the application:
- running it on the pc, using an emulator.
for this we recommend using "Android Studio".
- installing the app on an android phone - using this [apk][apk-link]


in both ways, you first need to run the FlightGear simulator with the following settings:
```--telnet=socket,in,10,127.0.0.1,<port>,tcp```

after you run the FlightGear, you can connect the remote to it.

In the text box, with `Enter IP` - enter the ip of the computer the FlightGear is running on.

In the text box with `Enter port` - enter the port you run the FlightGear on.
After you've entered the above, you can press `Connect`, and the button should turn green.
if it's not - that means no connection was made, and you should check for errors in the ip and port you entered, and retry to connect.

