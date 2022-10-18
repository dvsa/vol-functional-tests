These classes are beincag created to refactor a lot of duplicate code that can be places into page wise journey methods. E.g:
- completePage() can tick a checkbox, check some text and save anc continue.

Instead, what is currently in the code is separate methods repetitively being used throughout the code E.g:
- untilOnPage(), getPageHeading(), assertEquals(...), saveAndContinue() etc.