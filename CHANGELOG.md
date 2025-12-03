# Changelog

The format is loosely based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project loosely adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Format: `[MAJOR.MINOR.PATCH] - YYYY-MM-DD`

### TODO
Classes with no body are basically imports so thats what im gonna call them

- Code reformatting (that would work with default IntelliJ ctrl+alt+l keybind)
  - Option for `{` bracket opening class body on same line or newline
  - class import should stick (no blank line) to class below if the class below inherits from the import, this also excludes the import class from sticking to other import classes (it should have a blank line separating them)
  - option for amount of blank lines between import classes and a normal class. import classes can be next to eachother (on first reformat we keep any blank lines between, on second reformat we make sure there is no blank lines between import classes)
  - formatting for : that is used for class inheritance so that it has smart space on both sides
  - make sure to not touch stuff inside preprocessor macros that have () as in like Q() or TAG(nothing touched inside)
  - dont touch anything inside of normal `" "` and single quote strings `' '`
  - collapse empty {} blocks
  - smart space after class name / before opening of class body, and also working for inheritance
  - single space before single line // comments
  - option to have a space after a // comment opens eg: //test vs // test (option is have space or no space or untouched)
  - options for array wrapping in config, talking about {}, where I want the reformat to keep current way, either it will keep single line if it currently is and make sure to add a space after each , (make this an option) or if the array was multiline to any extent I want it to reformat to be multiline fully
  - for arrays I also want an option to have , at the start of each line or at the end of each line when arrays are multiline
  - for multiline arrays first item should be on newline compared to `{` and the closing `}` should also be on a newline alinged with the indent of the line where `{` was
  - option for amount of blank lines between any preprocessor (#include or #define) stuff and a class
- Support for {} and () so that when you place start it auto places second part like it usually does in other file types in IntelliJ
  - for any {} outside of strings we also want to make it so it auto adds a `;` at the end like `{};`
- Support for line autocompletion adding auto ; (IntelliJ thing that is under ctrl+enter normally i think)
- Working grayout for unused classes, as in if a class has no body and is not used anywhere for inheritance than we gray out
- Support for usages, basically if any class is called anywhere for inheritance than we cound that as usage (ik intellij has some framework for usages) and this should also support ctrl+click on the class to navigate to original import or to usage
- Optimize imports IntelliJ support, where imports are classes with no body, and we optimize by removing unused ones (ones never used for inheritance)

## [Unreleased]

### Added

- Support for single quote strings `' '` with highlighting
- Support for IntelliJ comment keybinds, so that ctrl+/ comments out the whole line with single line comment
- Working file structure IntelliJ window
- Inspection for duplicate parameter assignment in same class

### Changed
- `README.md` added a new guide on how to manually make a build of the plugin

### Fixed

- Support for classnames starting with numbers, like `30Rnd_556x45_Stanag`
- Errors for `>`, `<` and `!` operators inside macros

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
