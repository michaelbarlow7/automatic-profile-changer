# Automatic Profile Changer

This is just a simple little app I made to automatically change Profiles at certain times, determined by the user. Profiles are a CyanogenMod feature, so this won't work on regular Android and will probably crash. I made this for myself, so I've only tested this on CyanogenMod 12.1 based on Android 5.1.1, running on a Samsung Galaxy S5 (klte). It'll probably break on your phone.

I used the Seamus Phelan's [Profile Switcher](https://github.com/seamusphelan/ProfileSwitcher) code as a reference. The CyanogenMod specific classes were obtained directly from my phone using the method outlined on his README page (from /system/framework/framework.jar). Apparently you can find the sources from the CyanogenMod sources under android/system/out/target/common/obj/JAVA_LIBRARIES/framework_intermediates/.

I wrote most of it in Kotlin, cos why not. And I used Mercurial instead of Git, cos why not. Learning new things is fun sometimes. 
