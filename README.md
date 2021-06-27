# Flight Remote Control - Android App

in this assinment, we created a remote control for Flight Simulator (FlightGear simulator).

The remote looks like:
![remote](resources/app.png?raw=TRUE)


## Project Structure:
we used the MVVM architecture - as shown at the diagram below:
![UML](resources/class_diagram.png?raw=TRUE)
The view holds reference to the view-model (but the view-model does not hold reference to the view) and can activates its methods, although most of the communication between the view and the view-model is made by data binding. The view-model implements the ```Observable``` interface so it can notify whenever one of its properties have been changed.
The view-model holds reference to the view-model (but the model does not hold reference to the view-model) and can activates its methods, The view-model implements the ```Observable``` interface so it can notify whenever one of its properties have been changed.
Since the android UI package doesn't supply a Joystick, we made a special custom view for this purpose.

## Installing And Running The Application:

There are two options for running the application:
- running it on the pc, using an emulator.
for this we recommend using "Android Studio".
- installing the app on an android phone - using this [apk][apk-link]

