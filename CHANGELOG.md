# Changelog

The format is loosely based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project loosely adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

- todo :: Code refactoring via `CTRL`+`ALT`+`L`
- todo :: Add working file structure
- todo :: Add code refactoring
- fixme :: See error in config.cpp example, where there is an issue with < in a Q() macro and also !
- fixme :: Consider fixing classnames that start with a number, seems like they are broken, check `30Rnd_556x45_Stanag`


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
