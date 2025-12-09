[keepachangelog]: https://keepachangelog.com/en/1.1.0/

[marketplace:versions]: https://plugins.jetbrains.com/plugin/29234-arma-config-support/versions


# Changelog

[![Version](https://img.shields.io/jetbrains/plugin/v/29234-arma-config-support.svg?label=Version&color=2835a9&labelColor=151b23)][marketplace:versions]
[![Format](https://img.shields.io/badge/Keep_a_Changelog-[MAJOR.MINOR.PATCH]_--_YYYY--MM--DD-151b23?labelColor=f25d30)][keepachangelog]

### TODO

- Code reformatting
  - Arrays
    - Auto add comma after last element
  - Classes
    - Wrapping `ALWAYS/NEVER/SMART`
    - `{` on newline placement
    - Space before/after `:`
    - Space before `{`
  - Assignments
    - Space before/after `=`
  - Comments
    - Space before/after '//'
  - Blank lines
    - After preprocessor
    - Between classes depending on if import/body (stick related import?)
    - Around classes
    - min/max inside class bodies
  - Rules
    - Don't change indent for comments and preprocessors
- Working grayout for unused classes, as in if a class has no body and is not used anywhere for inheritance than we gray
  out, with quick fix to remove class (careful can be used in other included files)
- Support for usages, basically if any class is called anywhere for inheritance than we cound that as usage (ik intellij
  has some framework for usages) and this should also support ctrl+click on the class to navigate to original import or
  to usage
- Optimize imports IntelliJ support, where imports are classes with no body, and we optimize by removing unused ones (
  ones never used for inheritance)
- Highlighting for MACROS with variables eg `MACRO(var)`
- If I smart enter on a line that has a collapsed {} than I want it to open and I want to be inside
- fix smart enter adding `;` at line ending with `{` (opening of block)
- fixme add better samples for settings pages `ArmaConfigColorSettingsPage` and `ArmaConfigLanguageCodeStyleSettingProvider`
- Fixme formatting affects block comments and defines that are multiline and indents them improperly breaking their intendend immunity, espescially for defines
- Todo allow all types of assignments and class elements on root level, but make an inspection that will shout at them if they are at root in a config.cpp file
- Support for weird characters in include path
- Action on iclude paths allowing to copy paste given file into that file, consider having to resolve includes inside the file or adjust paths, should be under right click file or maybe `alt` + `enter`
- Create a setting or something that allows you to set the file as the PREP file and make it check if all functions from x folder are PREPED
- Suggestions/autocompletion when writing the #include path
- Suggestions/autocompletion when writing a path in macro at some point?

## [Unreleased]

### Added

- Support for single quote strings `' '` with highlighting
- Support for IntelliJ comment keybinds, so that ctrl+/ comments out the whole line with single line comment
- Working file structure IntelliJ window
- Inspection for duplicate parameter assignment in same class
- Code reformatting
    - Collapse empty `{}`
    - Arrays
      - Wrapping `ALWAYS/NEVER/SMART`
      - `{` and `}` newline placment
      - Leading commas
      - Space before/after `=`
- Editor completion matcher for `[]` `{}` `""` pairing
- Automatic `;` autocompletion for `{}` when typing
- Smart enter processor
- Support for `+=` with array assignment
- Support for use of macros in parameter names
- Support for exponent in math expressions, like `999e-006`
- Support for IntelliJ Live Templates and some basic templates
- Configurations saved to project
- Auto-open of `src/test/resources` as project when testing with `runIdeClean`
- Clickable links to files for `#include` and string highlighting for the path
- Support for classnames starting with numbers, like `30Rnd_556x45_Stanag`
- Support for `>`, `<` and `!` operators inside macros
- Support for math tokens inside of macros `+-*/%^`
- Support for `##` operator in identifiers `PREFIX##_Vehicles`
- Support for `&` inside of macros
- Support for nested arrays
- Support for trailing `,` in arrays with an inspection
- Inspection for identifiers starting with numbers

### Changed

- Significant refactor: Renamed all `ArmaConfig...` class names to `Cfg...`
- Improved and prettified `README.md` and `CHANGELOG.md`
- `README.md` added a new guide on how to manually make a build of the plugin
- `README.md` updated plugin description
- Moved test resources from `src/main` to `src/test`
- Reworked lexer parsing for macros
- Significant rework to nearly everything due to large grammar `ArmaConfig.bnf` file changes
- Lexer code cleanup and rework

### Fixed

- Suppressed some localization and text format warnings in `plugin.xml`

## [1.0.1] - 2025-12-03

### Changed

- Filled `CHANGELOG.md` with info
- Updated `README.md` with plugin id for links

## [1.0.0] - 2025-12-03

### Added

- Working parsing with support for:
    - classes
    - class inheritance
    - no body classes
    - preprocessor #include and #define
    - delete keyword
    - line and block comments
    - inline preprocessor usage inside of properties and class names
    - properties
        - string
        - array
        - int
        - float
        - complex math expressions
- Syntax Highlighting
- Breadcrumb provider (also works for Sticky Lines)
- Existing IntelliJ Icons for files (separate for config.cpp vs all others)
- Internal gradle tasks
- Settings page for highlighting
- Inspections
    - commas inside of parentheses
    - syntax check for correct array properties usage property[] = {};
