# PayBuddy

For a more detailed explaination use this link.
[Beskrivning.pdf](https://github.com/ViggoLagerstedtEkholm/PayBuddy/files/6352163/Beskrivning.pdf)

What is PayBuddy?
This app makes it possible to keep track of people that owe you money using
occasions. 

This application was built as a final project for an Android course.

PayBuddy was selected as one of the best final projects during VT2021 (Spring term 2021).

Why would I use this app?
The reason I developed this app is to keep track of anyone that owes you money. It
can also work as a reminder to pay for subscriptions or other payments. After the
user has added a few occasions they can also see them on the map, this is fun if you
want to show your friends where you have been in the past or to see an overview of
your occasions.

Why would I use this app?
The reason I developed this app is to keep track of anyone that owes you money. It
can also work as a reminder to pay for subscriptions or other payments. After the
user has added a few occasions they can also see them on the map, this is fun if you
want to show your friends where you have been in the past or to see an overview of
your occasions.

Future improvements
The app does not trigger notifications when the pending occasion expires but the user can make any pending occasion expire by holding the item for 3 seconds and accepting the dialog query.

When the user rotates the screen or recreates the activity the ViewModels will observe these changes. But if the android operating system decides to kill the background process to free memory our ViewModels will also be destroyed. If we have searched for something in the app this will no longer be saved once the user opens the app again. To solve this we need to use both ViewModel in combination with SavedInstanceState.

Working with both ViewModels and savedInstanceState improves the user experience for every case.

The app does not currently support this but should be implemented before any release.

Development environment
The app is developed in Android Studio and the emulated phone was the “Pixel 4 API 30” with Android 11 Target. The emulator I was developing on also has Google Play installed.

There exists 3 types of occasions:
1. Pending occasion - This is an occasion that we have not yet marked as paid or expired. 
- We can click “Register as paid” to make this pending occasion move to history. 
- We can click “Remove” to delete this pending occasion.
- We can click “See location on maps” to see where we put our occasion.

3. History occasion - This is an occasion that we have marked as paid and is therefore history.
- We can click “Remove” to delete this history occasion.
- We can click “Postpone” to make this occasion a pending occasion.
- We can click “See location on maps” to see where we put our occasion.

6. Expired occasion - This is an occasion that has not yet been paid before the expiring day.
- We can click “Remove” to delete this expired occasion.
- We can click “Postpone” to make this occasion a pending occasion.
- We can click “See location on maps” to see where we put our occasion.
- We can click “Contact” to contact whoever owes us money.

Home Screen:
Total item costs - Every item cost multiplied by the quantity of that item for all pending and expired occasions.
Total expired occasions - Total amount of occasions expired.
History occasion - Total amount of occasions that has been paid.
See all locations (Button) - Displays all the occasions on the map.

Adding a occasion:
Pick a date - Shows a dialog and the user can input any date.
Add items - Shows a dialog and the user can add a new item with the following attributes:
            - Item name -  The name of the item
            - Price - The item cost.
            - Quantity - The amount of this item we want to add.
            - Person name - The person that has this item.

Add location - After the user provides permission the app will track the user position, the following information will be used:
Latitude/longitude/altitude - Coordinates of the user.
Address - The nearest address to those coordinates.
Sensor mode - When we use the low accuracy the phone does not need to triangulate the position using nearby cell towers. This does save battery and provides a better user experience.

If the user enables the high accuracy the app will try to triangulate the position and more battery will be used.

Location Updates - The user can enable / disable the location updates.

Home screen:
The home screen helps the user see a summary of the following:
- Total item costs - Every item cost multiplied by the quantity of that item for all pending and expired occasions.
- Total expired occasions - Total amount of occasions expired.
- History occasion - Total amount of occasions that has been paid.
- See all locations (Button) - Displays all the occasions on the map.

When the user first enters the home page all numbers will start counting up with an animation. 

When you open the map it will zoom to the centroid of all the location latitude and longitude coordinates. This will work for clusters of occasions but needs improvement if the user places 2 occasions on different parts of the globe.

You can click any icon on the map and see all information about that specific occasion.

Settings:
Layout style - Switch between dark and light themes, this is saved in shared preferences and starts with the prefered theme.
The user can perfrom the following instructions:
Delete history - Deletes all history occasions. (with items/location)
Delete all active occasions - Deletes all pending occasions. (with items/location)
Delete all expired occasions - Deletes all expired occasions. (with items/location)
Wipe all data - Removes all occasions (expired, pending, history) (with items/location)

Flexability:
The app supports landscape mode on every page.
The logo supports different versions of android.

Architecture:
This app uses MVVM which makes it possible to move most of the logic away from the fragments. 

I have 3 repositories
Location
Items 
Occasion 

Each repository has methods for modifying each model.
I have 4 DAOs, these are the interfaces that communicate with the database. To query the database I use Room with SQlite.
All the data is fetched using LiveData, so any change to the database will refetch the data to the view.
This pattern makes it easy to work with the fragments. No need to worry about the phone rotating because everything will get fetched from the ViewModels once the fragment is destroyed and recreated.

