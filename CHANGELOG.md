[keepachangelog]: https://keepachangelog.com/en/

[marketplace:versions]: https://plugins.jetbrains.com/plugin/29234-arma-config-support/versions

# Changelog

[![Version](https://img.shields.io/jetbrains/plugin/v/29234-arma-config-support.svg?label=Version&color=2835a9&labelColor=151b23)][marketplace:versions]
[![Format](https://img.shields.io/badge/Keep_a_Changelog-[MAJOR.MINOR.PATCH]_--_YYYY--MM--DD-151b23?labelColor=f25d30)][keepachangelog]

### Planned

- fixme better samples for settings pages `ArmaConfigColorSettingsPage` and `ArmaConfigLanguageCodeStyleSettingProvider`
- Code reformatting
    - Arrays
        - Auto insert comma after last element
        - Spaces between `{}` when collapsed with elements
    - Assignments
        - Space before/after `=`
    - Comments
        - Space before/after `//`
    - Blank lines
        - After preprocessor
        - Between classes depending on if import/body (stick related import?)
        - Around classes
        - min/max inside class bodies
    - Rules
        - Don't change indent for comments and preprocessors
- Working grayout for unused classes
    - If a class has no body and is not used anywhere for inheritance
    - Quick fix to remove class (careful can be used in other included files)
- Support for usage search
    - If any class is called anywhere for inheritance than we cound that as usage (ik intellij has some framework for usages)
    - This should also support ctrl+click on the class to navigate to original import or
      to usage
    - If a class with the same name on the same indent level is called somewhere and its parent inherits after parent class or class that has inherited inherits (deepsearch)
    - Search included files?
- Optimize imports
    - imports are classes with no body, and we optimize by removing unused ones (ones never used for inheritance)
    - Only works for config.cpp?
    - Search all included files?
    - Requires basiclly a system for resolving includes
- Separate Highlighting for macro `()` from normal `()`. Similar to single quote strings, but a level above
- Smart Enter:
    - When invoked on a collapsed `{}`, automatically expand it and place cursor inside
    - Fix Smart Enter incorrectly adding `;` on lines ending with `{`
- Fixme formatting affects block comments and defines that are multiline and indents them improperly breaking their intendend immunity, espescially for defines
- Todo allow all types of assignments and class elements on root level, but make an inspection that will shout at them if they are at root in a config.cpp file
- Include resolving (copy and paste included file into #include line)
    - consider having to resolve includes inside the file or adjust paths
    - should be under right click file or maybe `alt` + `enter`
- Suggestions/autocompletion when writing the #include path
- Suggestions/autocompletion when writing a path in macro at some point?
- Filepath detection in strings (both `''` and `""`) and make em clickable
- Make it so that some live templates don't activate when in comments or have them have that context in general if possible
- SQF Injection support
- Improve macro highlihting, keyword purple, first word (separated by spaces) white, everything assigned in Yellow for example

## [Unreleased]

### Added

- Comment keybinds support <kbd>Ctrl</kbd>+<kbd>/</kbd> and <kbd>Ctrl</kbd>+<kbd>Shift</kbd>+<kbd>/</kbd>
- Clickable `#include` file paths and improved highlighting
- File Structure window integration
- Editor completion matcher for `[]`, `{}` and `""`
- Automatic `;` insertion when typing `{}`
- Smart Enter processor
- Live Templates group and basic built-in templates
- Code reformatting
    - Semicolon always collapse with no-space
    - Collapse empty `{}` blocks
    - Class
        - Wrapping `ALWAYS/SMART`
        - `{` and `}` newline placement
        - Space before/after `:`
        - Spaces between `{}` when collapsed with elements
        - Space before `{`
    - Arrays
        - Wrapping `ALWAYS/NEVER/SMART`
        - `{` and `}` newline placement
        - Leading commas
        - Space before/after `=`
- Inspection for duplicate parameter assignment in the same class
- Inspection for duplicate class in the same scope
- Inspection for identifiers starting with numbers
- Inspection for `#include` filepath being unresolved
- Syntax support for:
    - Single-quote strings `' '` with special highlighting
    - `+=` operator for array assignments
    - Macros appearing in parameter names
    - Exponent notation in numeric expressions (`999e-006`)
    - Unary `-` operator in math expressions
    - Class names starting with a number (e.g. `30Rnd_556x45_Stanag`)
    - `&`, `>`, `<`, `!`, `+`, `-`, `*`, `/`, `%` and `^` operators inside macros
    - `##` operator
    - Nested arrays
    - Trailing comma in arrays with an inspection (Acceptable in HEMTT, but not BI Tools)
    - Improved macro syntax
    - Block comments in more locations `/* */`

### Dev

- Improved and cleaned up `README` and `CHANGELOG`
- Added a section to `README` explaining how to manually build the plugin
- Massive rename of all internal `ArmaConfig...` classes to `Cfg...`
- Major grammar refactor
- Major lexer refactor
- Widespread internal cleanup due to class renames, grammar and lexer changes
- Test resources moved from `src/main` to `src/test`
- Run configurations saved to project
- Auto-open of `src/test/resources` as a project when using `runIdeClean` for testing
- Suppressed text localization, capitalization and lack of logo warnings in `plugin.xml`

## [1.0.1] - 2025-12-03

### Dev

- Updated `README` with plugin ID for correct links
- Populated `CHANGELOG` with version history

## [1.0.0] - 2025-12-03

### Added

- Syntax parsing with support for:
    - Classes
    - Class inheritance
    - Classes without bodies
    - Preprocessor directives: `#include`, `#define`
    - `delete` keyword
    - Line and block comments
    - Macro usage inside properties and class names
    - Strings
    - Arrays
    - Integers and Floats
    - Math expressions
- Syntax highlighting
- Breadcrumb provider and Sticky Lines
- IntelliJ file icons (different for `config.cpp` vs other files)
- IntelliJ Gradle tasks integration
- Highlighting settings page
- Inspections
    - Commas inside macros
    - Array syntax validation `property[] = {};`
