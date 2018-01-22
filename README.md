# SimpleHackerNewsReader
A simple, bare-bones reader for HackerNews (https://news.ycombinator.com)



To generate the test coverage report, run

 `./gradlew clean jacocoTestReport`
  
  in the project directory. The command requires at least one device/emulator to be currently connected with adb. Run `adb devices` to verify this is true.

If the `jacocoTestReport` command succeeds, the generated report will be found at `{projectRoot}/app/build/reports/coverage/debug/index.html`


__Possible Issues__

* If a test fails and you get this error: `android.support.test.espresso.PerformException: Error performing 'android.support.test.espresso.contrib.RecyclerViewActions$ActionOnItemAtPositionViewAction@6d2172b' on view 'Animations or transitions are enabled on the target device.` then you need to disable animations on the device/emulator. It is an issue with Espresso testing library.
Goto the Android's Settings -> Developer Settings, and disable Animations by setting "Animation Off" on 
  1. Window animation scale
  2. Transition animation scale
  3. Animator duration scale
  
 
* If you check the coverage reports and the coverage is zero all through, it may be because gradle failed to retrieve the coverage .ec file from the device. Please try an emulator (or maybe a Nexus device)