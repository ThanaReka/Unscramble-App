Unscramble app
=================================

Single player game app that displays scrambled words. To play the game, player has to make a
word using all the letters in the displayed scrambled word.
This code demonstrates the Android Architecture component- ViewModel and StateFlow.

Understand how the Android app architecture guidelines recommend separating classes that have 
different responsibilities and driving the UI from a model.


Define test strategy and implement unit tests to test the ViewModel and StateFlow in the Unscramble app. 
Make sure that you write tests alongside your app features to confirm that your apps work properly 
throughout the development process, as you continue to build Android apps.

Summary
- Use the testImplementation configuration to indicate that the dependencies apply to the local test source code and not the application code.
- Aim to categorize tests in three scenarios: Success path, error path, and boundary case.
- A good unit test has at least four characteristics: they are focused, understandable, deterministic, and self-contained.
- Test methods are executed in isolation to avoid unexpected side effects from mutable test instance state.
- By default, before each test method executes, JUnit creates a new instance of the test class.
- Code coverage plays a vital role to determine whether you adequately tested the classes, methods, and lines of code that make up your app.