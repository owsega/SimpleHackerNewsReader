# SimpleHackerNewsReader
A simple, bare-bones reader for HackerNews (https://news.ycombinator.com)



To generate the test coverage report, run

 `./gradlew clean jacocoTestReport`
  
  in the project directory. The command requires at least one device/emulator to be currently connected with adb. Run `adb devices` to verify this is true.

If the `jacocoTestReport` command succeeds, the generated report will be found at `{projectRoot}/app/build/reports/coverage/debug/index.html`
