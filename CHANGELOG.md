# Changelog

The format is loosely based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project loosely adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Format: `[MAJOR.MINOR.PATCH] - YYYY-MM-DD`

### TODO

- Code reformatting (that would work with default IntelliJ ctrl+alt+l keybind)

## [Unreleased]

### Added

- Support for IntelliJ comment keybinds, so that ctrl+/ comments out the whole line with single line comment
- Working file structure IntelliJ window
- Inspection for duplicate parameter assignment in same class

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
