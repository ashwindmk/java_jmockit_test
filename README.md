# JMockit

### Facts
- If multiple constructors are available, JMockit will use the constructor with all injectables available.
- You can mock the constructor of any class with MockUp and $init.
- You can mock specific methods of any class using MockUp.
- You cannot create expectations for the @Tested class methods.

### Delegate
- Used in Expectations for performing some operation before returning result.
- Used for capturing arguments.

### Verification
- You can only verify things once. This is true for any unit test framework.
